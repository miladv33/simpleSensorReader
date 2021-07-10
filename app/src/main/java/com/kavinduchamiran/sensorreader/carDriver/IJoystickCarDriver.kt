package com.kavinduchamiran.sensorreader.carDriver

import android.os.SystemClock
import android.view.MotionEvent
import com.kavinduchamiran.sensorreader.customView.joystick.JoyStickView
import com.kavinduchamiran.sensorreader.model.Controller

interface IJoystickCarDriver {

    fun drive(joyStickView: JoyStickView, newYPosition: Float) {
        with(CarControllers) {
            rightController.x = (joyStickView.centerX.toInt() * 10)
            rightController.y = Math.toDegrees(newYPosition.toDouble()).toInt() * 10
            setJoystickView(joyStickView)
        }
    }

    private fun setJoystickView(view: JoyStickView) {
        view.muckTouch(mockOnTouchJoysticks(view, CarControllers.rightController))
    }

    private fun mockOnTouchJoysticks(
        view: JoyStickView, controller: Controller
    ): MotionEvent? {
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + 50
        val metaState = 0
        val motionEvent: Int
        if (controller.getX() == 0) {
            view.justShowTouch = false
            motionEvent = MotionEvent.ACTION_UP
        } else {
            view.justShowTouch = true
            motionEvent = MotionEvent.ACTION_DOWN
        }
        return MotionEvent.obtain(
            downTime,
            eventTime,
            motionEvent,
            controller.x.toFloat(),
            controller.y.toFloat(),
            metaState
        )
    }
}
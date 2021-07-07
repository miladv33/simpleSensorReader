package com.kavinduchamiran.sensorreader

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Math.round
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), DirectionFinder {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
    }


    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mMagnet, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    fun resumeReading(view: View) {
        this.resume = true
    }

    fun pauseReading(view: View) {
        this.resume = false
    }

    override var mSensorManager: SensorManager
        get() = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        set(value) {}
    override var mAccelerometer: Sensor?
        get() = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            mSensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        set(value) {}
    override var mMagnet: Sensor?
        get() = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            mSensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        set(value) {}
    override var mLight: Sensor
        get() = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        set(value) {}
    override var mGyroscope: Sensor
        get() = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        set(value) {}
    override var resume: Boolean
        get() = false
       set(value) {}
}

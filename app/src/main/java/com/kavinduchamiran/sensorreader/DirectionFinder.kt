package com.kavinduchamiran.sensorreader

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.TextView
import kotlin.math.roundToInt

interface DirectionFinder : SensorEventListener {
    var rotationMatrix: FloatArray
        get() = FloatArray(9)
        set(value) {}
    var orientationAngles: FloatArray
        get() = FloatArray(3)
        set(value) {}
    var accelerometerReading: FloatArray
        get() = FloatArray(3)
        set(value) {}
    var magnetometerReading: FloatArray
        get() = FloatArray(3)
        set(value) {}
    var mSensorManager: SensorManager
    var mAccelerometer: Sensor?
    var mMagnet: Sensor?
    var mLight: Sensor
    var mGyroscope: Sensor
    var resume: Boolean


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        print("accuracy changed")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null ) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
//                findViewById<TextView>(R.id.acc_X).text = event.values[0].toString()
//                findViewById<TextView>(R.id.acc_Y).text = event.values[1].toString()
//                findViewById<TextView>(R.id.acc_Z).text = event.values[2].toString()
                Log.i("miladTestInterface", "onSensorChanged: X = ${event.values[0]}")
                Log.i("miladTestInterface", "onSensorChanged: Y = ${event.values[1]}")
                Log.i("miladTestInterface", "onSensorChanged: Z = ${event.values[2]}")
                System.arraycopy(event.values,0,accelerometerReading,0, accelerometerReading.size)
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            }
            if (event.sensor.type == Sensor.TYPE_LIGHT) {
                Log.i("miladTestInterface", "onSensorChanged: light = ${event.values[0]}")
//                findViewById<TextView>(R.id.light).text = event.values[0].toString()
            }

            if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
//                findViewById<TextView>(R.id.gyro_x).text =
//                    ((Math.toDegrees(event.values[0].toDouble()) + 360) % 360).toString()
//                findViewById<TextView>(R.id.gyro_y).text =
//                    ((Math.toDegrees(event.values[1].toDouble()) + 360) % 360).toString()
//                findViewById<TextView>(R.id.gyro_z).text =
//                    ((Math.toDegrees(event.values[2].toDouble()) + 360) % 360).toString()
                Log.i("miladTestInterface", "onSensorChanged: x = ${ ((Math.toDegrees(event.values[0].toDouble()) + 360) % 360)}")
                Log.i("miladTestInterface", "onSensorChanged: y = ${ ((Math.toDegrees(event.values[1].toDouble()) + 360) % 360)}")
                Log.i("miladTestInterface", "onSensorChanged: z = ${ ((Math.toDegrees(event.values[2].toDouble()) + 360) % 360)}")

            }
            updateOrientationAngles()
        }
    }

    /**
          orientation[0] = Azimuth (rotation around the -ve z-axis)
          orientation[1] = Pitch (rotation around the x-axis)
          orientation[2] = Roll (rotation around the y-axis)
       */
    fun updateOrientationAngles() {
//            // 1
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
//            // 2
        val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)
        // 3
        val degrees = (Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0
        // 4
        val angle = (degrees * 100).roundToInt() / 100

        val direction = getDirection(degrees)

        Log.i("miladTestOrientation", "updateOrientationAngles: orientation: $orientation")
        Log.i("miladTestOrientation", "updateOrientationAngles: degrees: $degrees")
        Log.i("miladTestOrientation", "updateOrientationAngles: angle: $angle")
        Log.i("miladTestOrientation", "updateOrientationAngles: direction: $direction")
    }

    fun getDirection(angle: Double): Direction {
        var direction: Direction = Direction.N

        if (angle >= 350 || angle <= 10)
            direction = Direction.N
        if (angle < 350 && angle > 280)
            direction = Direction.NW
        if (angle <= 280 && angle > 260)
            direction = Direction.W
        if (angle <= 260 && angle > 190)
            direction = Direction.SW
        if (angle <= 190 && angle > 170)
            direction = Direction.S
        if (angle <= 170 && angle > 100)
            direction = Direction.SE
        if (angle <= 100 && angle > 80)
            direction = Direction.E
        if (angle <= 80 && angle > 10)
            direction = Direction.NE

        return direction
    }


}
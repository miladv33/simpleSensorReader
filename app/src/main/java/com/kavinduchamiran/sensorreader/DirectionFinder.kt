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
    var mLight: Sensor
    var mGyroscope: Sensor
    var resume: Boolean
    fun startDirectionFinder(context: Context, sensorEventListener: SensorEventListener) {
        mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            mSensorManager.registerListener(
                sensorEventListener,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            mSensorManager.registerListener(
                sensorEventListener,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        print("accuracy changed")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && resume) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
//                findViewById<TextView>(R.id.acc_X).text = event.values[0].toString()
//                findViewById<TextView>(R.id.acc_Y).text = event.values[1].toString()
//                findViewById<TextView>(R.id.acc_Z).text = event.values[2].toString()
                System.arraycopy(
                    event.values,
                    0,
                    accelerometerReading,
                    0,
                    accelerometerReading.size
                )
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            }
            if (event.sensor.type == Sensor.TYPE_LIGHT) {
//                findViewById<TextView>(R.id.light).text = event.values[0].toString()
            }

            if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
//                findViewById<TextView>(R.id.gyro_x).text =
//                    ((Math.toDegrees(event.values[0].toDouble()) + 360) % 360).toString()
//                findViewById<TextView>(R.id.gyro_y).text =
//                    ((Math.toDegrees(event.values[1].toDouble()) + 360) % 360).toString()
//                findViewById<TextView>(R.id.gyro_z).text =
//                    ((Math.toDegrees(event.values[2].toDouble()) + 360) % 360).toString()
            }
            updateOrientationAngles()
        }
    }

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
package com.kavinduchamiran.sensorreader

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.kavinduchamiran.sensorreader.listeners.SensorChangesListener
import kotlin.math.roundToInt

interface DirectionFinder : SensorEventListener,SensorChangesListener {
    var rotationMatrix: FloatArray
        get() = FloatArray(9)
        set(value) {}
    var orientationAngles: FloatArray
        get() = FloatArray(3)
        set(value) {}
    var accelerometerReading: FloatArray
        get() = FloatArray(3)
        set(value) {}
    var gyroscopeReading: FloatArray
        get() = FloatArray(3)
        set(value) {}
    var magneReading: FloatArray
        get() = FloatArray(1)
        set(value) {}
    var lightReading: FloatArray
        get() = FloatArray(1)
        set(value) {}
    var mSensorManager: SensorManager
    var mAccelerometer: Sensor?
    var mMagnet: Sensor?
    var mLight: Sensor
    var mGyroscope: Sensor

    fun stopListening():Boolean

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        print("accuracy changed")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null ) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                Log.i("miladTestInterface", "onSensorChanged: X = ${event.values[0]}")
                Log.i("miladTestInterface", "onSensorChanged: Y = ${event.values[1]}")
                Log.i("miladTestInterface", "onSensorChanged: Z = ${event.values[2]}")
                System.arraycopy(event.values,0,accelerometerReading,0, accelerometerReading.size)
                onAccelerometerChanged(event.values)
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, magneReading, 0, magneReading.size)
                onMagnetChanged(event.values)
            }
            if (event.sensor.type == Sensor.TYPE_LIGHT) {
                System.arraycopy(event.values, 0, lightReading, 0, lightReading.size)
                onLightChanged(event.values)
                Log.i("miladTestInterface", "onSensorChanged: light = ${event.values[0]}")
            }

            if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
//
                System.arraycopy(event.values, 0, gyroscopeReading, 0, magneReading.size)
                onGyroscopeChanged(event.values)
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
            magneReading
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
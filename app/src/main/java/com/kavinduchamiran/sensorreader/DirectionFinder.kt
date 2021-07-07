package com.kavinduchamiran.sensorreader

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

interface DirectionFinder {
     var rotationMatrix: FloatArray
         get() = FloatArray(9)
         set(value) = TODO()
    var orientationAngles: FloatArray
        get() = FloatArray(3)
        set(value) = TODO()
    var accelerometerReading: FloatArray
        get() = FloatArray(3)
        set(value) = TODO()
    var magnetometerReading: FloatArray
        get() = FloatArray(3)
        set(value) = TODO()
    var mSensorManager: SensorManager
    var mLight: Sensor
    var mGyroscope: Sensor

    fun startDirectionFinder(context: Context,sensorEventListener: SensorEventListener){
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
}
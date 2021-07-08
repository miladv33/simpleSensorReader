package com.kavinduchamiran.sensorreader

import android.hardware.Sensor
import android.hardware.SensorManager

object SensorObject {
    var rotationMatrix = FloatArray(9)
    var orientationAngles = FloatArray(3)
    var accelerometerReading = FloatArray(3)
    var gyroscopeReading = FloatArray(3)
    var magneReading = FloatArray(3)
    var lightReading = FloatArray(1)
    var mSensorManager: SensorManager? = null
    var mAccelerometer: Sensor? = null
    var mMagnet: Sensor? = null
    var mLight: Sensor? = null
    var mGyroscope: Sensor? = null
}
package com.kavinduchamiran.sensorreader.listeners

interface SensorChangesListener {

    fun onAccelerometerChanged(accelerometerReading: FloatArray)

    fun onGyroscopeChanged(accelerometerReading: FloatArray)

    fun onLightChanged(accelerometerReading: FloatArray)

    fun onMagnetChanged(accelerometerReading: FloatArray)
}
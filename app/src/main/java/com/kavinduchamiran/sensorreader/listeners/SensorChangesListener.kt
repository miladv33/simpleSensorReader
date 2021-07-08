package com.kavinduchamiran.sensorreader.listeners

import com.kavinduchamiran.sensorreader.Direction

interface SensorChangesListener {

    fun onAccelerometerChanged(accelerometerReading: FloatArray)

    fun onGyroscopeChanged(accelerometerReading: FloatArray)

    fun onLightChanged(accelerometerReading: FloatArray)

    fun onMagnetChanged(accelerometerReading: FloatArray)

    fun onUpdateOrientationAngles(degrees:Double,angle:Int,direction: Direction)
}
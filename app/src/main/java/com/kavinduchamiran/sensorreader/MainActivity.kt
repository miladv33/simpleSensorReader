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

    private var resume = false
    override fun startToFindSensor() {
        with(SensorObject) {

            mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            mAccelerometer = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
                    mSensorManager?.registerListener(
                        this@MainActivity,
                        accelerometer,
                        SensorManager.SENSOR_DELAY_NORMAL,
                        SensorManager.SENSOR_DELAY_UI
                    )
                }
            mMagnet = mSensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
                    mSensorManager?.registerListener(
                        this@MainActivity,
                        magneticField,
                        SensorManager.SENSOR_DELAY_NORMAL,
                        SensorManager.SENSOR_DELAY_UI
                    )
                }
            mLight = mSensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
            mGyroscope = mSensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        startToFindSensor()
    }


    override fun onResume() {
        super.onResume()
        with(SensorObject) {
            mSensorManager?.registerListener(this@MainActivity, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            mSensorManager?.registerListener(this@MainActivity, mMagnet, SensorManager.SENSOR_DELAY_NORMAL)
            mSensorManager?.registerListener(this@MainActivity, mLight, SensorManager.SENSOR_DELAY_NORMAL)
            mSensorManager?.registerListener(this@MainActivity, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        SensorObject.mSensorManager?.unregisterListener(this)
    }

    fun resumeReading(view: View) {
        this.resume = true
    }

    fun pauseReading(view: View) {
        this.resume = false
    }


    override fun stopListening(): Boolean {
        return !resume
    }


    override fun onAccelerometerChanged(accelerometerReading: FloatArray) {
        if (stopListening()) return
        runOnUiThread {
            findViewById<TextView>(R.id.acc_X).text = accelerometerReading[0].toString()
            findViewById<TextView>(R.id.acc_Y).text = accelerometerReading[1].toString()
            findViewById<TextView>(R.id.acc_Z).text = accelerometerReading[2].toString()
        }
    }

    override fun onGyroscopeChanged(accelerometerReading: FloatArray) {
        if (stopListening()) return
        runOnUiThread {
            findViewById<TextView>(R.id.gyro_x).text =
                ((Math.toDegrees(accelerometerReading[0].toDouble()) + 360) % 360).toString()
            findViewById<TextView>(R.id.gyro_y).text =
                ((Math.toDegrees(accelerometerReading[1].toDouble()) + 360) % 360).toString()
            findViewById<TextView>(R.id.gyro_z).text =
                ((Math.toDegrees(accelerometerReading[2].toDouble()) + 360) % 360).toString()
        }
    }

    override fun onLightChanged(accelerometerReading: FloatArray) {
        if (stopListening()) return
        runOnUiThread {
            findViewById<TextView>(R.id.light).text = accelerometerReading[0].toString()
        }
    }

    override fun onMagnetChanged(accelerometerReading: FloatArray) {
        if (stopListening()) return
    }

    override fun onUpdateOrientationAngles(degrees: Double, angle: Int, direction: Direction) {
        Log.i("miladTestOrientation", "updateOrientationAngles: degrees: $degrees")
        Log.i("miladTestOrientation", "updateOrientationAngles: angle: $angle")
        Log.i("miladTestOrientation", "updateOrientationAngles: direction: $direction")
    }

}

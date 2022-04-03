package com.dabong.bubble_level

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var event: SensorEvent? = null
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null

    private var calibrateAngleX: Float = 0F
    private var calibrateAngleY: Float = 0F

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        // 실행중 화면꺼짐 OFF
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // 세로모드 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val point = Point()
        getWindowManager().defaultDisplay.getSize(point)
        levelView.init(
            (point.x / 20).toFloat(),
            (point.x / 2 + point.x / 20).toFloat(),
            (point.y / 2 + point.x / 20).toFloat()
        )

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        if (sensor == null) {
            Toast.makeText(this, "No gravity sensor attached on this device", Toast.LENGTH_SHORT)
                .show()
        }
        zeroButton.setOnClickListener {
            if (!holdButton.isSelected) {
                val vectorR =
                    sqrt(
                        event!!.values[0].pow(2) + event!!.values[1].pow(2) + event!!.values[2].pow(
                            2
                        )
                    )
                calibrateAngleX = (90 - acos(event!!.values[0] / vectorR) * 180 / PI).toFloat()
                calibrateAngleY = (90 - acos(event!!.values[1] / vectorR) * 180 / PI).toFloat()
            }
        }

        resetButton.setOnClickListener {
            if (!holdButton.isSelected) {
                calibrateAngleX = 0F
                calibrateAngleY = 0F
            }
        }

        holdButton.setOnClickListener {
            holdButton.isSelected = !holdButton.isSelected
        }


    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(holdButton.isSelected){
            return
        }
        if (event?.sensor == sensor) {
            this.event = event
            this.event?.let {
                val vectorR = sqrt(
                    it.values[0].pow(2)
                            + it.values[1].pow(2) + it.values[2].pow(2)
                )
                val xAngle =
                    (90 - acos(it.values[0] / vectorR) * 180 / PI).toFloat() - calibrateAngleX
                val yAngle =
                    (90 - acos(it.values[1] / vectorR) * 180 / PI).toFloat() - calibrateAngleY

                levelView.onSensorEvent(xAngle = xAngle, yAngle = yAngle)

            }

        }
    }
}
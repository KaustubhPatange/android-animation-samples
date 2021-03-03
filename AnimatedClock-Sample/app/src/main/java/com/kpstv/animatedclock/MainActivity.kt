package com.kpstv.animatedclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startClockAnimation()
    }

    private fun startClockAnimation() {
        val seconds0 = findViewById<NumberColumn>(R.id.seconds0)
        val seconds1 = findViewById<NumberColumn>(R.id.seconds1)
        val minutes0 = findViewById<NumberColumn>(R.id.minutes0)
        val minutes1 = findViewById<NumberColumn>(R.id.minutes1)
        val hours0 = findViewById<NumberColumn>(R.id.hours0)
        val hours1 = findViewById<NumberColumn>(R.id.hours1)

        seconds0.postDelayed(object : Runnable {
            override fun run() {
                val calender = Calendar.getInstance()
                val second = calender[Calendar.SECOND]
                val minute = calender[Calendar.MINUTE]
                val hour = calender[Calendar.HOUR_OF_DAY]

                hours1.animateToDigit(hour / 10)
                hours0.animateToDigit(hour % 10)

                minutes1.animateToDigit(minute / 10)
                minutes0.animateToDigit(minute % 10)

                seconds1.animateToDigit(second / 10)
                seconds0.animateToDigit(second % 10)

                seconds0.postDelayed(this, 1000)
            }
        }, 150)
    }
}
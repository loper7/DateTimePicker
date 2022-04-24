package com.loper7.datepicker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.loper7.date_time_picker.utils.lunar.Lunar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 *@Author loper7
 *@Date 2021/9/15 16:20
 *@Description
 **/
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        picker.setTextBold(false)
//        picker.setSelectedTextBold(false)
        picker.setOnDateTimeChangedListener{
            var calendar  = Calendar.getInstance()
            calendar.timeInMillis = it
        }

        btnCardDialog.setOnClickListener {
            startActivity(Intent(this, DatePickerExampleActivity::class.java))
        }
        btnCustomLayout.setOnClickListener {
            startActivity(Intent(this, CustomLayoutActivity::class.java))
        }
        btnGlobalization.setOnClickListener {
            startActivity(Intent(this, GlobalizationActivity::class.java))
        }

        btnWeekDialog.setOnClickListener {
            startActivity(Intent(this, WeekPickerExampleActivity::class.java))
        }
    }

}



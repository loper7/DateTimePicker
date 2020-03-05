package com.loper7.datepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.loper7.layout.DateTimePicker
import com.loper7.layout.dialog.DateTimePickerDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOpen.setOnClickListener {
            DateTimePickerDialog(this)
                .setTitle("选择日期及时间")
                .showBackNow(true)
                .setDisplayType(DateTimePicker.YEAR, DateTimePicker.MONTH, DateTimePicker.DAY)
                .setMinTime(System.currentTimeMillis())
                .show()

        }
    }
}

package com.loper7.datepicker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.loper7.date_time_picker.DateTimeConfig
import kotlinx.android.synthetic.main.activity_globalization.*

/**
 *
 * @CreateDate:     2021/1/12 11:10
 * @Description:
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
class GlobalizationActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_globalization)

        picker.setGlobal(DateTimeConfig.GLOBAL_US)
        picker1.setGlobal(DateTimeConfig.GLOBAL_CHINA)
        picker2.setGlobal(DateTimeConfig.GLOBAL_LOCAL)
        picker2.setDisplayType(intArrayOf(DateTimeConfig.YEAR,DateTimeConfig.MONTH,DateTimeConfig.DAY))

    }
}
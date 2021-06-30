package com.loper7.datepicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_custom_picker.*

/**
 *
 * @CreateDate:     2021/1/12 11:10
 * @Description:
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
class CustomLayoutActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_picker)
        picker.setLayout(R.layout.layout_date_picker_segmentation)
//        picker.bindController(DateTimeCustomController())

    }
}
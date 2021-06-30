package com.loper7.date_time_picker.common

import com.loper7.date_time_picker.number_picker.NumberPicker

/**
 *
 * @CreateDate:     2021/6/18 9:35
 * @Description:    控制器基类
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
abstract class BaseDateTimeController : DateTimeInterface {

    abstract fun bindPicker(type: Int, picker: NumberPicker?): BaseDateTimeController

    abstract fun build(): BaseDateTimeController
}
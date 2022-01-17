package com.loper7.date_time_picker.controller

import com.loper7.date_time_picker.ext.getMaxDayInMonth
import com.loper7.date_time_picker.number_picker.NumberPicker
import java.lang.Exception
import java.util.*

/**
 *
 * @CreateDate:     2021/6/18 9:35
 * @Description:    控制器基类
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
abstract class BaseDateTimeController : DateTimeInterface {

    abstract fun bindPicker(type: Int, picker: NumberPicker?): BaseDateTimeController

    abstract fun bindGlobal(global: Int): BaseDateTimeController

    abstract fun build(): BaseDateTimeController

    /**
     * 获取某月最大天数
     */
    protected fun getMaxDayInMonth(year: Int?, month: Int?): Int {
        if (year == null || month == null)
            return 0
        if (year <= 0 || month < 0)
            return 0
        try {
            val calendar: Calendar = Calendar.getInstance()
            calendar.clear()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            return calendar.getMaxDayInMonth()
        } catch (e: Exception) {
            return 0
        }

    }

}
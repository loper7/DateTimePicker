package com.loper7.date_time_picker

import android.util.Log
import com.loper7.date_time_picker.number_picker.NumberPicker
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * @CreateDate:     2020/9/11 13:41
 * @Description:    java类作用描述
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
object DateTimeConfig {
    const val YEAR = 0
    const val MONTH = 1
    const val DAY = 2
    const val HOUR = 3
    const val MIN = 4
    const val SECOND = 5

    const val GLOBAL_LOCAL = 0
    const val GLOBAL_CHINA = 1
    const val GLOBAL_US = 2

    const val DATE_DEFAULT = 0 //公历
    const val DATE_LUNAR = 1 //农历

    //数字格式化，<10的数字前自动加0
    val formatter =
        NumberPicker.Formatter { value: Int ->
            var str = value.toString()
            if (value < 10) {
                str = "0$str"
            }
            str
        }
    //国际化格月份格式化
    val globalizationMonthFormatter =
        NumberPicker.Formatter { value: Int ->
            var str = value.toString()
            if (value in 1..12)
                str = DateFormatSymbols(Locale.US).months.toList()[value - 1]
            str
        }



    //国际化格月份格式化-缩写
    val globalMonthFormatter =
        NumberPicker.Formatter { value: Int ->
            var str = value.toString()
            if (value in 1..12) {
                var month = DateFormatSymbols(Locale.US).months.toList()[value - 1]
                str = if (month.length > 3)
                    month.substring(0, 3)
                else
                    month
            }
            str
        }


    private fun isChina(): Boolean {
        return Locale.getDefault().language.contains("zh", true)
    }

    fun showChina(global: Int): Boolean {
        return global == GLOBAL_CHINA || (global == GLOBAL_LOCAL && isChina())
    }


}
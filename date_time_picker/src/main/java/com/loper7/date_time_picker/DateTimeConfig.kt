package com.loper7.date_time_picker

import com.loper7.date_time_picker.number_picker.NumberPicker

/**
 *
 * @CreateDate:     2020/9/11 13:41
 * @Description:    java类作用描述
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
object DateTimeConfig {
    var YEAR = 0
    var MONTH = 1
    var DAY = 2
    var HOUR = 3
    var MIN = 4
    var SECOND = 5

    //数字格式化，<10的数字前自动加0
    val formatter =
        NumberPicker.Formatter { value: Int ->
            var str = value.toString()
            if (value < 10) {
                str = "0$str"
            }
            str
        }
}
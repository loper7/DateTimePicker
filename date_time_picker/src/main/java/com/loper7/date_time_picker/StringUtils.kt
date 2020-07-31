package com.loper7.date_time_picker

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 字符串操作工具包
 *
 */
object StringUtils {
    fun conversionTime(time: String?, format: String?): Long {
        var format = format
        if (TextUtils.isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        try {
            return sdf.parse(time).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * @param time
     * @return yy-MM-dd HH:mm格式时间
     */
    fun conversionTime(time: Long, format: String?): String {
        var format = format
        if (TextUtils.isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(time))
    }

    /**
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     *
     * @return
     */
    fun getWeek(time: Long): String {
        var Week = ""
        val c = Calendar.getInstance()
        c.time = Date(time)
        val wek = c[Calendar.DAY_OF_WEEK]
        if (wek == 1) {
            Week += "周日"
        }
        if (wek == 2) {
            Week += "周一"
        }
        if (wek == 3) {
            Week += "周二"
        }
        if (wek == 4) {
            Week += "周三"
        }
        if (wek == 5) {
            Week += "周四"
        }
        if (wek == 6) {
            Week += "周五"
        }
        if (wek == 7) {
            Week += "周六"
        }
        return Week
    }
}
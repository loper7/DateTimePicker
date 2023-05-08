package com.loper7.date_time_picker.utils.lunar

import android.util.Log
import com.loper7.date_time_picker.ext.getMaxDayAtYear
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_DAY_NAMES
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_DZ
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_MONTH_NAMES
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_TABLE
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_TG
import com.loper7.date_time_picker.utils.lunar.LunarConstants.MIN_LUNAR_YEAR
import java.util.*

open class Lunar(
    var year: Int,
    var month: Int,
    var isLeapMonth: Boolean,
    var day: Int,
    var hour: Int,
    var minute: Int,
    var seconds: Int

) {


    companion object {


        fun getInstance(timeInMillis: Long): Lunar? {
            var calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis
            return getInstance(calendar)
        }

        fun getInstance(calendar: Calendar = Calendar.getInstance()): Lunar? {

            //传入的时间超出了计算范围
            if (!hasLunarInfo(calendar)) return null

            var lunarYear: Int = calendar[Calendar.YEAR]
            var lunarMonth = 0
            var lunarDay = 0
            val lunarHour = calendar[Calendar.HOUR_OF_DAY]
            val lunarLeapMonth: Int
            var isLeap = false

            var doffset = calendar[Calendar.DAY_OF_YEAR] - 1
            var hexvalue = LUNAR_TABLE[lunarYear - MIN_LUNAR_YEAR]
            //农历正月的偏移
            var loffset = hexvalue and 0xFF
            //如果当前离1月1号的天数比正月离元月的天数还小那么则应该是上一个农历年
            if (loffset > doffset) {
                lunarYear--
                doffset += GregorianCalendar().getMaxDayAtYear(lunarYear)
                hexvalue = LUNAR_TABLE[lunarYear - MIN_LUNAR_YEAR]
                loffset = hexvalue and 0xFF
            }
            var days = doffset - loffset + 1
            //农历闰月
            lunarLeapMonth = hexvalue shr 8 and 0xF
            val len = if (lunarLeapMonth > 0) 13 else 12
            //开始循环取
            var v = 0
            var cd = 0
            for (i in 0 until len) {
                v = if (lunarLeapMonth in 1..i) {
                    if (i == lunarLeapMonth) {
                        hexvalue shr 12 and 0x1
                    } else {
                        hexvalue shr 24 - i + 1 and 0x1
                    }
                } else {
                    hexvalue shr 24 - i and 0x1
                }
                cd = 29 + v
                days -= cd
                if (days <= 0) {
                    lunarDay = days + cd
                    lunarMonth = i + 1
                    if (lunarLeapMonth in 1..i) {
                        isLeap = i == lunarLeapMonth
                        --lunarMonth
                    }
                    break
                }
            }

            return Lunar(
                lunarYear,
                lunarMonth,
                isLeap,
                lunarDay,
                lunarHour,
                calendar[Calendar.MINUTE],
                calendar[Calendar.SECOND]
            )
        }

        /**
         * 是否有农历信息
         *
         * @param calendar
         * @return
         */
        fun hasLunarInfo(calendar: Calendar): Boolean {
            return try {
                val syear = calendar[Calendar.YEAR]
                val dayoffset = calendar[Calendar.DAY_OF_YEAR] - 1
                val lindex = syear - MIN_LUNAR_YEAR
                if (lindex < 0 || lindex >= LUNAR_TABLE.size) {
                    return false
                }
                var lyear = syear
                val hexValue = LUNAR_TABLE[lindex]
                val ldayoffset = hexValue and 0xFF
                if (ldayoffset > dayoffset) {
                    lyear--
                }
                lyear >= MIN_LUNAR_YEAR
            } catch (e: Throwable) {
                e.printStackTrace()
                false
            }
        }
    }


    /**
     * 获取农历干支纪年
     *
     * @return
     */
    val yearName: String
        get() {
            var tg = LUNAR_TG[(year - 4) % 10]
            var dz = LUNAR_DZ[(year - 4) % 12]
            return "${tg}${dz}年"
        }

    /**
     * 获取农历月名称
     *
     * @return
     */
    val monthName: String
        get() = (if (isLeapMonth) "闰" else "") + LUNAR_MONTH_NAMES[month - 1]

    /**
     * 获取农历日名称
     *
     * @return
     */
    val dayName: String
        get() = LUNAR_DAY_NAMES[day - 1]

    /**
     * 获取农历时辰名称
     */
    val hourName: String
        get() = "${LUNAR_DZ[(hour + 1) / 2 % 12]}时"


    /**
     * 获取农历年月中最多的天数
     */
    fun getMaxDayInMonth(): Int {
        val index: Int = year - MIN_LUNAR_YEAR
        val hexValue: Int = LUNAR_TABLE[index]
        return if (isLeapMonth) {
            (hexValue shr 12 and 0x1) + 29
        } else (hexValue shr 24 - month + 1 and 0x1) + 29
    }


    override fun equals(o: Any?): Boolean {
        if (o == null || o !is Lunar) return false
        return o.year == year && o.month == month && o.isLeapMonth == isLeapMonth
    }

    override fun toString(): String {
        var map = mutableMapOf<String, Any>()
        map["year"] = year
        map["month"] = month
        map["day"] = day
        map["hour"] = hour
        map["minute"] = minute
        map["seconds"] = seconds
        map["isLeapMonth"] = isLeapMonth
        map["yearName"] = yearName
        map["monthName"] = monthName
        map["dayName"] = dayName
        map["hourName"] = hourName
        return map.toString()

    }


}
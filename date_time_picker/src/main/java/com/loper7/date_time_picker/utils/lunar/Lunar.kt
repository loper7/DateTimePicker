package com.loper7.date_time_picker.utils.lunar

import android.util.Log
import com.loper7.date_time_picker.ext.getMaxDayAtYear
import com.loper7.date_time_picker.utils.StringUtils
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_DAY_NAMES
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_DZ
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_MONTH_NAMES
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_TABLE
import com.loper7.date_time_picker.utils.lunar.LunarConstants.LUNAR_TG
import com.loper7.date_time_picker.utils.lunar.LunarConstants.MIN_LUNAR_YEAR
import com.loper7.date_time_picker.utils.lunar.LunarConstants.NOT_FOUND_LUNAR
import java.time.Year
import java.util.*
import kotlin.math.min

class Lunar(
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

        fun getInstance(
            lunarYear: Int,
            lunarMonth: Int,
            isLeapMonth: Boolean,
            lunarDay: Int,
            hour: Int = 0,
            minute: Int = 0,
            seconds: Int = 0
        ): Lunar {
            return Lunar(lunarYear, lunarMonth, isLeapMonth, lunarDay, hour, minute, seconds)
        }


        /**
         * 获取month名称
         * @param year 年
         * @param monthIndex 下标
         */
        fun getMonthName(year: Int, monthIndex: Int): String {
            if (monthIndex == 0 || year == 0)
                return ""
            var month = 1
            var isLeapMonth = false
            var leapMonth = getLeapMonth(year)
            month = if (leapMonth <= 0 || monthIndex <= leapMonth) {
                monthIndex
            } else {
                isLeapMonth = monthIndex - 1 == leapMonth
                monthIndex - 1
            }
            if (month > LUNAR_MONTH_NAMES.size)
                month = LUNAR_MONTH_NAMES.size
            return (if (isLeapMonth) "闰" else "") + LUNAR_MONTH_NAMES[month - 1]
        }

        /**
         * 根据农历年获取闰月
         *
         * @param lunarYear
         * @return
         */
        fun getLeapMonth(lunarYear: Int): Int {
            if (!hasLunarInfo(lunarYear)) return NOT_FOUND_LUNAR
            val index: Int = lunarYear - MIN_LUNAR_YEAR
            val hexValue: Int = LUNAR_TABLE[index]
            return hexValue shr 8 and 0xF
        }

        /**
         * 获取农历年月中最多的天数
         */
        open fun getMaxDayInMonth(lunarYear: Int, lunarMonth: Int, isLeap: Boolean): Int {
            if (!hasLunarInfo(lunarYear)) return NOT_FOUND_LUNAR

            var isLeap = isLeap
            val index: Int = lunarYear - MIN_LUNAR_YEAR
            val hexValue: Int = LUNAR_TABLE[index]
            isLeap = (getLeapMonth(lunarYear) == lunarMonth) and isLeap
            return if (isLeap) {
                (hexValue shr 12 and 0x1) + 29
            } else (hexValue shr 24 - lunarMonth + 1 and 0x1) + 29
        }

        /**
         * 是否有农历信息
         *
         * @param calendar
         * @return
         */
        @Synchronized
        fun hasLunarInfo(calendar: Calendar): Boolean {
            val syear = calendar[Calendar.YEAR]
            val dayoffset = calendar[Calendar.DAY_OF_YEAR] - 1
            val lindex = syear - MIN_LUNAR_YEAR
            if (lindex < 0 || lindex > LUNAR_TABLE.size) {
                return false
            }
            var lyear = syear
            val hexValue = LUNAR_TABLE[lindex]
            val ldayoffset = hexValue and 0xFF
            if (ldayoffset > dayoffset) {
                lyear--
            }
            return lyear >= MIN_LUNAR_YEAR
        }

        /**
         * 是否有农历信息
         * @param lunarYear 农历年
         */
        fun hasLunarInfo(lunarYear: Int?): Boolean {
            if (lunarYear == null)
                return false
            val index: Int = lunarYear - MIN_LUNAR_YEAR
            return !(index > LUNAR_TABLE.size || index < 0)
        }


    }

    /**
     * 获取农历年中一共有多少个月
     *
     * @param lunarYear
     * @return
     */
    fun getTotalMonth(): Int {
        return if (getLeapMonth(year) > 0) 13 else 12
    }


    /**
     * 将农历信息转换为阳历
     * @param lunar 农历
     * @return Calendar 阳历
     */
    fun toCalendar(): Calendar? {

        val index: Int = year - MIN_LUNAR_YEAR
        val hexValue: Int = LUNAR_TABLE[index]
        val leapMonth = hexValue shr 8 and 0xF
        val loffset = hexValue and 0xFF
        val end =
            month + (if (isLeapMonth || month > leapMonth && leapMonth > 0) 1 else 0) - 1
        var dayOfYear = 0
        var v = 0
        for (i in 0 until end) {
            v = if (leapMonth in 1..i) {
                if (i == leapMonth) {
                    hexValue shr 12 and 0x1
                } else {
                    hexValue shr 24 - i + 1 and 0x1
                }
            } else {
                hexValue shr 24 - i and 0x1
            }
            dayOfYear += 29 + v
        }
        dayOfYear += day + loffset
        var solarYear = year
        val thisYearDays: Int = GregorianCalendar().getMaxDayAtYear(solarYear)
        if (dayOfYear > thisYearDays) {
            dayOfYear -= thisYearDays
            solarYear++
        }
        var calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(Calendar.YEAR, solarYear)
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, seconds)
        return calendar
    }

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


    /**
     * 通过下标变更月份
     */
    fun updateMonth(monthIndex: Int) {
        var leapMonth = getLeapMonth(year)
        month = if (leapMonth <= 0 || monthIndex <= leapMonth)
            monthIndex
        else
            monthIndex - 1
        isLeapMonth = month == leapMonth
    }


    /**
     *获取月份下标  从1开始
     */
    val monthIndex: Int
        get() {
            var leapMonth = getLeapMonth(year)
            if (leapMonth <= 0)
                return month
            return if (month < leapMonth)
                month
            else if (month == leapMonth)
                if (isLeapMonth) month + 1 else month
            else
                month + 1
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
     *  与传入农历是否为同一年
     *  @param calendar
     */
    fun isSameYear(lunar: Lunar): Boolean {
        return year == lunar.year
    }

    /**
     *  与传入农历是否为同一月
     *  @param calendar
     */
    fun isSameMonth(lunar: Lunar): Boolean {
        return isSameYear(lunar) && month == lunar.month && isLeapMonth == lunar.isLeapMonth
    }

    /**
     *  与传入农历是否为同一天
     *  @param calendar
     */
    fun isSameDay(lunar: Lunar): Boolean {
        return isSameMonth(lunar) && day == lunar.day
    }

    /**
     *  与传入农历是否为同一时
     *  @param calendar
     */
    fun isSameHour(lunar: Lunar): Boolean {
        return isSameDay(lunar) && hour == lunar.hour
    }

    /**
     *  与传入农历是否为同一分
     *  @param calendar
     */
    fun isSameMinute(lunar: Lunar): Boolean {
        return isSameHour(lunar) && minute == lunar.minute
    }

    /**
     *  与传入农历是否为同一秒
     *  @param calendar
     */
    fun isSameSecond(lunar: Lunar): Boolean {
        return isSameMinute(lunar) && seconds == lunar.seconds
    }

    /**
     * 当前农历是否在传入农历之后
     */
    fun isLast(lunar: Lunar): Boolean {
        return if (isSameMonth(lunar))
            StringUtils.conversionTime("$year-$month-$day $hour:$minute:$seconds") >
                    StringUtils.conversionTime("${lunar.year}-${lunar.month}-${lunar.day} ${lunar.hour}:${lunar.minute}:${lunar.seconds}")
        else {
            if (isSameYear(lunar)) {
                if (month == lunar.month)
                    isLeapMonth
                else
                    month > lunar.month
            } else {
                year > lunar.year
            }
        }
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
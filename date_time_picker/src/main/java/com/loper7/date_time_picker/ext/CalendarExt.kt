package com.loper7.date_time_picker.ext

import java.util.*

/**
 * 获取一年中所有的周
 * @param year 1900-9999 default:now
 * @return MutableList<MutableList<Long>>
 */
fun Calendar.getWeeksOfYear(
    year: Int = Calendar.getInstance().get(Calendar.YEAR)
): MutableList<MutableList<Long>> {
    if (year < 1900 || year > 9999)
        throw NullPointerException("The year must be within 1900-9999")

    firstDayOfWeek = Calendar.MONDAY
    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    minimalDaysInFirstWeek = 7
    set(Calendar.YEAR, year)

    var weeksData = mutableListOf<MutableList<Long>>()
    for (i in 1..getMaxWeekOfYear(year)) {
        var daysData = getDaysOfWeek(year, i)
        weeksData.add(daysData)
    }
    return weeksData
}


/**
 * 获取一年中的最后一周数字
 * @param year 1900-9999
 * @return week 52 or 53
 */
fun Calendar.getMaxWeekOfYear(year: Int = Calendar.getInstance().get(Calendar.YEAR)): Int {
    set(year, Calendar.DECEMBER, 31, 0, 0, 0)
    return getWeekOfYear(time)
}

/**
 * 获取 date 所在年的周数
 * @param date 时间
 * @return int
 */
fun Calendar.getWeekOfYear(date: Date): Int {
    firstDayOfWeek = Calendar.MONDAY
    minimalDaysInFirstWeek = 7
    time = date

    return get(Calendar.WEEK_OF_YEAR)
}

/**
 * 获取某年某周的日期时间戳集合[第一天-最后一天]
 * @param year 1900-9999
 * @param week 1-52/53
 * @return MutableList<Long>
 */
fun Calendar.getDaysOfWeek(
    year: Int = Calendar.getInstance().get(Calendar.YEAR),
    week: Int
): MutableList<Long> {
    if (year < 1900 || year > 9999)
        throw NullPointerException("The year must be within 1900-9999")

    firstDayOfWeek = Calendar.MONDAY
    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    minimalDaysInFirstWeek = 7
    set(Calendar.YEAR, year)
    set(Calendar.WEEK_OF_YEAR, week)

    var weekData = mutableListOf<Long>()
    for (i in 0 until 7) {
        weekData.add(timeInMillis + (86400000 * i))
    }
    return weekData
}
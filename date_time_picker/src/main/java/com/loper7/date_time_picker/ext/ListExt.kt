package com.loper7.date_time_picker.ext

import android.util.Log
import com.loper7.date_time_picker.utils.StringUtils
import java.util.*

/**
 * 将时间戳集合格式化为指定日期格式的集合
 * @return MutableList<String> [2021-09-09,2021--09-10,...]
 */
internal fun MutableList<Long>.toFormatList(format: String = "yyyy-MM-dd"): MutableList<String> {
    var formatList = mutableListOf<String>()
    for (i in this) {
        formatList.add(StringUtils.conversionTime(i, format))
    }
    return formatList
}

/**
 * 时间集合内是否包含对应某天
 */
internal fun MutableList<Long>.contain(date: Long): Boolean {
    for (i in this) {
        if (StringUtils.conversionTime(i, "yyyyMMdd") == StringUtils.conversionTime(
                date,
                "yyyyMMdd"
            )
        ) {
            return true
        }
    }
    return false
}

/**
 * 获取对应时间所在周的下标
 */
internal fun MutableList<MutableList<Long>>.index(date: Long?): Int {
    if (this.isNullOrEmpty() || date == null)
        return -1
    var _date = date
    if (_date == 0L)
        _date = Calendar.getInstance().timeInMillis
    for (i in 0 until size) {
        if (this[i].contain(_date))
            return i
    }
    return 0
}
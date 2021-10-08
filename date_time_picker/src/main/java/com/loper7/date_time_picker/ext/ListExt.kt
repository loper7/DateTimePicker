package com.loper7.date_time_picker.ext

import com.loper7.date_time_picker.utils.StringUtils

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
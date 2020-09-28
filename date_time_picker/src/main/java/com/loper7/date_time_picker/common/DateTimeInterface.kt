package com.loper7.date_time_picker.common

import com.loper7.date_time_picker.DateTimeConfig

/**
 *
 * @CreateDate:     2020/9/11 16:55
 * @Description:    java类作用描述
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
interface DateTimeInterface {

    /**
     * 设置默认时间戳
     *
     * @param time
     */
    fun setDefaultMillisecond(time:Long)

    /**
     * 设置最小选择时间
     *
     * @param time
     */
    fun setMinMillisecond(time: Long)

    /**
     * 设置最大选择时间
     *
     * @param time
     */
    fun setMaxMillisecond(time: Long)

    /**
     * 设置是否Picker循环滚动
     * @param types 需要设置的Picker类型（DateTimeConfig-> YEAR,MONTH,DAY,HOUR,MIN,SECOND）
     * @param wrapSelector 是否循环滚动
     */
    fun setWrapSelectorWheel(types: MutableList<Int>?=null, wrapSelector: Boolean = true)

    fun setOnDateTimeChangedListener(callback: ((Long) -> Unit)? = null)
}
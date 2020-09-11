package com.loper7.date_time_picker.common

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

    fun setOnDateTimeChangedListener(callback: ((Long) -> Unit)? = null)
}
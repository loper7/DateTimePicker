package com.loper7.date_time_picker.common

import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.DateTimeConfig.DAY
import com.loper7.date_time_picker.DateTimeConfig.HOUR
import com.loper7.date_time_picker.DateTimeConfig.MIN
import com.loper7.date_time_picker.DateTimeConfig.MONTH
import com.loper7.date_time_picker.DateTimeConfig.SECOND
import com.loper7.date_time_picker.DateTimeConfig.YEAR
import com.loper7.date_time_picker.number_picker.NumberPicker
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

/**
 *
 * @CreateDate:     2020/9/11 13:36
 * @Description:    日期/时间逻辑控制器
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
class DateTimeController : DateTimeInterface {
    private var mYearSpinner: NumberPicker? = null
    private var mMonthSpinner: NumberPicker? = null
    private var mDaySpinner: NumberPicker? = null
    private var mHourSpinner: NumberPicker? = null
    private var mMinuteSpinner: NumberPicker? = null
    private var mSecondSpinner: NumberPicker? = null

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0
    private var mSecond = 0

    private var minMillisecond = 0L
    private var minMonth = 1
    private var minDay = 1
    private var minHour = 0
    private var minMinute = 0
    private var minSecond = 0

    private var maxMillisecond = 0L
    private var maxMonth = 12
    private var maxDay = 31
    private var maxHour = 23
    private var maxMinute = 59
    private var maxSecond = 59

    private var millisecond: Long = 0
    private var mOnDateTimeChangedListener: ((Long) -> Unit)? = null


    companion object {
        fun create(): DateTimeController {
            return DateTimeController()
        }
    }


    fun bindPicker(type: Int, picker: NumberPicker?): DateTimeController {
        when (type) {
            YEAR -> mYearSpinner = picker
            MONTH -> mMonthSpinner = picker
            DAY -> mDaySpinner = picker
            HOUR -> mHourSpinner = picker
            MIN -> mMinuteSpinner = picker
            SECOND -> mSecondSpinner = picker
        }
        return this
    }

    fun build(): DateTimeController {
        val mDate = Calendar.getInstance()
        mYear = mDate.get(Calendar.YEAR)
        mMonth = mDate.get(Calendar.MONTH) + 1
        mDay = mDate.get(Calendar.DAY_OF_MONTH)
        mHour = mDate.get(Calendar.HOUR_OF_DAY)
        mMinute = mDate.get(Calendar.MINUTE)
        mSecond = mDate.get(Calendar.SECOND)
        millisecond = mDate.timeInMillis

        mYearSpinner?.run {
            maxValue = mYear + 100
            minValue = 1800
            value = mYear
            isFocusable = true
            isFocusableInTouchMode = true
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS //设置NumberPicker不可编辑
            setOnValueChangedListener(mOnYearChangedListener) //注册NumberPicker值变化时的监听事件
        }


        mMonthSpinner?.run {
            maxValue = 12
            minValue = 1
            value = mMonth
            isFocusable = true
            isFocusableInTouchMode = true
            setFormatter(DateTimeConfig.formatter) //格式化显示数字，个位数前添加0
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(mOnMonthChangedListener)
        }

        mDaySpinner?.run {
            leapMonth() //判断是否闰年，从而设置2月份的天数
            isFocusable = true
            isFocusableInTouchMode = true
            value = mDay
            setFormatter(DateTimeConfig.formatter)
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(mOnDayChangedListener)
        }

        mHourSpinner?.run {
            maxValue = 23
            minValue = 0
            isFocusable = true
            isFocusableInTouchMode = true
            value = mHour
            setFormatter(DateTimeConfig.formatter)
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(mOnHourChangedListener)
        }

        mMinuteSpinner?.run {
            maxValue = 59
            minValue = 0
            isFocusable = true
            isFocusableInTouchMode = true
            value = mMinute
            setFormatter(DateTimeConfig.formatter)
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(mOnMinuteChangedListener)
        }

        mSecondSpinner?.run {
            maxValue = 59
            minValue = 0
            isFocusable = true
            isFocusableInTouchMode = true
            value = mMinute
            setFormatter(DateTimeConfig.formatter)
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(mOnSecondChangedListener)
        }
        return this
    }



    private val mOnYearChangedListener = NumberPicker.OnValueChangeListener { _, _, _, _ ->
        mYear = mYearSpinner!!.value
        leapMonth()
        limitMaxAndMin()
        onDateTimeChanged()
    }

    private val mOnMonthChangedListener = NumberPicker.OnValueChangeListener { _, _, _, _ ->
        mMonth = mMonthSpinner!!.value
        leapMonth()
        limitMaxAndMin()
        onDateTimeChanged()
    }

    private val mOnDayChangedListener = NumberPicker.OnValueChangeListener { _, _, _, _ ->
        mDay = mDaySpinner!!.value
        limitMaxAndMin()
        onDateTimeChanged()
    }

    private val mOnHourChangedListener = NumberPicker.OnValueChangeListener { _, _, _, _ ->
        mHour = mHourSpinner!!.value
        limitMaxAndMin()
        onDateTimeChanged()
    }

    private val mOnMinuteChangedListener = NumberPicker.OnValueChangeListener { _, _, _, _ ->
        mMinute = mMinuteSpinner!!.value
        limitMaxAndMin()
        onDateTimeChanged()
    }

    private val mOnSecondChangedListener = NumberPicker.OnValueChangeListener { _, _, _, _ ->
        mSecond = mSecondSpinner!!.value
        onDateTimeChanged()
    }

    /**
     * 日期发生变化
     */
    private fun onDateTimeChanged() {

        if (mHourSpinner == null) mHour = 0
        if (mMinuteSpinner == null) mMinute = 0
        if (mSecondSpinner == null) mSecond = 0

        millisecond = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime.of(mYear, mMonth, mDay, mHour, mMinute, mSecond)
                .toInstant(ZoneOffset.ofHours(8)).toEpochMilli()
        } else {
            val mCalendar = Calendar.getInstance()
            mCalendar.set(mYear, mMonth, mDay, mHour, mMinute, mSecond)
            mCalendar.timeInMillis
        }


        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener?.invoke(millisecond)
        }
    }

    /**
     * 判定闰月
     */
    private fun leapMonth() {
        if (mMonth == 2) {
            if (isLeapYear(mYear)) {
                if (mDaySpinner?.maxValue != 29) {
                    mDaySpinner?.displayedValues = null
                    mDaySpinner?.minValue = 1
                    mDaySpinner?.maxValue = 29
                }
            } else {
                if (mDaySpinner?.maxValue != 28) {
                    mDaySpinner?.displayedValues = null
                    mDaySpinner?.minValue = 1
                    mDaySpinner?.maxValue = 28
                }
            }
        } else {
            when (mMonth) {
                4, 6, 9, 11 -> if (mDaySpinner?.maxValue != 30) {
                    mDaySpinner?.displayedValues = null
                    mDaySpinner?.minValue = 1
                    mDaySpinner?.maxValue = 30
                }
                else -> if (mDaySpinner?.maxValue != 31) {
                    mDaySpinner?.displayedValues = null
                    mDaySpinner?.minValue = 1
                    mDaySpinner?.maxValue = 31
                }
            }
        }
        if (mYear == mYearSpinner?.minValue && mMonth == mMonthSpinner?.minValue) {
            mDaySpinner?.minValue = minDay
        }
    }

    /**
     * 当前年2月有多少天
     *
     * @return
     */
    private fun leapMonth2days(year: Int): Int {
        return if (mMonth == 2) {
            if (isLeapYear(year)) {
                29
            } else {
                28
            }
        } else {
            when (mMonth) {
                4, 6, 9, 11 -> 30
                else -> 31
            }
        }
    }

    /**
     * 设置允许选择的区间
     */
    private fun limitMaxAndMin() {
        //设置月份最小值
        mMonthSpinner?.minValue = if (mYear == mYearSpinner?.minValue) minMonth
        else 1

        //设置月份最大值
        mMonthSpinner?.maxValue = if (mYear == mYearSpinner?.maxValue) maxMonth
        else 12


        /** */ //设置天最小值
        mDaySpinner?.minValue =
            if (mYear == mYearSpinner?.minValue && mMonth == mMonthSpinner?.minValue) minDay
            else 1

        //设置天最大值
        mDaySpinner?.maxValue =
            if (mYear == mYearSpinner?.maxValue && mMonth == mMonthSpinner?.maxValue) maxDay
            else leapMonth2days(mYear)

        mDay = mDaySpinner!!.value

        /** */ //设置小时最小值
        mHourSpinner?.minValue =
            if (mYear == mYearSpinner?.minValue && mMonth == mMonthSpinner?.minValue && mDay == mDaySpinner?.minValue) minHour
            else 0

        //设置小时最大值
        mHourSpinner?.maxValue =
            if (mYear == mYearSpinner?.maxValue && mMonth == mMonthSpinner?.maxValue && mDay == mDaySpinner?.maxValue) maxHour
            else 23

        /** */ //设置分钟最小值
        mMinuteSpinner?.minValue =
            if (mYear == mYearSpinner?.minValue && mMonth == mMonthSpinner?.minValue && mDay == mDaySpinner?.minValue && mHour == mHourSpinner?.minValue) minMinute
            else 0

        //设置分钟最大值
        mMinuteSpinner?.maxValue =
            if (mYear == mYearSpinner?.maxValue && mMonth == mMonthSpinner?.maxValue && mDay == mDaySpinner?.maxValue && mHour == mHourSpinner?.maxValue) maxMinute
            else 59

        /** */ //设置秒最小值
        mSecondSpinner?.minValue =
            if (mYear == mYearSpinner?.minValue && mMonth == mMonthSpinner?.minValue && mDay == mDaySpinner?.minValue && mHour == mHourSpinner?.minValue && mMinute == mMinuteSpinner?.minValue) minSecond
            else 0

        //设置秒最大值
        mSecondSpinner?.maxValue =
            if (mYear == mYearSpinner?.maxValue && mMonth == mMonthSpinner?.maxValue && mDay == mDaySpinner?.maxValue && mHour == mHourSpinner?.maxValue && mMinute == mMinuteSpinner?.maxValue) maxSecond
            else 59
    }

    /**
     * 是否为闰年
     *
     * @param year 当前年份
     * @return true or false
     */
    private fun isLeapYear(year: Int): Boolean {
        val c = Calendar.getInstance()
        c[year, 2] = 1
        c.add(Calendar.DAY_OF_MONTH, -1)
        return c[Calendar.DAY_OF_MONTH] == 29
    }

    override fun setDefaultMillisecond(time: Long) {
        if (time <= 0) return
        if (time < minMillisecond) return
        if (maxMillisecond in 1 until time) return
        val mCalendar = Calendar.getInstance()
        time.let { mCalendar.timeInMillis = it }

        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH) + 1

        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
        mMinute = mCalendar.get(Calendar.MINUTE)
        mSecond = mCalendar.get(Calendar.SECOND)

        millisecond = time
        mYearSpinner?.value = mYear
        mMonthSpinner?.value = mMonth
        mDaySpinner?.value = mDay
        mHourSpinner?.value = mHour
        mMinuteSpinner?.value = mMinute
        mSecondSpinner?.value = mSecond
        limitMaxAndMin()
        onDateTimeChanged()
    }

    override fun setMinMillisecond(time: Long) {
        if (time <= 0) return
        if (maxMillisecond in 1 until time) return
        minMillisecond = time
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = time
        minMonth = mCalendar.get(Calendar.MONTH) + 1
        minDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        minHour = mCalendar.get(Calendar.HOUR_OF_DAY)
        minMinute = mCalendar.get(Calendar.MINUTE)
        minSecond = mCalendar.get(Calendar.SECOND)
        mYearSpinner?.minValue = mCalendar.get(Calendar.YEAR)
        mMonthSpinner?.minValue = mMonth
        mDaySpinner?.minValue = minDay
        mHourSpinner?.minValue = minHour
        mMinuteSpinner?.minValue = minMinute
        mSecondSpinner?.minValue = minSecond
        limitMaxAndMin()
        if (this.millisecond < time) setDefaultMillisecond(time)
    }

    override fun setMaxMillisecond(time: Long) {
        if (time <= 0) return
        if (minMillisecond > 0L && time < minMillisecond) return
        maxMillisecond = time
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = time
        maxMonth = mCalendar.get(Calendar.MONTH) + 1
        maxDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        maxHour = mCalendar.get(Calendar.HOUR_OF_DAY)
        maxMinute = mCalendar.get(Calendar.MINUTE)
        maxSecond = mCalendar.get(Calendar.SECOND)

        mYearSpinner?.maxValue = mCalendar.get(Calendar.YEAR)
        mMonthSpinner?.maxValue = maxMonth
        mDaySpinner?.maxValue = maxDay
        mHourSpinner?.maxValue = maxHour
        mMinuteSpinner?.maxValue = maxMinute
        mSecondSpinner?.maxValue = maxSecond
        limitMaxAndMin()
        if (this.millisecond > time) setDefaultMillisecond(time)
    }

    override fun setOnDateTimeChangedListener(callback: ((Long) -> Unit)?) {
        mOnDateTimeChangedListener = callback
        onDateTimeChanged()
    }

}

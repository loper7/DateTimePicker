package com.loper7.date_time_picker.controller

import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.DateTimeConfig.DAY
import com.loper7.date_time_picker.DateTimeConfig.HOUR
import com.loper7.date_time_picker.DateTimeConfig.MIN
import com.loper7.date_time_picker.DateTimeConfig.MONTH
import com.loper7.date_time_picker.DateTimeConfig.SECOND
import com.loper7.date_time_picker.DateTimeConfig.YEAR
import com.loper7.date_time_picker.ext.*
import com.loper7.date_time_picker.number_picker.NumberPicker
import com.loper7.date_time_picker.utils.lunar.Lunar
import com.loper7.date_time_picker.utils.lunar.LunarConstants
import java.util.*

/**
 *
 * @CreateDate:     2020/9/11 13:36
 * @Description:    日期/时间逻辑控制器
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
class LunarDateTimeController : BaseDateTimeController() {
    private var mYearSpinner: NumberPicker? = null
    private var mMonthSpinner: NumberPicker? = null
    private var mDaySpinner: NumberPicker? = null
    private var mHourSpinner: NumberPicker? = null
    private var mMinuteSpinner: NumberPicker? = null
    private var mSecondSpinner: NumberPicker? = null

    private lateinit var lunar: Lunar
    private lateinit var minLunar: Lunar
    private lateinit var maxLunar: Lunar

    private var global = DateTimeConfig.GLOBAL_LOCAL

    private var mOnDateTimeChangedListener: ((Long) -> Unit)? = null

    private var wrapSelectorWheel = true
    private var wrapSelectorWheelTypes: MutableList<Int>? = null

    private var defaultMin = Lunar.getInstance(1900, 1, false, 1, 0, 0, 0)
    private var defaultMax = Lunar.getInstance(2100, 12, false, 29, 23, 59, 59)


    override fun bindPicker(type: Int, picker: NumberPicker?): LunarDateTimeController {
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

    override fun bindGlobal(global: Int): LunarDateTimeController {
        this.global = global
        return this
    }

    override fun build(): LunarDateTimeController {
        lunar = Lunar.getInstance() ?: defaultMin
        minLunar = defaultMin
        maxLunar = defaultMax

        mYearSpinner?.run {
            maxValue = maxLunar.year
            minValue = minLunar.year
            value = lunar.year
            isFocusable = true
            isFocusableInTouchMode = true
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS //设置NumberPicker不可编辑
            setOnValueChangedListener(onChangeListener)
        }


        mMonthSpinner?.run {
            maxValue = maxLunar.monthIndex
            minValue = minLunar.monthIndex
            value = lunar.monthIndex
            isFocusable = true
            isFocusableInTouchMode = true

            formatter = NumberPicker.Formatter { value: Int ->
                Lunar.getMonthName(mYearSpinner?.value ?: 0, value) + "月"
            }

            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(onChangeListener)
        }

        mDaySpinner?.run {
            maxValue = maxLunar.day
            minValue = minLunar.day
            value = lunar.day
            isFocusable = true
            isFocusableInTouchMode = true
            formatter = NumberPicker.Formatter { value: Int ->
                if (value != 0)
                    LunarConstants.LUNAR_DAY_NAMES[value - 1]
                else
                    "$value"
            }
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(onChangeListener)
        }

        mHourSpinner?.run {
            maxValue = maxLunar.hour
            minValue = minLunar.hour
            isFocusable = true
            isFocusableInTouchMode = true
            value = lunar.hour
            formatter = DateTimeConfig.formatter
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(onChangeListener)
        }

        mMinuteSpinner?.run {
            maxValue = maxLunar.minute
            minValue = minLunar.minute
            isFocusable = true
            isFocusableInTouchMode = true
            value = lunar.minute
            formatter = DateTimeConfig.formatter
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(onChangeListener)
        }

        mSecondSpinner?.run {
            maxValue = maxLunar.seconds
            minValue = minLunar.seconds
            isFocusable = true
            isFocusableInTouchMode = true
            value = lunar.seconds
            formatter = DateTimeConfig.formatter
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener(onChangeListener)
        }
        return this
    }


    private val onChangeListener = NumberPicker.OnValueChangeListener { view, old, new ->
        limitMaxAndMin()
        onDateTimeChanged()
    }

    /**
     * 同步数据
     */
    private fun syncDateData() {
        mYearSpinner?.apply { lunar.year = value }
        mMonthSpinner?.apply { lunar.updateMonth(value) }
        mDaySpinner?.apply { lunar.day = value }
        mHourSpinner?.apply { lunar.hour = value }
        mMinuteSpinner?.apply { lunar.minute = value }
        mSecondSpinner?.apply { lunar.seconds = value }
    }

    /**
     * 日期发生变化
     */
    private fun onDateTimeChanged() {
        syncDateData()
        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener?.invoke(lunar.toCalendar()?.timeInMillis ?: 0)
        }
    }

    /**
     * 设置允许选择的区间
     */
    private fun limitMaxAndMin() {
        syncDateData()

        var maxDayInMonth = lunar.getMaxDayInMonth()

        mMonthSpinner?.apply {
            minValue =
                if (lunar.isSameYear(minLunar)) minLunar.monthIndex else 1
            maxValue =
                if ((lunar.isSameYear(maxLunar))) maxLunar.monthIndex else lunar.getTotalMonth()
        }
        mDaySpinner?.apply {
            minValue =
                if (lunar.isSameMonth(minLunar)) minLunar.day else 1
            maxValue =
                if (lunar.isSameMonth(maxLunar)) maxLunar.day else maxDayInMonth
        }
        mHourSpinner?.apply {
            minValue =
                if (lunar.isSameDay(minLunar)) minLunar.hour else 0
            maxValue =
                if (lunar.isSameDay(maxLunar)) maxLunar.hour else 23
        }
        mMinuteSpinner?.apply {
            minValue = if (lunar.isSameHour(minLunar)) minLunar.minute else 0
            maxValue =
                if (lunar.isSameHour(maxLunar)) maxLunar.minute else 59
        }
        mSecondSpinner?.apply {
            minValue =
                if (lunar.isSameMinute(minLunar)) minLunar.seconds else 0
            maxValue =
                if (lunar.isSameMinute(maxLunar)) maxLunar.seconds else 59
        }

        if (mDaySpinner?.value ?: 0 >= maxDayInMonth) {
            mDaySpinner?.value = maxDayInMonth
            onDateTimeChanged()
        }
        setWrapSelectorWheel(wrapSelectorWheelTypes, wrapSelectorWheel)

    }


    override fun setDefaultMillisecond(time: Long) {
        if (time == 0L) return
        if (time < minLunar.toCalendar()?.timeInMillis ?: 0) return
        if (time > maxLunar.toCalendar()?.timeInMillis ?: 0) return

        lunar = Lunar.getInstance(time) ?: defaultMin

        mYearSpinner?.value = lunar.year
        mMonthSpinner?.value = lunar.monthIndex
        mDaySpinner?.value = lunar.day
        mHourSpinner?.value = lunar.hour
        mMinuteSpinner?.value = lunar.minute
        mSecondSpinner?.value = lunar.seconds

        limitMaxAndMin()
        onDateTimeChanged()
    }

    override fun setMinMillisecond(time: Long) {

        if (time == 0L) return
        if (maxLunar?.toCalendar()?.timeInMillis ?: 0 in 1 until time) return
        minLunar = Lunar.getInstance(time) ?: defaultMin

        mYearSpinner?.minValue = minLunar.year

        limitMaxAndMin()
        setWrapSelectorWheel(wrapSelectorWheelTypes, wrapSelectorWheel)
        if (!lunar.isLast(minLunar)) setDefaultMillisecond(minLunar.toCalendar()?.timeInMillis ?: 0)
    }

    override fun setMaxMillisecond(time: Long) {
        if (time == 0L) return
        if (minLunar?.toCalendar()?.timeInMillis ?: 0 > 0L && time < minLunar?.toCalendar()?.timeInMillis ?: 0) return
        maxLunar = Lunar.getInstance(time) ?: defaultMax

        mYearSpinner?.maxValue = maxLunar.year
        limitMaxAndMin()
        setWrapSelectorWheel(wrapSelectorWheelTypes, wrapSelectorWheel)
        if (lunar.isLast(minLunar)) setDefaultMillisecond(maxLunar.toCalendar()?.timeInMillis ?: 0)
    }


    override fun setWrapSelectorWheel(types: MutableList<Int>?, wrapSelector: Boolean) {
        this.wrapSelectorWheelTypes = types
        this.wrapSelectorWheel = wrapSelector
        if (wrapSelectorWheelTypes == null || wrapSelectorWheelTypes!!.isEmpty()) {
            wrapSelectorWheelTypes = mutableListOf()
            wrapSelectorWheelTypes!!.add(YEAR)
            wrapSelectorWheelTypes!!.add(MONTH)
            wrapSelectorWheelTypes!!.add(DAY)
            wrapSelectorWheelTypes!!.add(HOUR)
            wrapSelectorWheelTypes!!.add(MIN)
            wrapSelectorWheelTypes!!.add(SECOND)
        }

        wrapSelectorWheelTypes!!.apply {
            for (type in this) {
                when (type) {
                    YEAR -> mYearSpinner?.run { wrapSelectorWheel = wrapSelector }
                    MONTH -> mMonthSpinner?.run { wrapSelectorWheel = wrapSelector }
                    DAY -> mDaySpinner?.run { wrapSelectorWheel = wrapSelector }
                    HOUR -> mHourSpinner?.run { wrapSelectorWheel = wrapSelector }
                    MIN -> mMinuteSpinner?.run { wrapSelectorWheel = wrapSelector }
                    SECOND -> mSecondSpinner?.run { wrapSelectorWheel = wrapSelector }
                }
            }
        }
    }


    override fun setOnDateTimeChangedListener(callback: ((Long) -> Unit)?) {
        mOnDateTimeChangedListener = callback
        onDateTimeChanged()
    }

    override fun getMillisecond(): Long {
        return lunar.toCalendar()?.timeInMillis ?: 0
    }

}

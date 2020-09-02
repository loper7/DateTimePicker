package com.loper7.date_time_picker

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import com.loper7.date_time_picker.number_picker.NumberPicker
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar

class DateTimePicker : FrameLayout {
    private lateinit var mYearSpinner: NumberPicker
    private lateinit var mMonthSpinner: NumberPicker
    private lateinit var mDaySpinner: NumberPicker
    private lateinit var mHourSpinner: NumberPicker
    private lateinit var mMinuteSpinner: NumberPicker

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0

    private var minMillisecond = 0L
    private var minMonth = 1
    private var minDay = 1
    private var minHour = 0
    private var minMinute = 0

    private var maxMillisecond = 0L
    private var maxMonth = 12
    private var maxDay = 31
    private var maxHour = 23
    private var maxMinute = 59

    private var millisecond: Long = 0
    private var mOnDateTimeChangedListener: ((DateTimePicker?, Long) -> Unit)? = null

    private var displayType = intArrayOf(YEAR, MONTH, DAY, HOUR, MIN)

    private var showLabel = true
    private var themeColor = 0
    private var textSize = 0

    private var yearLabel = "年"
    private var monthLabel = "月"
    private var dayLabel = "日"
    private var hourLabel = "时"
    private var minLabel = "分"

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : this(context, attrs)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val attributesArray = context.obtainStyledAttributes(attrs, R.styleable.DateTimePicker)
        showLabel = attributesArray.getBoolean(R.styleable.DateTimePicker_showLabel, true)
        themeColor = attributesArray.getColor(
            R.styleable.DateTimePicker_themeColor,
            ContextCompat.getColor(context, R.color.colorAccent)
        )
        textSize = px2dip(
            attributesArray.getDimensionPixelSize(
                R.styleable.DateTimePicker_textSize,
                dip2px(15f).toInt()
            ).toFloat()
        ).toInt()
        attributesArray.recycle()
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {

        val mDate = Calendar.getInstance()
        mYear = mDate.get(Calendar.YEAR)
        mMonth = mDate.get(Calendar.MONTH) + 1
        mDay = mDate.get(Calendar.DAY_OF_MONTH)
        mHour = mDate.get(Calendar.HOUR_OF_DAY)
        mMinute = mDate.get(Calendar.MINUTE)
        millisecond = mDate.timeInMillis
        View.inflate(context, R.layout.view_date_picker, this)

        mYearSpinner = findViewById(R.id.np_datetime_year)
        mYearSpinner.label = yearLabel
        mYearSpinner.maxValue = mYear + 100
        mYearSpinner.minValue = 1800
        mYearSpinner.value = mYear
        mYearSpinner.isFocusable = true
        mYearSpinner.isFocusableInTouchMode = true
        mYearSpinner.descendantFocusability =
            NumberPicker.FOCUS_BLOCK_DESCENDANTS //设置NumberPicker不可编辑
        mYearSpinner.setOnValueChangedListener(mOnYearChangedListener) //注册NumberPicker值变化时的监听事件

        mMonthSpinner = findViewById(R.id.np_datetime_month)
        mMonthSpinner.label = monthLabel
        mMonthSpinner.maxValue = 12
        mMonthSpinner.minValue = 1
        mMonthSpinner.value = mMonth
        mMonthSpinner.isFocusable = true
        mMonthSpinner.isFocusableInTouchMode = true
        mMonthSpinner.setFormatter(formatter) //格式化显示数字，个位数前添加0
        mMonthSpinner.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        mMonthSpinner.setOnValueChangedListener(mOnMonthChangedListener)

        mDaySpinner = findViewById(R.id.np_datetime_day)
        mDaySpinner.label = dayLabel
        leapMonth() //判断是否闰年，从而设置2月份的天数
        mDaySpinner.isFocusable = true
        mDaySpinner.isFocusableInTouchMode = true
        mDaySpinner.value = mDay
        mDaySpinner.setFormatter(formatter)
        mDaySpinner.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        mDaySpinner.setOnValueChangedListener(mOnDayChangedListener)

        mHourSpinner = findViewById(R.id.np_datetime_hour)
        mHourSpinner.label = hourLabel
        mHourSpinner.maxValue = 23
        mHourSpinner.minValue = 0
        mHourSpinner.isFocusable = true
        mHourSpinner.isFocusableInTouchMode = true
        mHourSpinner.value = mHour
        mHourSpinner.setFormatter(formatter)
        mHourSpinner.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        mHourSpinner.setOnValueChangedListener(mOnHourChangedListener)

        mMinuteSpinner = findViewById(R.id.np_datetime_minute)
        mMinuteSpinner.label = minLabel
        mMinuteSpinner.maxValue = 59
        mMinuteSpinner.minValue = 0
        mMinuteSpinner.isFocusable = true
        mMinuteSpinner.isFocusableInTouchMode = true
        mMinuteSpinner.value = mMinute
        mMinuteSpinner.setFormatter(formatter)
        mMinuteSpinner.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener)
    }

    private fun refreshUI() {
        if (showLabel) {
            mYearSpinner.label = yearLabel
            mMonthSpinner.label = monthLabel
            mDaySpinner.label = dayLabel
            mHourSpinner.label = hourLabel
            mMinuteSpinner.label = minLabel
        } else {
            mYearSpinner.label = ""
            mMonthSpinner.label = ""
            mDaySpinner.label = ""
            mHourSpinner.label = ""
            mMinuteSpinner.label = ""
        }
        mYearSpinner.setTextColor(themeColor)
        mYearSpinner.setTextSize(textSize)
        mMonthSpinner.setTextColor(themeColor)
        mMonthSpinner.setTextSize(textSize)
        mDaySpinner.setTextColor(themeColor)
        mDaySpinner.setTextSize(textSize)
        mHourSpinner.setTextColor(themeColor)
        mHourSpinner.setTextSize(textSize)
        mMinuteSpinner.setTextColor(themeColor)
        mMinuteSpinner.setTextSize(textSize)
    }

    private val mOnYearChangedListener =
        NumberPicker.OnValueChangeListener { _, _, _, _ ->
            mYear = mYearSpinner.value
            leapMonth()
            limitMaxAndMin()
            onDateTimeChanged()
        }

    private val mOnMonthChangedListener =
        NumberPicker.OnValueChangeListener { _, _, _, _ ->
            mMonth = mMonthSpinner.value
            leapMonth()
            limitMaxAndMin()
            onDateTimeChanged()
        }

    private val mOnDayChangedListener =
        NumberPicker.OnValueChangeListener { _, _, _, _ ->
            mDay = mDaySpinner.value
            limitMaxAndMin()
            onDateTimeChanged()
        }

    private val mOnHourChangedListener =
        NumberPicker.OnValueChangeListener { _, _, _, _ ->
            mHour = mHourSpinner.value
            limitMaxAndMin()
            onDateTimeChanged()
        }

    private val mOnMinuteChangedListener =
        NumberPicker.OnValueChangeListener { _, _, _, _ ->
            mMinute = mMinuteSpinner.value
            onDateTimeChanged()
        }

    /**
     * 日期发生变化
     */
    private fun onDateTimeChanged() {

        if (!displayType.contains(HOUR)) {
            mHour = 0
        }

        if (!displayType.contains(MIN)) {
            mMinute = 0
        }

        millisecond =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime.of(mYear, mMonth, mDay, mHour, mMinute)
                    .toInstant(ZoneOffset.ofHours(8))
                    .toEpochMilli()
            } else {
                val mCalendar = Calendar.getInstance()
                mCalendar.set(mYear, mMonth, mDay, mHour, mMinute)
                mCalendar.timeInMillis
            }


        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener?.invoke(this, millisecond)
        }
    }

    /**
     * 判定闰月
     */
    private fun leapMonth() {
        if (mMonth == 2) {
            if (isLeapYear(mYear)) {
                if (mDaySpinner.maxValue != 29) {
                    mDaySpinner.displayedValues = null
                    mDaySpinner.minValue = 1
                    mDaySpinner.maxValue = 29
                }
            } else {
                if (mDaySpinner.maxValue != 28) {
                    mDaySpinner.displayedValues = null
                    mDaySpinner.minValue = 1
                    mDaySpinner.maxValue = 28
                }
            }
        } else {
            when (mMonth) {
                4, 6, 9, 11 -> if (mDaySpinner.maxValue != 30) {
                    mDaySpinner.displayedValues = null
                    mDaySpinner.minValue = 1
                    mDaySpinner.maxValue = 30
                }
                else -> if (mDaySpinner.maxValue != 31) {
                    mDaySpinner.displayedValues = null
                    mDaySpinner.minValue = 1
                    mDaySpinner.maxValue = 31
                }
            }
        }
        if (mYear == mYearSpinner.minValue && mMonth == mMonthSpinner.minValue) {
            mDaySpinner.minValue = minDay
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

    private val TAG = "DateTimePicker"

    /**
     * 设置允许选择的区间
     */
    private fun limitMaxAndMin() {
        //设置月份最小值
        mMonthSpinner.minValue =
            if (mYear == mYearSpinner.minValue)
                minMonth
            else
                1

        //设置月份最大值
        mMonthSpinner.maxValue =
            if (mYear == mYearSpinner.maxValue)
                maxMonth
            else
                12

        if (mMonthSpinner.value != 0) {
            mMonth = mMonthSpinner.value
        }

        /** */ //设置天最小值
        mDaySpinner.minValue =
            if (mYear == mYearSpinner.minValue && mMonth == mMonthSpinner.minValue)
                minDay
            else
                1

        //设置天最大值
        mDaySpinner.maxValue =
            if (mYear == mYearSpinner.maxValue && mMonth == mMonthSpinner.maxValue)
                maxDay
            else
                leapMonth2days(mYear)

        mDay = mDaySpinner.value

        /** */ //设置小时最小值
        mHourSpinner.minValue =
            if (mYear == mYearSpinner.minValue && mMonth == mMonthSpinner.minValue && mDay == mDaySpinner.minValue)
                minHour
            else
                0

        //设置小时最大值
        mHourSpinner.maxValue =
            if (mYear == mYearSpinner.maxValue && mMonth == mMonthSpinner.maxValue && mDay == mDaySpinner.maxValue)
                maxHour
            else
                23
        mHour = mHourSpinner.value

        /** */ //设置分钟最小值
        mMinuteSpinner.minValue =
            if (mYear == mYearSpinner.minValue && mMonth == mMonthSpinner.minValue && mDay == mDaySpinner.minValue && mHour == mHourSpinner.minValue)
                minMinute
            else
                0

        //设置分钟最大值
        mMinuteSpinner.maxValue =
            if (mYear == mYearSpinner.maxValue && mMonth == mMonthSpinner.maxValue && mDay == mDaySpinner.maxValue && mHour == mHourSpinner.maxValue)
                maxMinute
            else
                59
        mMinute = mMinuteSpinner.value
    }

    /**
     * 根据手机的分辨率dp 转成px(像素)
     */
    private fun dip2px(dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    /**
     * 根据手机的分辨率px(像素) 转成dp
     */
    private fun px2dip(pxValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return pxValue / scale + 0.5f
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

    /************************************************************************/
    /*
     *对外的公开方法
     */
    fun setOnDateTimeChangedListener(callback: ((DateTimePicker?, Long) -> Unit)? = null) {
        mOnDateTimeChangedListener = callback
        onDateTimeChanged()
    }

    /**
     * 设置显示类型
     *
     * @param types
     */
    fun setDisplayType(types: IntArray?) {
        if (types == null || types.isEmpty()) return
        displayType = types

        if (!displayType.contains(YEAR)) {
            mYearSpinner.visibility = View.GONE
        }

        if (!displayType.contains(MONTH)) {
            mMonthSpinner.visibility = View.GONE
        }

        if (!displayType.contains(DAY)) {
            mDaySpinner.visibility = View.GONE
        }

        if (!displayType.contains(HOUR)) {
            mHourSpinner.visibility = View.GONE
        }

        if (!displayType.contains(MIN)) {
            mMinuteSpinner.visibility = View.GONE
        }
    }

    /**
     * 设置默认时间戳
     *
     * @param time
     */
    fun setDefaultMillisecond(time: Long) {
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

        millisecond = time
        mYearSpinner.value = mYear
        mMonthSpinner.value = mMonth
        mDaySpinner.value = mDay
        mHourSpinner.value = mHour
        mMinuteSpinner.value = mMinute
        limitMaxAndMin()
        onDateTimeChanged()
    }

    /**
     * 设置最小选择时间
     *
     * @param time
     */
    fun setMinMillisecond(time: Long) {
        if (time <= 0) return
        if (maxMillisecond in 1 until time) return
        minMillisecond = time
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = time
        minMonth = mCalendar.get(Calendar.MONTH) + 1
        minDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        minHour = mCalendar.get(Calendar.HOUR_OF_DAY)
        minMinute = mCalendar.get(Calendar.MINUTE)
        mYearSpinner.minValue = mCalendar.get(Calendar.YEAR)
        mMonthSpinner.minValue = mMonth
        mDaySpinner.minValue = minDay
        mHourSpinner.minValue = minDay
        mMinuteSpinner.minValue = minDay
        limitMaxAndMin()
        if (this.millisecond < time) setDefaultMillisecond(time)
    }

    /**
     * 设置最大选择时间
     *
     * @param time
     */
    fun setMaxMillisecond(time: Long) {
        if (time <= 0) return
        if (minMillisecond > 0L && time < minMillisecond) return
        maxMillisecond = time
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = time
        maxMonth = mCalendar.get(Calendar.MONTH) + 1
        maxDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        maxHour = mCalendar.get(Calendar.HOUR_OF_DAY)
        maxMinute = mCalendar.get(Calendar.MINUTE)
        mYearSpinner.maxValue = mCalendar.get(Calendar.YEAR)
        mMonthSpinner.maxValue = maxMonth
        mDaySpinner.maxValue = maxDay
        mHourSpinner.maxValue = maxHour
        mMinuteSpinner.maxValue = maxMinute
        limitMaxAndMin()
        if (this.millisecond > time) setDefaultMillisecond(time)
    }

    /**
     * 是否显示label
     *
     * @param b
     */
    fun showLabel(b: Boolean) {
        showLabel = b
        refreshUI()
    }

    /**
     * 主题颜色
     *
     * @param color
     */
    fun setThemeColor(@ColorInt color: Int) {
        if (color == 0) return
        themeColor = color
        refreshUI()
    }

    /**
     * 字体大小
     *
     * @param sp
     */
    fun setTextSize(@Dimension sp: Int) {
        textSize = sp
        refreshUI()
    }

    /**
     * 设置标签文字
     * @param year 年标签
     * @param month 月标签
     * @param day 日标签
     * @param hour 时标签
     * @param min 分份标签
     */
    fun setLabelText(
        year: String = yearLabel,
        month: String = monthLabel,
        day: String = dayLabel,
        hour: String = hourLabel,
        min: String = minLabel
    ) {
        this.yearLabel = year
        this.monthLabel = month
        this.dayLabel = day
        this.hourLabel = hour
        this.minLabel = min
        refreshUI()
    }

    companion object {
        var YEAR = 0
        var MONTH = 1
        var DAY = 2
        var HOUR = 3
        var MIN = 4

        //数字格式化，<10的数字前自动加0
        private val formatter =
            NumberPicker.Formatter { value: Int ->
                var str = value.toString()
                if (value < 10) {
                    str = "0$str"
                }
                str
            }
    }
}
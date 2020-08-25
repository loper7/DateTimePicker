package com.loper7.date_time_picker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import com.loper7.date_time_picker.number_picker.NumberPicker
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class DateTimePicker : FrameLayout {
    private var mYearSpinner: NumberPicker? = null
    private var mMonthSpinner: NumberPicker? = null
    private var mDaySpinner: NumberPicker? = null
    private var mHourSpinner: NumberPicker? = null
    private var mMinuteSpinner: NumberPicker? = null

    private var mDate: Calendar? = null
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0

    private var minMonth = 1
    private var minDay = 1
    private var minHour = 0
    private var minMinute = 0

    private var maxMonth = 0
    private var maxDay = 0
    private var maxHour = 0
    private var maxMinute = 0

    private var millisecond: Long? = null
    private var mOnDateTimeChangedListener: OnDateTimeChangedListener? = null
    private var displayType = intArrayOf(YEAR, MONTH, DAY, HOUR, MIN)

    private var showLabel = true
    private var themeColor = 0
    private var textSize = 0

    private var yearLabel = "年"
    private var monthLabel = "月"
    private var dayLabel = "日"
    private var hourLabel = "时"
    private var minLabel = "分"

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs) {
        init(context)
    }

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
        mDate = Calendar.getInstance()
        mYear = mDate!!.get(Calendar.YEAR)
        mMonth = mDate!!.get(Calendar.MONTH) + 1
        mHour = mDate!!.get(Calendar.HOUR_OF_DAY)
        mMinute = mDate!!.get(Calendar.MINUTE)
        millisecond = mDate!!.timeInMillis
        View.inflate(context, R.layout.view_date_picker, this)
        mYearSpinner = findViewById<View>(R.id.np_datetime_year) as NumberPicker

        mYearSpinner!!.maxValue = 2100
        mYearSpinner!!.minValue = 1900
        mYearSpinner!!.label = yearLabel
        mYearSpinner!!.value = mYear
        mYearSpinner!!.isFocusable = true
        mYearSpinner!!.isFocusableInTouchMode = true
        mYearSpinner!!.descendantFocusability =
            NumberPicker.FOCUS_BLOCK_DESCENDANTS //设置NumberPicker不可编辑
        mYearSpinner!!.setOnValueChangedListener(mOnYearChangedListener) //注册NumberPicker值变化时的监听事件
        mMonthSpinner = findViewById<View>(R.id.np_datetime_month) as NumberPicker

        mMonthSpinner!!.maxValue = 12
        mMonthSpinner!!.minValue = 1
        mMonthSpinner!!.label = monthLabel
        mMonthSpinner!!.value = mMonth
        mMonthSpinner!!.isFocusable = true
        mMonthSpinner!!.isFocusableInTouchMode = true
        mMonthSpinner!!.setFormatter(formatter) //格式化显示数字，个位数前添加0
        mMonthSpinner!!.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        mMonthSpinner!!.setOnValueChangedListener(mOnMonthChangedListener)
        mDaySpinner = findViewById<View>(R.id.np_datetime_day) as NumberPicker

        leapMonth() //判断是否闰年，从而设置2月份的天数
        mDay = mDate!!.get(Calendar.DAY_OF_MONTH)
        mDaySpinner!!.setFormatter(formatter)
        mDaySpinner!!.label = dayLabel
        mDaySpinner!!.isFocusable = true
        mDaySpinner!!.isFocusableInTouchMode = true
        mDaySpinner!!.value = mDay
        mDaySpinner!!.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        mDaySpinner!!.setOnValueChangedListener(mOnDayChangedListener)
        mHourSpinner = findViewById<View>(R.id.np_datetime_hour) as NumberPicker

        mHourSpinner!!.maxValue = 23
        mHourSpinner!!.minValue = 0
        mYearSpinner!!.isFocusable = true
        mHourSpinner!!.isFocusableInTouchMode = true
        mHourSpinner!!.label = hourLabel
        mHourSpinner!!.value = mHour
        mHourSpinner!!.setFormatter(formatter)
        mHourSpinner!!.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        mHourSpinner!!.setOnValueChangedListener(mOnHourChangedListener)
        mMinuteSpinner = findViewById<View>(R.id.np_datetime_minute) as NumberPicker

        mMinuteSpinner!!.maxValue = 59
        mMinuteSpinner!!.minValue = 0
        mMinuteSpinner!!.isFocusable = true
        mMinuteSpinner!!.label = minLabel
        mMinuteSpinner!!.isFocusableInTouchMode = true
        mMinuteSpinner!!.value = mMinute
        mMinuteSpinner!!.setFormatter(formatter)
        mMinuteSpinner!!.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        mMinuteSpinner!!.setOnValueChangedListener(mOnMinuteChangedListener)

        refreshUI()
    }

    private fun refreshUI() {
        if (showLabel) {
            mYearSpinner!!.label = yearLabel
            mMonthSpinner!!.label = monthLabel
            mDaySpinner!!.label = dayLabel
            mHourSpinner!!.label = hourLabel
            mMinuteSpinner!!.label = minLabel
        } else {
            mYearSpinner!!.label = ""
            mMonthSpinner!!.label = ""
            mDaySpinner!!.label = ""
            mHourSpinner!!.label = ""
            mMinuteSpinner!!.label = ""
        }
        mYearSpinner!!.setTextColor(themeColor)
        mYearSpinner!!.setTextSize(textSize)
        mMonthSpinner!!.setTextColor(themeColor)
        mMonthSpinner!!.setTextSize(textSize)
        mDaySpinner!!.setTextColor(themeColor)
        mDaySpinner!!.setTextSize(textSize)
        mHourSpinner!!.setTextColor(themeColor)
        mHourSpinner!!.setTextSize(textSize)
        mMinuteSpinner!!.setTextColor(themeColor)
        mMinuteSpinner!!.setTextSize(textSize)
    }

    private val mOnYearChangedListener =
        NumberPicker.OnValueChangeListener { picker, oldVal, newVal, editText ->
            mYear = mYearSpinner!!.value
            leapMonth()
            limitMaxMin()
            onDateTimeChanged()
        }
    private val mOnMonthChangedListener =
        NumberPicker.OnValueChangeListener { picker, oldVal, newVal, editText ->
            mMonth = mMonthSpinner!!.value
            leapMonth()
            limitMaxMin()
            onDateTimeChanged()
        }
    private val mOnDayChangedListener =
        NumberPicker.OnValueChangeListener { picker, oldVal, newVal, editText ->
            mDay = mDaySpinner!!.value
            limitMaxMin()
            onDateTimeChanged()
        }
    private val mOnHourChangedListener =
        NumberPicker.OnValueChangeListener { picker, oldVal, newVal, editText ->
            mHour = mHourSpinner!!.value
            limitMaxMin()
            onDateTimeChanged()
        }
    private val mOnMinuteChangedListener =
        NumberPicker.OnValueChangeListener { picker, oldVal, newVal, editText ->
            mMinute = mMinuteSpinner!!.value
            //            limitMaxMin();
            onDateTimeChanged()
        }

    /*
     *接口回调 参数是当前的View 年月日时分秒
     */
    interface OnDateTimeChangedListener {
        fun onDateTimeChanged(view: DateTimePicker?, millisecond: Long)
    }

    /**
     * 日期发生变化
     */
    private fun onDateTimeChanged() {
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


        if (mOnDateTimeChangedListener != null && millisecond != null) {
            mOnDateTimeChangedListener!!.onDateTimeChanged(this, millisecond!!)
        }
    }

    /**
     * 判定闰月
     */
    private fun leapMonth() {
        if (mMonth == 2) {
            if (isLeapYear(mYear)) {
                if (mDaySpinner!!.maxValue != 29) {
                    mDaySpinner!!.displayedValues = null
                    mDaySpinner!!.minValue = 1
                    mDaySpinner!!.maxValue = 29
                }
            } else {
                if (mDaySpinner!!.maxValue != 28) {
                    mDaySpinner!!.displayedValues = null
                    mDaySpinner!!.minValue = 1
                    mDaySpinner!!.maxValue = 28
                }
            }
        } else {
            when (mMonth) {
                4, 6, 9, 11 -> if (mDaySpinner!!.maxValue != 30) {
                    mDaySpinner!!.displayedValues = null
                    mDaySpinner!!.minValue = 1
                    mDaySpinner!!.maxValue = 30
                }
                else -> if (mDaySpinner!!.maxValue != 31) {
                    mDaySpinner!!.displayedValues = null
                    mDaySpinner!!.minValue = 1
                    mDaySpinner!!.maxValue = 31
                }
            }
        }
        if (mYear == mYearSpinner!!.minValue && mMonth == mMonthSpinner!!.minValue) {
            mDaySpinner!!.minValue = minDay
        }
        mDay = mDaySpinner!!.value
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

    private fun limitMaxMin() { //设置月份最小值
        if (mYear == mYearSpinner!!.minValue) mMonthSpinner!!.minValue =
            minMonth else mMonthSpinner!!.minValue = 1
        //设置月份最大值
        if (mYear == mYearSpinner!!.maxValue) mMonthSpinner!!.maxValue =
            maxMonth else mMonthSpinner!!.maxValue = 12
        mMonth = mMonthSpinner!!.value
        /** */ //设置天最小值
        if (mYear == mYearSpinner!!.minValue && mMonth == mMonthSpinner!!.minValue) mDaySpinner!!.minValue =
            minDay else mDaySpinner!!.minValue = 1
        //设置天最大值
        if (mYear == mYearSpinner!!.maxValue && mMonth == mMonthSpinner!!.maxValue) mDaySpinner!!.maxValue =
            maxDay else mDaySpinner!!.maxValue = leapMonth2days(mYear)
        mDay = mDaySpinner!!.value
        /** */ //设置小时最小值
        if (mYear == mYearSpinner!!.minValue && mMonth == mMonthSpinner!!.minValue && mDay == mDaySpinner!!.minValue) mHourSpinner!!.minValue =
            minHour else mHourSpinner!!.minValue = 0
        //设置小时最大值
        if (mYear == mYearSpinner!!.maxValue && mMonth == mMonthSpinner!!.maxValue && mDay == mDaySpinner!!.maxValue) mHourSpinner!!.maxValue =
            maxHour else mHourSpinner!!.maxValue = 23
        mHour = mHourSpinner!!.value
        /** */ //设置分钟最小值
        if (mYear == mYearSpinner!!.minValue && mMonth == mMonthSpinner!!.minValue && mDay == mDaySpinner!!.minValue && mHour == mHourSpinner!!.minValue
        ) mMinuteSpinner!!.minValue = minMinute else mMinuteSpinner!!.minValue = 0
        //设置分钟最大值
        if (mYear == mYearSpinner!!.maxValue && mMonth == mMonthSpinner!!.maxValue && mDay == mDaySpinner!!.maxValue && mHour == mHourSpinner!!.maxValue
        ) mMinuteSpinner!!.maxValue = maxMinute else mMinuteSpinner!!.maxValue = 59
        mMinute = mMinuteSpinner!!.value
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
        return c[Calendar.DAY_OF_MONTH] == 28
    }

    /************************************************************************/
    /*
     *对外的公开方法
     */
    fun setOnDateTimeChangedListener(callback: OnDateTimeChangedListener?) {
        mOnDateTimeChangedListener = callback
        onDateTimeChanged()
    }

    /**
     * 设置显示类型
     *
     * @param types
     */
    fun setDisplayType(types: IntArray?) {
        if (types == null || types.size <= 0) return
        displayType = types
        mYearSpinner!!.visibility = View.GONE
        mMonthSpinner!!.visibility = View.GONE
        mDaySpinner!!.visibility = View.GONE
        mHourSpinner!!.visibility = View.GONE
        mMinuteSpinner!!.visibility = View.GONE
        for (i in types.indices) {
            if (displayType[i] == YEAR) mYearSpinner!!.visibility = View.VISIBLE
            if (displayType[i] == MONTH) mMonthSpinner!!.visibility = View.VISIBLE
            if (displayType[i] == DAY) mDaySpinner!!.visibility = View.VISIBLE
            if (displayType[i] == HOUR) mHourSpinner!!.visibility = View.VISIBLE
            if (displayType[i] == MIN) mMinuteSpinner!!.visibility = View.VISIBLE
        }
    }

    /**
     * 设置默认时间戳
     *
     * @param time
     */
    fun setDefaultMillisecond(time: Long) {
        if (time != 0L) mDate!!.time = Date(time)
        mYear = mDate!![Calendar.YEAR]
        mMonth = mDate!![Calendar.MONTH] + 1
        mDay = mDate!![Calendar.DAY_OF_MONTH]
        mHour = mDate!![Calendar.HOUR_OF_DAY]
        mMinute = mDate!![Calendar.MINUTE]
        millisecond = mDate!!.timeInMillis
        mYearSpinner!!.value = mYear
        mMonthSpinner!!.value = mMonth
        mDaySpinner!!.value = mDay
        mHourSpinner!!.value = mHour
        mMinuteSpinner!!.value = mMinute
        limitMaxMin()
        onDateTimeChanged()
    }

    /**
     * 设置最小选择时间
     *
     * @param time
     */
    fun setMinMillisecond(time: Long) {
        if (time == 0L) return
        val mDate = Calendar.getInstance()
        mDate.time = Date(time)
        val mYear = mDate[Calendar.YEAR]
        minMonth = mDate[Calendar.MONTH] + 1
        minDay = mDate[Calendar.DAY_OF_MONTH]
        minHour = mDate[Calendar.HOUR_OF_DAY]
        minMinute = mDate[Calendar.MINUTE]
        mYearSpinner!!.minValue = mYear
        mMonthSpinner!!.minValue = mMonth
        mDaySpinner!!.minValue = minDay
        mHourSpinner!!.minValue = minDay
        mMinuteSpinner!!.minValue = minDay
        limitMaxMin()
        if (this.mDate!!.timeInMillis < mDate.timeInMillis) setDefaultMillisecond(time)
    }

    /**
     * 设置最大选择时间
     *
     * @param time
     */
    fun setMaxMillisecond(time: Long) {
        if (time == 0L) return
        val mDate = Calendar.getInstance()
        mDate.time = Date(time)
        val mYear = mDate[Calendar.YEAR]
        maxMonth = mDate[Calendar.MONTH] + 1
        maxDay = mDate[Calendar.DAY_OF_MONTH]
        maxHour = mDate[Calendar.HOUR_OF_DAY]
        maxMinute = mDate[Calendar.MINUTE]
        mYearSpinner!!.maxValue = mYear
        mMonthSpinner!!.maxValue = maxMonth
        mDaySpinner!!.maxValue = maxDay
        mHourSpinner!!.maxValue = maxHour
        mMinuteSpinner!!.maxValue = maxMinute
        limitMaxMin()
        if (this.mDate!!.timeInMillis > mDate.timeInMillis) setDefaultMillisecond(time)
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
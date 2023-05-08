package com.loper7.date_time_picker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import com.loper7.date_time_picker.DateTimeConfig.DAY
import com.loper7.date_time_picker.DateTimeConfig.GLOBAL_LOCAL
import com.loper7.date_time_picker.DateTimeConfig.HOUR
import com.loper7.date_time_picker.DateTimeConfig.MIN
import com.loper7.date_time_picker.DateTimeConfig.MONTH
import com.loper7.date_time_picker.DateTimeConfig.SECOND
import com.loper7.date_time_picker.DateTimeConfig.YEAR
import com.loper7.date_time_picker.controller.BaseDateTimeController
import com.loper7.date_time_picker.controller.DateTimeInterface
import com.loper7.date_time_picker.controller.DateTimeController
import com.loper7.date_time_picker.number_picker.NumberPicker
import com.loper7.tab_expand.ext.dip2px
import com.loper7.tab_expand.ext.px2dip
import org.jetbrains.annotations.NotNull
import java.lang.Exception

open class DateTimePicker : FrameLayout, DateTimeInterface {

    private var mYearSpinner: NumberPicker? = null
    private var mMonthSpinner: NumberPicker? = null
    private var mDaySpinner: NumberPicker? = null
    private var mHourSpinner: NumberPicker? = null
    private var mMinuteSpinner: NumberPicker? = null
    private var mSecondSpinner: NumberPicker? = null

    private var displayType = intArrayOf(YEAR, MONTH, DAY, HOUR, MIN, SECOND)

    private var showLabel = true
    private var themeColor = 0
    private var textColor = 0
    private var dividerColor = 0
    private var selectTextSize = 0
    private var normalTextSize = 0

    private var yearLabel = "年"
    private var monthLabel = "月"
    private var dayLabel = "日"
    private var hourLabel = "时"
    private var minLabel = "分"
    private var secondLabel = "秒"

    private var global = GLOBAL_LOCAL

    private var layoutResId = R.layout.dt_layout_date_picker

    private var controller: BaseDateTimeController? = null

    private var textBold = true
    private var selectedTextBold = true


    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : this(context, attrs)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val attributesArray = context.obtainStyledAttributes(attrs, R.styleable.DateTimePicker)
        showLabel = attributesArray.getBoolean(R.styleable.DateTimePicker_dt_showLabel, true)
        themeColor = attributesArray.getColor(
            R.styleable.DateTimePicker_dt_themeColor,
            ContextCompat.getColor(context, R.color.colorAccent)
        )
        textColor = attributesArray.getColor(
            R.styleable.DateTimePicker_dt_textColor,
            ContextCompat.getColor(context, R.color.colorTextGray)
        )
        dividerColor= attributesArray.getColor(
            R.styleable.DateTimePicker_dt_dividerColor,
            ContextCompat.getColor(context, R.color.colorDivider)
        )
        selectTextSize =
            context.px2dip(
                attributesArray.getDimensionPixelSize(
                    R.styleable.DateTimePicker_dt_selectTextSize,
                    context.dip2px(0f)
                ).toFloat()
            )
        normalTextSize =
            context.px2dip(
                attributesArray.getDimensionPixelSize(
                    R.styleable.DateTimePicker_dt_normalTextSize,
                    context.dip2px(0f)
                ).toFloat()
            )

        layoutResId = attributesArray.getResourceId(
            R.styleable.DateTimePicker_dt_layout,
            R.layout.dt_layout_date_picker
        )
        textBold = attributesArray.getBoolean(
            R.styleable.DateTimePicker_dt_textBold, textBold
        )
        selectedTextBold = attributesArray.getBoolean(
            R.styleable.DateTimePicker_dt_selectedTextBold, selectedTextBold
        )

        attributesArray.recycle()
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        removeAllViews()
        try {
            if (!DateTimeConfig.showChina(global) && layoutResId == R.layout.dt_layout_date_picker)
                View.inflate(context, R.layout.dt_layout_date_picker_globalization, this)
            else
                View.inflate(context, layoutResId, this)
        } catch (e: Exception) {
            throw Exception("layoutResId is it right or not?")
        }

        mYearSpinner = findViewById(R.id.np_datetime_year)
        if (mYearSpinner == null)
            mYearSpinner = findViewWithTag("np_datetime_year")
        mMonthSpinner = findViewById(R.id.np_datetime_month)
        if (mMonthSpinner == null)
            mMonthSpinner = findViewWithTag("np_datetime_month")
        mDaySpinner = findViewById(R.id.np_datetime_day)
        if (mDaySpinner == null)
            mDaySpinner = findViewWithTag("np_datetime_day")
        mHourSpinner = findViewById(R.id.np_datetime_hour)
        if (mHourSpinner == null)
            mHourSpinner = findViewWithTag("np_datetime_hour")
        mMinuteSpinner = findViewById(R.id.np_datetime_minute)
        if (mMinuteSpinner == null)
            mMinuteSpinner = findViewWithTag("np_datetime_minute")
        mSecondSpinner = findViewById(R.id.np_datetime_second)
        if (mSecondSpinner == null)
            mSecondSpinner = findViewWithTag("np_datetime_second")

        setThemeColor(themeColor)
        setTextSize(normalTextSize, selectTextSize)
        showLabel(showLabel)
        setDisplayType(displayType)
        setSelectedTextBold(selectedTextBold)
        setTextBold(textBold)
        setTextColor(textColor)
        setDividerColor(dividerColor)


        //绑定控制器
        bindController(controller ?: DateTimeController())

    }


    /**
     * 绑定控制器
     */
    fun bindController(controller: BaseDateTimeController?) {
        this.controller = controller
        if (this.controller == null)
            this.controller = DateTimeController().bindPicker(YEAR, mYearSpinner)
                .bindPicker(MONTH, mMonthSpinner)
                .bindPicker(DAY, mDaySpinner).bindPicker(HOUR, mHourSpinner)
                .bindPicker(MIN, mMinuteSpinner).bindPicker(SECOND, mSecondSpinner)
                .bindGlobal(global)?.build()
        else
            this.controller?.bindPicker(YEAR, mYearSpinner)
                ?.bindPicker(MONTH, mMonthSpinner)
                ?.bindPicker(DAY, mDaySpinner)?.bindPicker(HOUR, mHourSpinner)
                ?.bindPicker(MIN, mMinuteSpinner)?.bindPicker(SECOND, mSecondSpinner)
                ?.bindGlobal(global)?.build()
    }


    /**
     * 设置国际化日期格式显示
     * @param global : DateTimeConfig.GLOBAL_LOCAL
     *                 DateTimeConfig.GLOBAL_CHINA
     *                 DateTimeConfig.GLOBAL_US
     */
    fun setGlobal(global: Int) {
        this.global = global
        init()
    }

    /**
     * 设置自定义layout
     */
    fun setLayout(@NotNull layout: Int) {
        if (layout == 0)
            return
        layoutResId = layout
        init()
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
            mYearSpinner?.visibility = View.GONE
        }

        if (!displayType.contains(MONTH)) {
            mMonthSpinner?.visibility = View.GONE
        }

        if (!displayType.contains(DAY)) {
            mDaySpinner?.visibility = View.GONE
        }

        if (!displayType.contains(HOUR)) {
            mHourSpinner?.visibility = View.GONE
        }

        if (!displayType.contains(MIN)) {
            mMinuteSpinner?.visibility = View.GONE
        }

        if (!displayType.contains(SECOND)) {
            mSecondSpinner?.visibility = View.GONE
        }
    }

    /**
     * 是否显示label
     *
     * @param b
     */
    fun showLabel(b: Boolean) {
        showLabel = b
        if (b) {
            mYearSpinner?.label = yearLabel
            mMonthSpinner?.label = monthLabel
            mDaySpinner?.label = dayLabel
            mHourSpinner?.label = hourLabel
            mMinuteSpinner?.label = minLabel
            mSecondSpinner?.label = secondLabel
        } else {
            mYearSpinner?.label = ""
            mMonthSpinner?.label = ""
            mDaySpinner?.label = ""
            mHourSpinner?.label = ""
            mMinuteSpinner?.label = ""
            mSecondSpinner?.label = ""
        }
    }

    /**
     * 主题颜色
     *
     * @param color
     */
    fun setThemeColor(@ColorInt color: Int) {
        if (color == 0) return
        themeColor = color
        mYearSpinner?.selectedTextColor = themeColor
        mMonthSpinner?.selectedTextColor = themeColor
        mDaySpinner?.selectedTextColor = themeColor
        mHourSpinner?.selectedTextColor = themeColor
        mMinuteSpinner?.selectedTextColor = themeColor
        mSecondSpinner?.selectedTextColor = themeColor

    }

    /**
     * 设置选择器字体颜色
     */
    fun setTextColor(@ColorInt color: Int) {
        if (color == 0) return
        textColor = color
        mYearSpinner?.textColor = textColor
        mMonthSpinner?.textColor = textColor
        mDaySpinner?.textColor = textColor
        mHourSpinner?.textColor = textColor
        mMinuteSpinner?.textColor = textColor
        mSecondSpinner?.textColor = textColor
    }

    /**
     * 设置选择器分割线颜色
     */
    fun setDividerColor(@ColorInt color: Int) {
        if (color == 0) return
        dividerColor = color
        mYearSpinner?.dividerColor = color
        mMonthSpinner?.dividerColor = color
        mDaySpinner?.dividerColor = color
        mHourSpinner?.dividerColor = color
        mMinuteSpinner?.dividerColor = color
        mSecondSpinner?.dividerColor = color
    }

    /**
     * 字体大小
     *
     * @param normal
     * @param select
     */
    fun setTextSize(@Dimension normal: Int, @Dimension select: Int) {
        if (normal == 0) return
        if (select == 0) return
        var textSize = context!!.dip2px(select.toFloat())
        var normalTextSize = context!!.dip2px(normal.toFloat())
        mYearSpinner?.textSize = normalTextSize.toFloat()
        mMonthSpinner?.textSize = normalTextSize.toFloat()
        mDaySpinner?.textSize = normalTextSize.toFloat()
        mHourSpinner?.textSize = normalTextSize.toFloat()
        mMinuteSpinner?.textSize = normalTextSize.toFloat()
        mSecondSpinner?.textSize = normalTextSize.toFloat()

        mYearSpinner?.selectedTextSize = textSize.toFloat()
        mMonthSpinner?.selectedTextSize = textSize.toFloat()
        mDaySpinner?.selectedTextSize = textSize.toFloat()
        mHourSpinner?.selectedTextSize = textSize.toFloat()
        mMinuteSpinner?.selectedTextSize = textSize.toFloat()
        mSecondSpinner?.selectedTextSize = textSize.toFloat()

    }

    /**
     * 设置标签文字
     * @param year 年标签
     * @param month 月标签
     * @param day 日标签
     * @param hour 时标签
     * @param min 分标签
     *  @param min 秒标签
     */
    fun setLabelText(
        year: String = yearLabel,
        month: String = monthLabel,
        day: String = dayLabel,
        hour: String = hourLabel,
        min: String = minLabel,
        second: String = secondLabel
    ) {
        this.yearLabel = year
        this.monthLabel = month
        this.dayLabel = day
        this.hourLabel = hour
        this.minLabel = min
        this.secondLabel = second
        showLabel(showLabel)
    }


    /**
     * 设置是否Picker循环滚动
     * @param types 需要设置的Picker类型（DateTimeConfig-> YEAR,MONTH,DAY,HOUR,MIN,SECOND）
     * @param wrapSelector 是否循环滚动
     */
    fun setWrapSelectorWheel(vararg types: Int, wrapSelector: Boolean) {
        setWrapSelectorWheel(types.toMutableList(), wrapSelector)
    }

    /**
     * 设置是否Picker循环滚动
     * @param wrapSelector 是否循环滚动
     */
    fun setWrapSelectorWheel(wrapSelector: Boolean) {
        setWrapSelectorWheel(null, wrapSelector)
    }

    /**
     * 获取类型对应的NumberPicker
     * @param displayType 类型
     */
    fun getPicker(displayType: Int): NumberPicker? {
        return when (displayType) {
            YEAR -> mYearSpinner
            MONTH -> mMonthSpinner
            DAY -> mDaySpinner
            HOUR -> mHourSpinner
            MIN -> mMinuteSpinner
            SECOND -> mSecondSpinner
            else -> null
        }
    }


    /**
     * 设置选择器字体是否加粗
     */
    fun setTextBold(textBold: Boolean) {
        this.textBold = textBold
        mYearSpinner?.isTextBold = textBold
        mMonthSpinner?.isTextBold = textBold
        mDaySpinner?.isTextBold = textBold
        mHourSpinner?.isTextBold = textBold
        mMinuteSpinner?.isTextBold = textBold
        mSecondSpinner?.isTextBold = textBold
    }


    /**
     * 设置选择器选中字体是否加粗
     */
    fun setSelectedTextBold(selectedTextBold: Boolean) {
        this.selectedTextBold = selectedTextBold
        mYearSpinner?.isSelectedTextBold = selectedTextBold
        mMonthSpinner?.isSelectedTextBold = selectedTextBold
        mDaySpinner?.isSelectedTextBold = selectedTextBold
        mHourSpinner?.isSelectedTextBold = selectedTextBold
        mMinuteSpinner?.isSelectedTextBold = selectedTextBold
        mSecondSpinner?.isSelectedTextBold = selectedTextBold
    }


    override fun setDefaultMillisecond(time: Long) {
        controller?.setDefaultMillisecond(time)
    }

    override fun setMinMillisecond(time: Long) {
        controller?.setMinMillisecond(time)
    }

    override fun setMaxMillisecond(time: Long) {
        controller?.setMaxMillisecond(time)
    }

    override fun setWrapSelectorWheel(types: MutableList<Int>?, wrapSelector: Boolean) {
        controller?.setWrapSelectorWheel(types, wrapSelector)
    }

    override fun setOnDateTimeChangedListener(callback: ((Long) -> Unit)?) {
        controller?.setOnDateTimeChangedListener(callback)
    }

    override fun getMillisecond(): Long {
        return controller?.getMillisecond() ?: 0L
    }


}
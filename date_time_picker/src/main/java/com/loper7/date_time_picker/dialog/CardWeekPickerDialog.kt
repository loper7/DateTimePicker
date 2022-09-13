package com.loper7.date_time_picker.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.loper7.date_time_picker.R
import com.loper7.date_time_picker.ext.*
import com.loper7.date_time_picker.ext.getMaxWeekOfYear
import com.loper7.date_time_picker.ext.getWeekOfYear
import com.loper7.date_time_picker.ext.toFormatList
import com.loper7.date_time_picker.number_picker.NumberPicker
import com.loper7.date_time_picker.utils.StringUtils
import com.loper7.tab_expand.ext.dip2px
import java.util.*


/**
 * 卡片 周视图 选择器
 */
open class CardWeekPickerDialog(context: Context) : BottomSheetDialog(context),
    View.OnClickListener {
    companion object {
        const val CARD = 0 //卡片
        const val CUBE = 1 //方形
        const val STACK = 2 //顶部圆角

        fun builder(context: Context): Builder {
            return lazy { Builder(context) }.value
        }
    }

    private var builder: Builder? = null

    private val np_week by lazy { delegate.findViewById<NumberPicker>(R.id.np_week) }
    private val tv_cancel by lazy { delegate.findViewById<TextView>(R.id.dialog_cancel) }
    private val tv_submit by lazy { delegate.findViewById<TextView>(R.id.dialog_submit) }
    private val tv_title by lazy { delegate.findViewById<TextView>(R.id.tv_title) }
    private val linear_bg by lazy { delegate.findViewById<LinearLayout>(R.id.linear_bg) }
    private val divider_bottom by lazy { delegate.findViewById<View>(R.id.divider_bottom) }
    private val divider_line by lazy { delegate.findViewById<View>(R.id.dialog_select_border) }

    private var mBehavior: BottomSheetBehavior<FrameLayout>? = null

    private val calendar by lazy { Calendar.getInstance() }
    private var weeksData = mutableListOf<MutableList<Long>>()


    constructor(context: Context, builder: Builder) : this(context) {
        this.builder = builder
    }

    init {
        builder = builder(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dt_dialog_week_picker)
        super.onCreate(savedInstanceState)

        val bottomSheet = delegate.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        bottomSheet!!.setBackgroundColor(Color.TRANSPARENT)

        mBehavior = BottomSheetBehavior.from(bottomSheet)

        weeksData = calendar.getWeeks()
        builder?.apply {
            weeksData =
                calendar.getWeeks(startMillisecond, endMillisecond, startContain, endContain)
            //背景模式
            if (model != 0) {
                val parmas = LinearLayout.LayoutParams(linear_bg!!.layoutParams)
                when (model) {
                    CARD -> {
                        parmas.setMargins(
                            context.dip2px(12f),
                            context.dip2px(12f),
                            context.dip2px(12f),
                            context.dip2px(12f)
                        )
                        linear_bg!!.layoutParams = parmas
                        linear_bg!!.setBackgroundResource(R.drawable.shape_bg_round_white_5)
                    }
                    CUBE -> {
                        parmas.setMargins(0, 0, 0, 0)
                        linear_bg!!.layoutParams = parmas
                        linear_bg!!.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorTextWhite
                            )
                        )
                    }
                    STACK -> {
                        parmas.setMargins(0, 0, 0, 0)
                        linear_bg!!.layoutParams = parmas
                        linear_bg!!.setBackgroundResource(R.drawable.shape_bg_top_round_white_15)
                    }
                    else -> {
                        parmas.setMargins(0, 0, 0, 0)
                        linear_bg!!.layoutParams = parmas
                        linear_bg!!.setBackgroundResource(model)
                    }
                }
            }

            //标题
            if (titleValue.isNullOrEmpty()) {
                tv_title!!.visibility = View.GONE
            } else {
                tv_title?.text = titleValue
                tv_title?.visibility = View.VISIBLE
            }

            //按钮
            tv_cancel?.text = cancelText
            tv_submit?.text = chooseText

            //主题
            if (themeColor != 0) {
                tv_submit!!.setTextColor(themeColor)
                np_week!!.selectedTextColor = themeColor
            }

            if (builder!!.assistColor != 0) {
                tv_title?.setTextColor(builder!!.assistColor)
                tv_cancel?.setTextColor(builder!!.assistColor)
                np_week!!.textColor = builder!!.assistColor


            }
            if (builder!!.dividerColor != 0) {
                divider_bottom?.setBackgroundColor(builder!!.dividerColor)
                divider_line?.setBackgroundColor(builder!!.dividerColor)
                np_week!!.dividerColor = builder!!.dividerColor
            }

        }

        //视图周
        np_week?.apply {
            if (weeksData.isNullOrEmpty())
                return

            minValue = 1
            maxValue = weeksData.size
            value = weeksData.index(builder?.defaultMillisecond) + 1
            isFocusable = true
            isFocusableInTouchMode = true
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS //设置NumberPicker不可编辑
            wrapSelectorWheel = builder?.wrapSelectorWheel ?: true

            formatter =
                builder?.formatter?.invoke(weeksData) ?: NumberPicker.Formatter { value: Int ->
                    var weekData = weeksData[value - 1].toFormatList("yyyy/MM/dd")
                    var str = "${weekData.first()}  -  ${weekData.last()}"
                    str
                }
        }

        tv_cancel!!.setOnClickListener(this)
        tv_submit!!.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onClick(v: View) {
        this.dismiss()
        when (v.id) {
            R.id.dialog_submit -> {
                np_week?.apply {
                    builder?.onChooseListener?.invoke(weeksData[value - 1], formatter.format(value))
                }
            }
            R.id.dialog_cancel -> {
                builder?.onCancelListener?.invoke()
            }
        }
        this.dismiss()
    }


    class Builder(private var context: Context) {

        @JvmField
        var cancelText: String = "取消"

        @JvmField
        var chooseText: String = "确定"

        @JvmField
        var titleValue: String? = null

        @JvmField
        var model: Int = CARD

        @JvmField
        var themeColor: Int = 0

        @JvmField
        var assistColor: Int = 0

        @JvmField
        var dividerColor: Int = 0

        @JvmField
        var wrapSelectorWheel: Boolean = true

        @JvmField
        var onChooseListener: ((MutableList<Long>, String) -> Unit)? = null

        @JvmField
        var onCancelListener: (() -> Unit)? = null

        @JvmField
        var defaultMillisecond: Long = 0

        @JvmField
        var startMillisecond: Long = 0

        @JvmField
        var startContain: Boolean = true

        @JvmField
        var endMillisecond: Long = 0

        @JvmField
        var endContain: Boolean = true

        @JvmField
        var formatter: ((MutableList<MutableList<Long>>) -> NumberPicker.Formatter?)? = null

        /**
         * 设置标题
         * @param value 标题
         * @return Builder
         */
        fun setTitle(value: String): Builder {
            this.titleValue = value
            return this
        }

        /**
         * 显示模式
         * @param model  CARD,CUBE,STACK
         * @return Builder
         */
        fun setBackGroundModel(model: Int): Builder {
            this.model = model
            return this
        }

        /**
         * 设置主题颜色
         * @param themeColor 主题颜色
         * @return Builder
         */
        fun setThemeColor(@ColorInt themeColor: Int): Builder {
            this.themeColor = themeColor
            return this
        }

        /**
         *设置是否循环滚动
         * @return Builder
         */
        fun setWrapSelectorWheel(wrapSelector: Boolean): Builder {
            this.wrapSelectorWheel = wrapSelector
            return this
        }

        /**
         * 设置默认选中周次所在的任意时间
         * @param millisecond 默认时间
         * @return Builder
         */
        fun setDefaultMillisecond(millisecond: Long): Builder {
            this.defaultMillisecond = millisecond
            return this
        }

        /**
         * 设置起始周所在时间
         * @param millisecond 起始时间
         * @param contain 起始周是否包含起始时间
         * @return Builder
         */
        fun setStartMillisecond(millisecond: Long, contain: Boolean = true): Builder {
            this.startMillisecond = millisecond
            this.startContain = contain
            return this
        }

        /**
         * 设置结束周所在时间
         * @param millisecond 结束时间
         * @param contain 结束周是否包含结束时间
         * @return Builder
         */
        fun setEndMillisecond(millisecond: Long, contain: Boolean = true): Builder {
            this.endMillisecond = millisecond
            this.endContain = contain
            return this
        }

        /**
         * 设置格式化
         * @param datas 数据
         * @return Builder
         */
        fun setFormatter(formatter: (MutableList<MutableList<Long>>) -> NumberPicker.Formatter?): Builder {
            this.formatter = formatter
            return this
        }

        /**
         * 绑定选择监听
         * @param text 按钮文字
         * @param listener 选择监听函数 MutableList<Long> 选择周次所包含的天时间戳 String 周format字符串
         * @return Builder
         */
        fun setOnChoose(
            text: String = "确定",
            listener: ((MutableList<Long>, String) -> Unit)? = null
        ): Builder {
            this.onChooseListener = listener
            this.chooseText = text
            return this
        }

        /**
         * 绑定取消监听
         * @param text 按钮文字
         * @param listener 取消监听函数
         * @return Builder
         */
        fun setOnCancel(text: String = "取消", listener: (() -> Unit)? = null): Builder {
            this.onCancelListener = listener
            this.cancelText = text
            return this
        }

        /**
         * 这只dialog内辅助文字的颜色
         * @return Builder
         */
        fun setAssistColor(@ColorInt value: Int): Builder {
            this.assistColor = value
            return this
        }

        /**
         * 这只dialog内分割线颜色
         * @return Builder
         */
        fun setDividerColor(@ColorInt value: Int): Builder {
            this.dividerColor = value
            return this
        }

        fun build(): CardWeekPickerDialog {
            return CardWeekPickerDialog(context, this)
        }
    }

}
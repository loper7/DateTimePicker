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
import com.loper7.date_time_picker.ext.getMaxWeekOfYear
import com.loper7.date_time_picker.ext.getWeekOfYear
import com.loper7.date_time_picker.ext.getWeeksOfYear
import com.loper7.date_time_picker.ext.toFormatList
import com.loper7.date_time_picker.number_picker.NumberPicker
import com.loper7.tab_expand.ext.dip2px
import java.util.*


/**
 * 卡片 周视图 选择器
 */
class CardWeekPickerDialog(context: Context) : BottomSheetDialog(context), View.OnClickListener {
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

    private var mBehavior: BottomSheetBehavior<FrameLayout>? = null


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


        //视图周
        np_week?.apply {
            val calendar = Calendar.getInstance()
            var datas = calendar.getWeeksOfYear()
            minValue = 1
            maxValue = calendar.getMaxWeekOfYear()
            value = calendar.getWeekOfYear(Calendar.getInstance().time)
            isFocusable = true
            isFocusableInTouchMode = true
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS //设置NumberPicker不可编辑

            formatter = NumberPicker.Formatter { value: Int ->
                var weekData = datas[value-1].toFormatList("MM/dd")
                var str = "${weekData.first()}  -  ${weekData.last()}"
                str
            }
        }


        //背景模式
        if (builder!!.model != 0) {
            val parmas = LinearLayout.LayoutParams(linear_bg!!.layoutParams)
            when (builder!!.model) {
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
                    linear_bg!!.setBackgroundResource(builder!!.model)
                }
            }
        }

        //标题
        if (builder!!.titleValue.isNullOrEmpty()) {
            tv_title!!.visibility = View.GONE
        } else {
            tv_title?.text = builder!!.titleValue
            tv_title?.visibility = View.VISIBLE
        }

        //按钮
        tv_cancel?.text = builder!!.cancelText
        tv_submit?.text = builder!!.chooseText


        if (builder!!.themeColor != 0) {
            tv_submit!!.setTextColor(builder!!.themeColor)
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
        var wrapSelectorWheel: Boolean = true

        @JvmField
        var onChooseListener: ((Long) -> Unit)? = null

        @JvmField
        var onCancelListener: (() -> Unit)? = null

        /**
         * 设置标题
         */
        fun setTitle(value: String): Builder {
            this.titleValue = value
            return this
        }

        /**
         * 显示模式
         */
        fun setBackGroundModel(model: Int): Builder {
            this.model = model
            return this
        }

        /**
         * 设置主题颜色
         */
        fun setThemeColor(@ColorInt themeColor: Int): Builder {
            this.themeColor = themeColor
            return this
        }

        /**
         * 设置是否循环滚动
         */
        fun setWrapSelectorWheel(wrapSelector: Boolean): Builder {
            this.wrapSelectorWheel = wrapSelector
            return this
        }


        /**
         * 绑定选择监听
         */
        fun setOnChoose(text: String = "确定", listener: ((Long) -> Unit)? = null): Builder {
            this.onChooseListener = listener
            this.chooseText = text
            return this
        }

        /**
         * 绑定取消监听
         */
        fun setOnCancel(text: String = "取消", listener: (() -> Unit)? = null): Builder {
            this.onCancelListener = listener
            this.cancelText = text
            return this
        }

        fun build(): CardWeekPickerDialog {
            return CardWeekPickerDialog(context, this)
        }
    }

}
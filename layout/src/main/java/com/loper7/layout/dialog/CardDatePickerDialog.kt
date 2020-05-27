package com.loper7.layout.dialog

import android.R.attr.fillColor
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.loper7.layout.DateTimePicker
import com.loper7.layout.R
import com.loper7.layout.StringUtils
import java.util.*


/**
 *
 * @ProjectName:    DatePicker
 * @Package:        com.loper7.layout.dialog
 * @ClassName:      DateDateDateTimePickerDialog
 * @CreateDate:     2020/3/3 0003 11:38
 * @Description:    java类作用描述
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
class CardDatePickerDialog(context: Context) : BottomSheetDialog(context), View.OnClickListener {
    companion object {
        val CARD = 0//卡片
        val CUBE = 1//方形
        val STACK = 2//顶部圆角
    }

    private var listener: OnChooseListener? = null


    private var tv_cancel: TextView? = null
    private var tv_submit: TextView? = null
    private var tv_title: TextView? = null
    private var tv_choose_date: TextView? = null
    private var btn_today: TextView? = null
    private var datePicker: DateTimePicker? = null
    private var tv_go_back: TextView? = null
    private var linear_now: LinearLayout? = null
    private var linear_bg: LinearLayout? = null

    private var millisecond: Long = 0

    private var backNow: Boolean = true
    private var focusDateInfo: Boolean = true
    private var dateLabel: Boolean = true
    private var titleValue: String? = null
    private var defaultMillisecond: Long = 0
    private var minTime: Long = 0
    private var maxTime: Long = 0
    private var displayTypes: IntArray? = null
    private var model: Int = CARD
    private var themeColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_time_picker)
        super.onCreate(savedInstanceState)

        val bottomSheet = delegate.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        bottomSheet!!.setBackgroundColor(Color.TRANSPARENT)


        tv_cancel = findViewById(R.id.dialog_cancel)
        tv_submit = findViewById(R.id.dialog_submit)
        datePicker = findViewById(R.id.dateTimePicker)
        tv_title = findViewById(R.id.tv_title)
        btn_today = findViewById(R.id.btn_today)
        tv_choose_date = findViewById(R.id.tv_choose_date)
        tv_go_back = findViewById(R.id.tv_go_back)
        linear_now = findViewById(R.id.linear_now)
        linear_bg = findViewById(R.id.linear_bg)


        //背景模式
        if (model != 0) {
            var parmas = LinearLayout.LayoutParams(linear_bg!!.layoutParams)
            when (model) {
                CARD -> {
                    parmas.setMargins(dip2px(12f), dip2px(12f), dip2px(12f), dip2px(12f))
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
        if (!TextUtils.isEmpty(titleValue))
            tv_title!!.text = titleValue

        //显示标签
        datePicker!!.showLabel(dateLabel)

        //显示模式
        if (displayTypes == null) {
            displayTypes = intArrayOf(
                DateTimePicker.YEAR,
                DateTimePicker.MONTH,
                DateTimePicker.DAY,
                DateTimePicker.HOUR,
                DateTimePicker.MIN
            )
        }

        datePicker!!.setDisplayType(displayTypes)
        //回到当前时间展示
        if (displayTypes != null) {
            var year_month_day_hour = 0
            for (i in displayTypes!!) {
                if (i == DateTimePicker.YEAR && year_month_day_hour <= 0) {
                    year_month_day_hour = 0
                    tv_go_back!!.text = "回到今年"
                    btn_today!!.text = "今"
                }
                if (i == DateTimePicker.MONTH && year_month_day_hour <= 1) {
                    year_month_day_hour = 1
                    tv_go_back!!.text = "回到本月"
                    btn_today!!.text = "本"
                }
                if (i == DateTimePicker.DAY && year_month_day_hour <= 2) {
                    year_month_day_hour = 2
                    tv_go_back!!.text = "回到今日"
                    btn_today!!.text = "今"
                }
                if ((i == DateTimePicker.HOUR || i == DateTimePicker.MIN) && year_month_day_hour <= 3) {
                    year_month_day_hour = 3
                    tv_go_back!!.text = "回到此刻"
                    btn_today!!.text = "此"
                }
            }

        }
        linear_now!!.visibility = if (backNow) View.VISIBLE else View.GONE
        tv_choose_date!!.visibility = if (focusDateInfo) View.VISIBLE else View.GONE

        //设置最小时间
        datePicker!!.setMinMillisecond(minTime)
        //设置最大时间
        datePicker!!.setMaxMillisecond(maxTime)

        datePicker!!.setDefaultMillisecond(defaultMillisecond)


        datePicker!!.setTextSize(15)
        if (themeColor != 0) {
            datePicker!!.setThemeColor(themeColor)
            tv_submit!!.setTextColor(themeColor)

            val gd = GradientDrawable()
            gd.setColor(themeColor)
            gd.cornerRadius = dip2px(60f).toFloat()
            btn_today!!.background = gd
        }

        tv_cancel!!.setOnClickListener(this)
        tv_submit!!.setOnClickListener(this)
        btn_today!!.setOnClickListener(this)

        datePicker!!.setOnDateTimeChangedListener { _, millisecond, _, _, _, _, minute ->
            this.millisecond = millisecond
            tv_choose_date!!.text =
                (StringUtils.conversionTime(millisecond, "yyyy年MM月dd日 ") + StringUtils.getWeek(
                    millisecond
                ))
        }
    }

    override fun onClick(v: View) {
        this.dismiss()
        if (listener == null)
            return
        when (v.id) {

            R.id.btn_today -> {
                listener?.onChoose(Calendar.getInstance().timeInMillis)
            }
            R.id.dialog_submit -> {
                listener?.onChoose(millisecond)
            }
        }

        this.dismiss()
    }

    /**
     * 设置标题
     */
    public fun setTitle(value: String): CardDatePickerDialog {
        this.titleValue = value
        return this;
    }

    /**
     * 设置默认时间
     */
    public fun setDefaultTime(millisecond: Long): CardDatePickerDialog {
        this.defaultMillisecond = millisecond
        return this
    }

    /**
     * 设置范围最小值
     */
    public fun setMinTime(millisecond: Long): CardDatePickerDialog {
        this.minTime = millisecond
        return this
    }

    public fun setMaxTime(millisecond: Long): CardDatePickerDialog {
        this.maxTime = millisecond
        return this
    }


    /**
     * 是否显示回到当前
     */
    public fun showBackNow(b: Boolean): CardDatePickerDialog {
        this.backNow = b
        return this
    }

    /**
     * 是否显示单位标签
     */
    public fun showFocusDateInfo(b: Boolean): CardDatePickerDialog {
        this.focusDateInfo = b
        return this
    }


    /**
     * 是否显示选中日期信息
     */
    public fun showDateLabel(b: Boolean): CardDatePickerDialog {
        this.dateLabel = b
        return this
    }

    /**
     * 设置显示值
     */
    public fun setDisplayType(vararg types: Int): CardDatePickerDialog {
        this.displayTypes = types
        return this
    }

    /**
     * 设置显示值
     */
    public fun setDisplayType(types: MutableList<Int>): CardDatePickerDialog {
        this.displayTypes = types.toIntArray()
        return this
    }

    /**
     * 显示模式
     */
    public fun setBackGroundModel(model: Int): CardDatePickerDialog {
        this.model = model
        return this
    }

    public fun setThemeColor(@ColorInt themeColor: Int): CardDatePickerDialog {
        this.themeColor = themeColor
        return this
    }

    /**
     * 绑定监听
     */
    public fun setOnChooseListener(listener: OnChooseListener): CardDatePickerDialog {
        this.listener = listener
        return this
    }

    interface OnChooseListener {
        fun onChoose(millisecond: Long)
    }


    /**
     * 根据手机的分辨率dp 转成px(像素)
     */
    private fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率px(像素) 转成dp
     */
    private fun px2dip(pxValue: Float): Int {
        val scale = getContext().resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

}
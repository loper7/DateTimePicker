package com.loper7.layout.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
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
class DateTimePickerDialog(context: Context) : BottomSheetDialog(context),View.OnClickListener {

    private var listener: OnHandleListener? = null


    private var tv_cancel: TextView? = null
    private var tv_submit: TextView? = null
    private var tv_title: TextView? = null
    private var tv_choose_date: TextView? = null
    private var btn_today: TextView? = null
    private var datePicker: DateTimePicker? = null
    private var tv_go_back: TextView? = null
    private var linear_now: LinearLayout? = null

    private var millisecond: Long = 0
    private var backNow: Boolean = true


    private var titleValue: String? = null
    private var defaultMillisecond: Long = 0
    private var minTime: Long = 0
    private var displayTypes: IntArray? = null

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

        //设置一些初始值
        //标题
        if (!TextUtils.isEmpty(titleValue))
            tv_title!!.text = titleValue
        //显示模式
        if (displayTypes != null)
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

        //设置最小时间
        datePicker!!.setMinMillisecond(minTime)

        if (defaultMillisecond != 0L)
            datePicker!!.setDefaultMillisecond(defaultMillisecond)

        tv_cancel!!.setOnClickListener(this)
        tv_submit!!.setOnClickListener(this)
        btn_today!!.setOnClickListener(this)

        datePicker!!.setOnDateTimeChangedListener { view, millisecond, year, month, day, hour, minute ->
            this.millisecond = millisecond
            tv_choose_date!!.text =
                (StringUtils.conversionTime(millisecond, "yyyy年MM月dd日 ") + StringUtils.getWeek(millisecond))
        }


    }

    override fun onClick(v: View) {
        this.dismiss()
        if (listener == null)
            return
        when (v.id) {

            R.id.btn_today -> {
                listener?.onChooseDate(Calendar.getInstance().timeInMillis)
            }
            R.id.dialog_submit -> {
                listener?.onChooseDate(millisecond)
            }
        }

        this.dismiss()
    }


    public fun setTitle(value: String):DateTimePickerDialog {
        this.titleValue = value
        return this
    }


    /**
     * 设置默认时间
     */
    public fun setDefaultTime(millisecond: Long):DateTimePickerDialog {
        this.defaultMillisecond = millisecond
        return this
    }

    /**
     * 设置范围最小值
     */
    public fun setMinTime(millisecond: Long): DateTimePickerDialog {
        this.minTime = millisecond
        return this
    }

    /**
     * 是否显示回到当前
     */
    public fun showBackNow(b: Boolean): DateTimePickerDialog {
        this.backNow = b
        return this
    }

    /**
     * 设置显示值
     */
    public fun setDisplayType(vararg types: Int): DateTimePickerDialog {
        this.displayTypes = types
        return this
    }

    /**
     * 绑定监听
     */
    public fun setOnHandleListener(listener: OnHandleListener): DateTimePickerDialog {
        this.listener = listener
        return this
    }

    interface OnHandleListener {
        fun onChooseDate(millisecond: Long)
    }
}
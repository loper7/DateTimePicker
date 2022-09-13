package com.loper7.datepicker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.loper7.date_time_picker.dialog.CardWeekPickerDialog
import com.loper7.date_time_picker.number_picker.NumberPicker
import kotlinx.android.synthetic.main.activity_week_picker_example.*

class WeekPickerExampleActivity : AppCompatActivity() {


    private var maxDate: Long = 0
    private var minDate: Long = 0
    private var defaultDate: Long = 0

    private lateinit var context: Context

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_picker_example)
        context = this


        tvMaxDate.setOnClickListener {
            CardDatePickerDialog.builder(this)
                .setTitle("SET MAX DATE")
                .setDisplayType(DateTimeConfig.YEAR,DateTimeConfig.MONTH,DateTimeConfig.DAY)
                .setDefaultTime(maxDate)
                .setOnChoose {
                    maxDate = it
                    tvMaxDate.text = StringUtils.conversionTime(it, "yyyy-MM-dd")
                }.build().show()

        }


        btnClearMax.setOnClickListener {
            maxDate = 0L
            tvMaxDate.text = ""
        }

        tvMinDate.setOnClickListener {
            CardDatePickerDialog.builder(this)
                .setTitle("SET MIN DATE")
                .setDisplayType(DateTimeConfig.YEAR,DateTimeConfig.MONTH,DateTimeConfig.DAY)
                .setDefaultTime(minDate)
                .setOnChoose {
                    minDate = it
                    tvMinDate.text = StringUtils.conversionTime(it, "yyyy-MM-dd")
                }.build().show()
        }

        btnClearMin.setOnClickListener {
            minDate = 0L
            tvMinDate.text = ""
        }

        tvDefaultDate.setOnClickListener {
            CardDatePickerDialog.builder(this)
                .setTitle("SET DEFAULT DATE")
                .setDefaultTime(defaultDate)
                .setDisplayType(DateTimeConfig.YEAR,DateTimeConfig.MONTH,DateTimeConfig.DAY)
                .setOnChoose {
                    defaultDate = it
                    tvDefaultDate.text =
                        StringUtils.conversionTime(it, "yyyy-MM-dd")
                }.build().show()
        }

        btnClearDefault.setOnClickListener {
            defaultDate = 0L
            tvDefaultDate.text = ""
        }

        btnCardDialogShow.setOnClickListener {

            var model = CardDatePickerDialog.CARD
            if (radioModelCard.isChecked)
                model = CardDatePickerDialog.CARD
            if (radioModelCube.isChecked)
                model = CardDatePickerDialog.CUBE
            if (radioModelStack.isChecked)
                model = CardDatePickerDialog.STACK
            if (radioModelCustom.isChecked)
                model = R.drawable.shape_bg_dialog_custom


            CardWeekPickerDialog.builder(context)
                .setTitle("WEEK PICKER")
                .setBackGroundModel(model)
                .setWrapSelectorWheel(false)
                .setDefaultMillisecond(defaultDate)
                .setStartMillisecond(minDate)
                .setEndMillisecond(maxDate)
                .setThemeColor(if (model == R.drawable.shape_bg_dialog_custom) Color.parseColor("#FF8000") else 0)
//                .setBackGroundModel(R.drawable.shape_bg_dialog_dark)
//                .setAssistColor(Color.parseColor("#DDFFFFFF"))
//                .setDividerColor(Color.parseColor("#222222"))
                .setFormatter {
                    NumberPicker.Formatter { value: Int ->
                        var weekData = it[value - 1].toFormatList("MM月dd日")
                        var str = "从${weekData.first()}  开始到  ${weekData.last()}结束"
                        str
                    }
                }
                .setOnChoose("选择") {weekData,formatValue ->
                    btnCardDialogShow.text = formatValue
                }
                .setOnCancel("关闭") {
                }.build().show()
        }
    }
}

/**
 * 将时间戳集合格式化为指定日期格式的集合
 * @return MutableList<String> [2021-09-09,2021--09-10,...]
 */
internal fun MutableList<Long>.toFormatList(format: String = "yyyy-MM-dd"): MutableList<String> {
    var formatList = mutableListOf<String>()
    for (i in this) {
        formatList.add(StringUtils.conversionTime(i, format))
    }
    return formatList
}

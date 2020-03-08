package com.loper7.datepicker

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.loper7.layout.DateTimePicker
import com.loper7.layout.StringUtils
import com.loper7.layout.dialog.CardDatePickerDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btnCardDialogShow.setOnClickListener {

            var displayList = mutableListOf<Int>()
            if (checkYear.isChecked)
                displayList.add(DateTimePicker.YEAR)
            if (checkMonth.isChecked)
                displayList.add(DateTimePicker.MONTH)
            if (checkDay.isChecked)
                displayList.add(DateTimePicker.DAY)
            if (checkHour.isChecked)
                displayList.add(DateTimePicker.HOUR)
            if (checkMin.isChecked)
                displayList.add(DateTimePicker.MIN)

            var model = CardDatePickerDialog.CARD
            if (radioModelCard.isChecked)
                model = CardDatePickerDialog.CARD
            if (radioModelCube.isChecked)
                model = CardDatePickerDialog.CUBE
            if(radioModelStack.isChecked)
                model=CardDatePickerDialog.STACK
            if(radioModelCustom.isChecked)
                model=R.drawable.shape_bg_dialog_custom

            CardDatePickerDialog(this)
                .setTitle("CARD DATE PICKER DIALOG")
                .setDisplayType(displayList)
                .setModel(model)
                .showBackNow(checkBackNow.isChecked)
                .showDateLabel(checkUnitLabel.isChecked)
                .showFocusDateInfo(checkDateInfo.isChecked)
                .setOnChooseListener(object : CardDatePickerDialog.OnChooseListener {
                    @SuppressLint("SetTextI18n")
                    override fun onChoose(millisecond: Long) {
                        tvChooseDate.text = "◉  ${StringUtils.conversionTime(
                            millisecond,
                            "yyyy-MM-dd HH:mm"
                        )}    ${StringUtils.getWeek(millisecond)}"
                    }
                }).show()
        }
    }
}

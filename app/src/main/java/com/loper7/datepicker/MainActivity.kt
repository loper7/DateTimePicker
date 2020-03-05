package com.loper7.datepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.loper7.layout.DateTimePicker
import com.loper7.layout.dialog.CardDatePickerDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onClick(v: View) {
        when(v.id){
            /*****************************CardDatePickerDialog******************************/
            R.id.btnDefault->{
                CardDatePickerDialog(this)
                    .setTitle("DEFAULT EXAMPLES")
                    .show()
            }
            R.id.btnNoToday->{
                CardDatePickerDialog(this)
                    .setTitle("NO-TODAY EXAMPLES")
                    .setDisplayType(DateTimePicker.YEAR,DateTimePicker.MONTH,DateTimePicker.DAY,DateTimePicker.HOUR)
                    .showBackNow(false)
                    .show()
            }
            R.id.btnNoWeek->{
                CardDatePickerDialog(this)
                    .setTitle("NO-WEEK EXAMPLES")
                    .setDisplayType(DateTimePicker.MIN,DateTimePicker.MONTH,DateTimePicker.DAY,DateTimePicker.HOUR)
                    .showFocusDateInfo(false)
                    .show()
            }
            R.id.btnNoDateLabel->{
                CardDatePickerDialog(this)
                    .setTitle("NO DATE LABEL EXAMPLES")
                    .showDateLabel(false)
                    .show()
            }
            R.id.btnFreedom->{
                CardDatePickerDialog(this)
                    .setTitle("FREEDOM EXAMPLES")
                    .showFocusDateInfo(false)
                    .setDisplayType(DateTimePicker.MONTH,DateTimePicker.DAY,DateTimePicker.HOUR)
                    .show()
            }
        }

    }
}

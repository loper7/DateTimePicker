package com.loper7.datepicker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * @CreateDate: 2020/12/1 11:48
 * @Description:
 * @Author: LOPER7
 * @Email: loper7@163.com
 */
public class JMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new CardDatePickerDialog.Builder(this)
                .setTitle("SET MAX DATE")
                .setOnChoose("确定", new Function1<Long, kotlin.Unit>() {
                    @Override
                    public Unit invoke(Long aLong) {
                        //aLong  = millisecond
                        return null;
                    }
                }).build().show();

        new CardDatePickerDialog.Builder(this)
                .setTitle("SET MAX DATE")
                .setOnChoose("确定", aLong -> {
                    //aLong  = millisecond
                    return null;
                }).build().show();

    }
}

package com.loper7.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.loper7.layout.number_picker.NumberPicker;

import java.util.Calendar;
import java.util.Date;

public class DateTimePicker extends FrameLayout {

    public static int YEAR = 0;
    public static int MONTH = 1;
    public static int DAY = 2;
    public static int HOUR = 3;
    public static int MIN = 4;

    private NumberPicker mYearSpinner;
    private NumberPicker mMonthSpinner;
    private NumberPicker mDaySpinner;
    private NumberPicker mHourSpinner;
    private NumberPicker mMinuteSpinner;
    private Calendar mDate;
    private int mYear, mMonth, mDay, mHour, mMinute, minMonth = 1, minDay = 1;
    private long millisecond;
    private OnDateTimeChangedListener mOnDateTimeChangedListener;

    private int[] displayType = {YEAR, MONTH, DAY, HOUR, MIN};

    private boolean showLabel = true;


    public DateTimePicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs);
        TypedArray attributesArray = context.obtainStyledAttributes(
                attrs, R.styleable.DateTimePicker, defStyle, 0);
        showLabel = attributesArray.getBoolean(R.styleable.DateTimePicker_showLabel, true);
        attributesArray.recycle();

        init(context);

    }

    public DateTimePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public DateTimePicker(Context context) {
        super(context);

        init(context);
    }

    /**
     * 设置显示类型
     *
     * @param types
     */
    public void setDisplayType(int[] types) {
        if (types == null || types.length <= 0)
            return;
        this.displayType = types;
        mYearSpinner.setVisibility(GONE);
        mMonthSpinner.setVisibility(GONE);
        mDaySpinner.setVisibility(GONE);
        mHourSpinner.setVisibility(GONE);
        mMinuteSpinner.setVisibility(GONE);
        for (int i = 0; i < types.length; i++) {
            if (displayType[i] == YEAR)
                mYearSpinner.setVisibility(VISIBLE);
            if (displayType[i] == MONTH)
                mMonthSpinner.setVisibility(VISIBLE);
            if (displayType[i] == DAY)
                mDaySpinner.setVisibility(VISIBLE);
            if (displayType[i] == HOUR)
                mHourSpinner.setVisibility(VISIBLE);
            if (displayType[i] == MIN)
                mMinuteSpinner.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置默认时间戳
     *
     * @param time
     */
    public void setDefaultMillisecond(long time) {
        if (time != 0)
            mDate.setTime(new Date(time));
        mYear = mDate.get(Calendar.YEAR);
        mMonth = mDate.get(Calendar.MONTH) + 1;
        mDay = mDate.get(Calendar.DAY_OF_MONTH);
        mHour = mDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mDate.get(Calendar.MINUTE);
        millisecond = mDate.getTimeInMillis();

        mYearSpinner.setValue(mYear);
        mMonthSpinner.setValue(mMonth);
        mDaySpinner.setValue(mDay);
        mHourSpinner.setValue(mHour);
        mMinuteSpinner.setValue(mMinute);

        onDateTimeChanged();
    }

    /**
     * 设置最小选择时间
     *
     * @param time
     */
    public void setMinMillisecond(long time) {
        if (time == 0)
            return;
        Calendar mDate = Calendar.getInstance();
        mDate.setTime(new Date(time));
        int mYear = mDate.get(Calendar.YEAR);
        minMonth = mDate.get(Calendar.MONTH) + 1;
        minDay = mDate.get(Calendar.DAY_OF_MONTH);

        mYearSpinner.setMinValue(mYear);
        mMonthSpinner.setMinValue(mMonth);
        mDaySpinner.setMinValue(minDay);

        if (this.mDate.getTimeInMillis() < mDate.getTimeInMillis())
            setDefaultMillisecond(time);

    }


    private void init(Context context) {
        mDate = Calendar.getInstance();
        mYear = mDate.get(Calendar.YEAR);
        mMonth = mDate.get(Calendar.MONTH) + 1;
        mHour = mDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mDate.get(Calendar.MINUTE);
        millisecond = mDate.getTimeInMillis();

        inflate(context, R.layout.view_date_picker, this);


        mYearSpinner = (NumberPicker) this.findViewById(R.id.np_datetime_year);
        mYearSpinner.setMaxValue(2100);
        mYearSpinner.setMinValue(1900);
        if (showLabel)
            mYearSpinner.setLabel("年");
        mYearSpinner.setValue(mYear);
        mYearSpinner.setFocusable(true);
        mYearSpinner.setFocusableInTouchMode(true);
        mYearSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//设置NumberPicker不可编辑
        mYearSpinner.setOnValueChangedListener(mOnYearChangedListener);//注册NumberPicker值变化时的监听事件

        mMonthSpinner = (NumberPicker) this.findViewById(R.id.np_datetime_month);
        mMonthSpinner.setMaxValue(12);
        mMonthSpinner.setMinValue(1);
        if (showLabel)
            mMonthSpinner.setLabel("月");
        mMonthSpinner.setValue(mMonth);
        mYearSpinner.setFocusable(true);
        mYearSpinner.setFocusableInTouchMode(true);
        mMonthSpinner.setFormatter(formatter);//格式化显示数字，个位数前添加0
        mMonthSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mMonthSpinner.setOnValueChangedListener(mOnMonthChangedListener);

        mDaySpinner = (NumberPicker) this.findViewById(R.id.np_datetime_day);
        judgeMonth();//判断是否闰年，从而设置2月份的天数
        mDay = mDate.get(Calendar.DAY_OF_MONTH);
        mDaySpinner.setFormatter(formatter);
        if (showLabel)
            mDaySpinner.setLabel("日");
        mYearSpinner.setFocusable(true);
        mYearSpinner.setFocusableInTouchMode(true);
        mDaySpinner.setValue(mDay);
        mDaySpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mDaySpinner.setOnValueChangedListener(mOnDayChangedListener);

        mHourSpinner = (NumberPicker) this.findViewById(R.id.np_datetime_hour);
        mHourSpinner.setMaxValue(23);
        mHourSpinner.setMinValue(0);
        mHourSpinner.setLabel("时");
        if (showLabel)
            mYearSpinner.setFocusable(true);
        mYearSpinner.setFocusableInTouchMode(true);
        mHourSpinner.setValue(mHour);
        mHourSpinner.setFormatter(formatter);
        mHourSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);

        mMinuteSpinner = (NumberPicker) this.findViewById(R.id.np_datetime_minute);
        mMinuteSpinner.setMaxValue(59);
        mMinuteSpinner.setMinValue(0);
        if (showLabel)
            mMinuteSpinner.setLabel("分");
        mYearSpinner.setFocusable(true);
        mYearSpinner.setFocusableInTouchMode(true);
        mMinuteSpinner.setValue(mMinute);
        mMinuteSpinner.setFormatter(formatter);
        mMinuteSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);

    }

    private void refreshUI(){
        if (showLabel) {
            mYearSpinner.setLabel("年");
            mMonthSpinner.setLabel("月");
            mDaySpinner.setLabel("日");
            mHourSpinner.setLabel("时");
            mMinuteSpinner.setLabel("分");
        }else {
            mYearSpinner.setLabel("");
            mMonthSpinner.setLabel("");
            mDaySpinner.setLabel("");
            mHourSpinner.setLabel("");
            mMinuteSpinner.setLabel("");
        }
    }

    private NumberPicker.OnValueChangeListener mOnYearChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal, EditText editText) {
            mYear = mYearSpinner.getValue();
            judgeYear();
            onDateTimeChanged();
        }

    };

    private NumberPicker.OnValueChangeListener mOnMonthChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal, EditText editText) {
            mMonth = mMonthSpinner.getValue();
            judgeMonth();
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnDayChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal, EditText editText) {
            mDay = mDaySpinner.getValue();
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnHourChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal, EditText editText) {
            mHour = mHourSpinner.getValue();
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal, EditText editText) {
            mMinute = mMinuteSpinner.getValue();
            onDateTimeChanged();
        }
    };

    //数字格式化，<10的数字前自动加0
    private static NumberPicker.Formatter formatter = value -> {
        String Str = String.valueOf(value);
        if (value < 10) {
            Str = "0" + Str;
        }
        return Str;
    };

    /*
     *接口回调 参数是当前的View 年月日时分秒
     */
    public interface OnDateTimeChangedListener {
        void onDateTimeChanged(DateTimePicker view, long millisecond, int year, int month, int day, int hour, int minute);
    }


    /**
     * 日期发生变化
     */
    private void onDateTimeChanged() {
        millisecond = StringUtils.conversionTime(mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinute + ":00", "yyyy-MM-dd HH:mm:ss");
        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener.onDateTimeChanged(this, millisecond, mYear, mMonth, mDay, mHour, mMinute);
        }
    }

    /**
     * 判定闰年
     */
    private void judgeYear() {
        if (mMonth == 2) {
            if (isLeapYear(mYear)) {
                if (mDaySpinner.getMaxValue() != 29) {
                    mDaySpinner.setDisplayedValues(null);
                    mDaySpinner.setMinValue(1);
                    mDaySpinner.setMaxValue(29);
                }
            } else {
                if (mDaySpinner.getMaxValue() != 28) {
                    mDaySpinner.setDisplayedValues(null);
                    mDaySpinner.setMinValue(1);
                    mDaySpinner.setMaxValue(28);
                }
            }
        }

        if (mYear == mYearSpinner.getMinValue())
            mMonthSpinner.setMinValue(minMonth);
        else
            mMonthSpinner.setMinValue(1);

        if (mYear == mYearSpinner.getMinValue() && mMonth == mMonthSpinner.getMinValue())
            mDaySpinner.setMinValue(minDay);

        mDay = mDaySpinner.getValue();
    }


    /**
     * 判定闰月
     */
    private void judgeMonth() {
        if (mMonth == 2) {
            if (isLeapYear(mYear)) {
                if (mDaySpinner.getMaxValue() != 29) {
                    mDaySpinner.setDisplayedValues(null);
                    mDaySpinner.setMinValue(1);
                    mDaySpinner.setMaxValue(29);
                }
            } else {
                if (mDaySpinner.getMaxValue() != 28) {
                    mDaySpinner.setDisplayedValues(null);
                    mDaySpinner.setMinValue(1);
                    mDaySpinner.setMaxValue(28);
                }
            }
        } else {
            switch (mMonth) {
                case 4:
                case 6:
                case 9:
                case 11:
                    if (mDaySpinner.getMaxValue() != 30) {
                        mDaySpinner.setDisplayedValues(null);
                        mDaySpinner.setMinValue(1);
                        mDaySpinner.setMaxValue(30);
                    }
                    break;
                default:
                    if (mDaySpinner.getMaxValue() != 31) {
                        mDaySpinner.setDisplayedValues(null);
                        mDaySpinner.setMinValue(1);
                        mDaySpinner.setMaxValue(31);
                    }
            }
        }

        if (mYear == mYearSpinner.getMinValue() && mMonth == mMonthSpinner.getMinValue()) {
            mDaySpinner.setMinValue(minDay);
        }

        mDay = mDaySpinner.getValue();

    }


    /**
     * 是否为闰年
     *
     * @param year 当前年份
     * @return true or false
     */
    private boolean isLeapYear(int year) {
        Calendar c = Calendar.getInstance();
        c.set(year, 2, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return c.get(Calendar.DAY_OF_MONTH) == 28;
    }

    /*
     *对外的公开方法
     */
    public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {
        mOnDateTimeChangedListener = callback;
        onDateTimeChanged();
    }

    /**
     * 是否显示label
     * @param b
     */
    public void showLabel(boolean b) {
        this.showLabel = b;
        refreshUI();
    }
}
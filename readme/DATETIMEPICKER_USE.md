# DateTimePicker 使用
##### xml中
``` xml
        <com.loper7.date_time_picker.DateTimePicker
            android:id="@+id/dateTimePicker"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:showLabel="true"
            app:textSize="16sp"
            app:themeColor="#FF8080" />
```
##### 代码中
* 设置监听
``` kotlin
    dateTimePicker.setOnDateTimeChangedListener { view, millisecond, year, month, day, hour, minute ->
        var chooseTime="${StringUtils.conversionTime(millisecond, "yyyy年MM月dd日 HH时mm分")}"
    }
```
以上为最简单的使用方式
##### 更多设置
* 设置显示状态
>DateTimePicker支持显示 年月日时分 五个选项的任意组合，显示顺序以此为年、月、日、时、分，setDisplayType中可无序设置。
``` kotlin
     dateTimePicker.setDisplayType(intArrayOf(
            DateTimePicker.YEAR,//显示年
            DateTimePicker.MONTH,//显示月
            DateTimePicker.DAY,//显示日
            DateTimePicker.HOUR,//显示时
            DateTimePicker.MIN))//显示分
```
* 设置默认选中时间
``` kotlin
 dateTimePicker.setDefaultMillisecond(defaultMillisecond)//defaultMillisecond 为毫秒时间戳
```
* 设置允许选择的最小时间
``` kotlin
  dateTimePicker.setMinMillisecond(minMillisecond)
```
* 设置允许选择的最大时间
``` kotlin
  dateTimePicker.setMaxMillisecond(maxMillisecond)
```
*  是否显示label标签（选中栏 年月日时分汉字）
``` kotlin
  dateTimePicker.showLabel(true)
```
*  设置主题颜色
``` kotlin
  dateTimePicker.setThemeColor(ContextCompat.getColor(context,R.color.colorPrimary))
```
*  设置字体大小
>设置的字体大小为选中栏的字体大小，预览字体会根据字体大小等比缩放
``` kotlin
  dateTimePicker.setTextSize(15)//单位为sp
```

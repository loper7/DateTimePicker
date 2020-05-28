# CardDatePickerDialog 使用
##### 最简单的使用方式
``` kotlin
   CardDatePickerDialog(context).setOnChooseListener(object :CardDatePickerDialog.OnChooseListener {
                override fun onChoose(millisecond: Long) {
                }
            })
```
##### 所有可配置属性
``` kotlin
  CardDatePickerDialog.builder(context)
                .setTitle("CARD DATE PICKER DIALOG")
                .setDisplayType(displayList)
                .setBackGroundModel(model)
                .showBackNow(true)
                .setDefaultTime(defaultDate)
                .setMaxTime(maxDate)
                .setMinTime(minDate)
                .setThemeColor(color)
                .showDateLabel(true)
                .showFocusDateInfo(true)
                .build().setOnChooseListener(object : CardDatePickerDialog.OnChooseListener {
                    @SuppressLint("SetTextI18n")
                    override fun onChoose(millisecond: Long) {
       
                    }
                }).show()
```
##### 可配置属性说明
* 设置标题
``` kotlin
fun setTitle(value: String)
```
* 是否显示回到当前按钮
``` kotlin
fun showBackNow(b: Boolean)
```
* 是否显示选中日期信息
``` kotlin
fun showFocusDateInfo(b: Boolean)
```
* 显示模式
``` kotlin
// model 分为：CardDatePickerDialog.CARD//卡片,CardDatePickerDialog.CUBE//方形,CardDatePickerDialog.STACK//顶部圆角
// model 允许直接传入drawable资源文件id作为弹窗的背景，如示例内custom
fun setBackGroundModel(model: Int)
```
* 设置主题颜色
``` kotlin
fun setThemeColor(@ColorInt themeColor: Int)
```
>以下方法参考[DateTimePicker 使用说明](https://github.com/loperSeven/DateTimePicker/blob/master/readme/DATETIMEPICKER_USE.md)
* 设置显示值
``` kotlin
fun setDisplayType(vararg types: Int)
```
``` kotlin
fun setDisplayType(types: MutableList<Int>)
```
* 设置默认时间
``` kotlin
fun setDefaultTime(millisecond: Long)
```
* 设置范围最小值
``` kotlin
fun setMinTime(millisecond: Long)
```
* 设置范围最大值
``` kotlin
fun setMaxTime(millisecond: Long)
```
* 是否显示单位标签
``` kotlin
fun showDateLabel(b: Boolean)
```

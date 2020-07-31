# CardDatePickerDialog 使用
##### 最简单的使用方式
``` kotlin
     CardDatePickerDialog.builder(this)
                .setTitle("SET MAX DATE")
                .setOnChoose(listener = object :CardDatePickerDialog.OnChooseListener{
                    override fun onChoose(millisecond: Long) {
                     
                    }
                }).build().show()
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
                .setLabelText("年","月","日","时","分")
                .setOnChoose("选择",object :CardDatePickerDialog.OnChooseListener{
                    override fun onChoose(millisecond: Long) {
                     
                    }
                })
                .setOnCancel("关闭",object :CardDatePickerDialog.OnCancelListener{
                    override fun onCancel() {

                    }
                }).build().show()
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
* 设置标签文字
``` kotlin
/**
*示例
*setLabelText("年","月","日","时","分")
*setLabelText("年","月","日","时")
*setLabelText(month="月",hour="时")
*/
fun setLabelText(year:String=yearLabel,month:String=monthLabel,day:String=dayLabel,hour:String=hourLabel,min:String=minLabel)
```
* 绑定选择监听
``` kotlin
/**
*示例
*setOnChoose("确定")
*setOnChoose(listener = object :CardDatePickerDialog.OnChooseListener{
                    override fun onChoose(millisecond: Long) {
                    }
                })
*setOnChoose("确定",object :CardDatePickerDialog.OnChooseListener{
                    override fun onChoose(millisecond: Long) {
                    }
                })
*/
fun setOnChoose(text: String = "确定", listener: OnChooseListener?=null)
```
* 绑定取消监听
``` kotlin
/**
*示例
*setOnCancel("取消")
*setOnCancel(listener = object :CardDatePickerDialog.OnCancelListener{
                  override fun onCancel() {
                    }
                })
*setOnCancel("取消",object :CardDatePickerDialog.OnCancelListener{
                    override fun onCancel() {
                    }
                })
*/
fun setOnCancel(text: String = "取消", listener: OnCancelListener?=null)
```

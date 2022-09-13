![](https://github.com/loperSeven/DateTimePicker/blob/master/images/title.png)
<br/>
[![](https://jitpack.io/v/loperSeven/DateTimePicker.svg)](https://jitpack.io/#loperSeven/DateTimePicker)&ensp;[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)&ensp;[![](https://img.shields.io/badge/platform-android-green)](https://github.com/loperSeven)&ensp;[![](https://img.shields.io/badge/license-MIT-blue)](https://opensource.org/licenses/MIT)
<br/>
## Language
中文 | [English](https://github.com/loperSeven/DateTimePicker/blob/master/README_EN.md)
<br/>
<br/>
DateTimePicker 一个简约、漂亮的日期时间选择器，支持全方位自定义UI，内置日期时间选择弹窗，基于 Google BottomSheetDialog，可直接使用。
<br/>
## 预览
加载不出图片可以前往 [掘金](https://juejin.cn/post/6917909994985750535) 查看
<br/>
<br/>
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/gif_date.gif)
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/gif_more.gif)
<br/>
## 快速体验
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/app_qrcode.png)
<br/>
或者&ensp;[点击下载](http://fir.cqtencent.cn/dtpicker)
<br/>
## 如何引入
Step 1. 添加 JitPack repository 
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
Step 2. 添加 Gradle依赖
```
dependencies {
    ...
    implementation 'com.google.android.material:material:1.1.0' //为了防止不必要的依赖冲突，0.0.3开始需要自行依赖google material库
    implementation 'com.github.loperSeven:DateTimePicker:0.5.8'//具体版本请看顶部jitpack标识，如0.5.8,仅支持androidx
}


```
## 如何使用
日期时间选择控件
<br/>
&ensp;&ensp;[DateTimePicker 使用说明](https://github.com/loperSeven/DateTimePicker/wiki/DateTimePicker-%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E)
<br/>
卡片弹窗
<br/>
&ensp;&ensp;[CardDatePickerDialog 使用说明](https://github.com/loperSeven/DateTimePicker/wiki/CardDatePickerDialog-%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E)
<br/>
自定义选择器
<br/>
&ensp;&ensp;[DateTimePicker 自定义](https://github.com/loperSeven/DateTimePicker/wiki/DateTimePicker-%E8%87%AA%E5%AE%9A%E4%B9%89)
<br/>
周选择弹窗
<br/>
&ensp;&ensp;[CardWeekPickerDialog 使用说明](https://github.com/loperSeven/DateTimePicker/wiki/CardWeekPickerDialog-%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E)
## 更新日志

### [v0.5.8](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.8) -> [v0.6.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.6.0)
* fix [issues 64](https://github.com/loperSeven/DateTimePicker/issues/64)
* fix [issues 64](https://github.com/loperSeven/DateTimePicker/issues/66)
* CardDatePickerDialog 内选中日期信息支持选择农历模式 or 公历模式
```kotlin
/**
 * 设置dialog选中日期信息展示格式
 * @param value 1:LUNAR 0:DEFAULT
 * @return Builder
 */
 fun setChooseDateModel(value: Int): Builder {
 	this.chooseDateModel = value
        return this
 }
```
* 适配深色模式
```kotlin
//由于背景、DateTimePicker以及主题色均可由开发者灵活配置，为了兼容这些配置，所以适配深色模式需要调用以下几个方法进行设置。

//1.自定义dialog背景，为light/dark模式设置不同的值
builder.setBackGroundModel(if(isDark) R.drawable.shape_bg_dialog_dark else R.drawable.shape_bg_dialog_light)
//2.自定义dialog辅助文字颜色
setAssistColor(if(isDark) darkColor else lightColor)
//3.自定义dialog分割线颜色
setDividerColor(if(isDark) darkColor else lightColor)

//这样深色模式就适配完成了，当然，以上三个方法的作用不仅可以用于适配深色模式，也可以传入更契合app主题的颜色，用于与app统一UI风格。
```

### [v0.5.7](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.7) -> [v0.5.8](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.8)
* fix [issues 57](https://github.com/loperSeven/DateTimePicker/issues/57)

### [v0.5.3](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.3) -> [v0.5.7](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.7)
* fix [issues 51](https://github.com/loperSeven/DateTimePicker/issues/51)
* fix [issues 53](https://github.com/loperSeven/DateTimePicker/issues/53)

### [v0.5.2](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.2) -> [v0.5.3](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.3)
* fix [issues 47](https://github.com/loperSeven/DateTimePicker/issues/47)
* fix [issues 48](https://github.com/loperSeven/DateTimePicker/issues/48)

### [v0.5.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.1) -> [v0.5.2](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.2)
* fix [issues 44](https://github.com/loperSeven/DateTimePicker/issues/44)
* `CardDatePickerDialog` 支持设置 `touchHideable`， 用以控制是否允许滑动手势关闭弹窗
```kotlin
	/**
         * 是否可以滑动关闭弹窗
         * @param touchHideable 默认为 true
         */
        fun setTouchHideable(touchHideable:Boolean = true):Builder{
            this.touchHideable = touchHideable
            return this
        }
```
* `CardWeekPickerDialog` 支持设置 `formatter` ，可自定义格式化展示样式
```kotlin
	/**
         * 设置格式化
         * @param datas 数据
         * @return Builder
         */
        fun setFormatter(formatter: (MutableList<MutableList<Long>>) -> NumberPicker.Formatter?): Builder {
            this.formatter = formatter
            return this
        }
```


### [v0.5.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.0) -> [v0.5.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.1)
* 支持农历日期转换
```kotlin
var lunar = Lunar.getInstance(calendar)
lunar?.apply {
   Log.d("lunar",lunar.toString())
}
```
* `CardDatePickerDialog` 内原标题下日期信息变更为农历日期信息<br><br>
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/img_lunar.jpg)

### [v0.4.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.4.1) -> [v0.5.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.5.0)
* fix [issues 40](https://github.com/loperSeven/DateTimePicker/issues/40)
* fix [issues 39](https://github.com/loperSeven/DateTimePicker/issues/39)
* fix [issues 37](https://github.com/loperSeven/DateTimePicker/issues/37)
* 优化选择周功能
> 注意：为了规避属性名与系统冲突问题，此版本对自定义属性名添加了前缀`dt`。

### [v0.3.4](https://github.com/loperSeven/DateTimePicker/releases/tag/0.3.4) -> [v0.4.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.4.1)
* fix [issues 29](https://github.com/loperSeven/DateTimePicker/issues/29)
* fix [issues 30](https://github.com/loperSeven/DateTimePicker/issues/30)
* fix [issues 32](https://github.com/loperSeven/DateTimePicker/issues/32)
* fix [issues 34](https://github.com/loperSeven/DateTimePicker/issues/34)
* fix [issues 35](https://github.com/loperSeven/DateTimePicker/issues/35)
* DateTimePicker 新增 getMillisecond() 方法
* 新增选择周功能弹窗

### [v0.3.3](https://github.com/loperSeven/DateTimePicker/releases/tag/0.3.3) -> [v0.3.4](https://github.com/loperSeven/DateTimePicker/releases/tag/0.3.4)
**支持设置国际化日期选择格式**
`DateTimePicker` 新增如下方法，`CardDatePickerDialog` 不受任何影响。
* 设置国际化日期显示格式 
```kotlin
    /**
     * 设置国际化日期格式显示 如果没有自定义layout且需要设置的日期格式为 dd-MM(单词缩写)-yyyy HH:mm:ss,此方法会改变年月日的顺序，及月份的显示。
     * @param global : DateTimeConfig.GLOBAL_LOCAL 根据设备系统语言自动选择
     *                 DateTimeConfig.GLOBAL_CHINA 设置日期格式为 yyyy-MM-dd HH:mm:ss
     *                 DateTimeConfig.GLOBAL_US 设置日期格式为 dd-MM(单词缩写)-yyyy HH:mm:ss
     */
    fun setGlobal(global: Int) {
      ...
    }
```
* 获取类型对应的 NumberPicker
```kotlin
    /**
     * 获取类型对应的NumberPicker
     * @param displayType 类型
     */
    fun getPicker(displayType: Int): NumberPicker? {
        ...
    }
```
用例可见：[GlobalizationActivity](https://github.com/loperSeven/DateTimePicker/blob/master/app/src/main/java/com/loper7/datepicker/GlobalizationActivity.kt)

### [v0.3.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.3.1) -> [v0.3.3](https://github.com/loperSeven/DateTimePicker/releases/tag/0.3.3)
* fix [issues 24](https://github.com/loperSeven/DateTimePicker/issues/24)
* fix [issues 26](https://github.com/loperSeven/DateTimePicker/issues/26)

### [v0.3.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.3.0) -> [v0.3.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.3.1)
* fix [issues 18](https://github.com/loperSeven/DateTimePicker/issues/18)

### [v0.2.3](https://github.com/loperSeven/DateTimePicker/releases/tag/0.2.3) -> [v0.3.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.3.0)
* 支持自定义选择器布局，详见[wiki](https://github.com/loperSeven/DateTimePicker/wiki/DateTimePicker-%E8%87%AA%E5%AE%9A%E4%B9%89)
* fix [issues 12](https://github.com/loperSeven/DateTimePicker/issues/12)

### [v0.2.2](https://github.com/loperSeven/DateTimePicker/releases/tag/0.2.2) -> [v0.2.3](https://github.com/loperSeven/DateTimePicker/releases/tag/0.2.3)
* fix [issues 11](https://github.com/loperSeven/DateTimePicker/issues/11)

### [v0.2.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.2.1) -> [v0.2.2](https://github.com/loperSeven/DateTimePicker/releases/tag/0.2.2)
* fix [issues 10](https://github.com/loperSeven/DateTimePicker/issues/10)

### [v0.2.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.2.0) -> [v0.2.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.2.1)
* fix [issues 7](https://github.com/loperSeven/DateTimePicker/issues/7)

### [v0.1.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.1.1) -> [v0.2.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.2.0)
* 新增DateTimeController逻辑控制器，以支持UI 100% 自定义
* 重构DateTimePicker，抽离逻辑至DateTimeController,自身仅控制UI
* 抽离常量至DateTimeConfig
* 抽离像素单位转换至ContextExt拓展
* fix [issues 4](https://github.com/loperSeven/DateTimePicker/issues/4)-CardDatePickerDialog.Builder内变量添加@JvmField以支持Java调用

### [v0.1.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.1.0) -> [v0.1.1](https://github.com/loperSeven/DateTimePicker/releases/tag/0.1.1)
* 修复设置最大年份后，当选择到最大年份时，月份为0的问题
* 修复范围最大值，范围最小值，选中默认值之间冲突时约束异常问题（规则为：最大范围值与最小范围值存在冲突时，后设置的值不会生效；选中默认值与范围最大值/范围最小值存在冲突时，不会生效）
* 优化监听回调代码，使用kotlin特性，使回调更加简洁
<br/><br/>eg:感谢 [yaolv7](https://github.com/yaolv7) 提供的Pull requests
### [v0.0.7](https://github.com/loperSeven/DateTimePicker/releases/tag/0.0.7) -> [v0.1.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.1.0)
* DateTimePicker类变更为kotlin编写
* 新增单位标签可在代码内配置
* 移除DateTimePicker回调监听非必要字段
* 新增CardDatePickerDialog取消、确定按钮文字配置
* 新增CardDatePickerDialog取消按钮回调监听
* 变更CardDatePickerDialog监听绑定至builder内
<br/><br/>eg:此次更新对原有方法及配置有一定改动，详情查看 <strong>[ 如何使用 ]</strong>

## 混淆
```
-dontwarn com.loper7.date_time_picker.**
-keep class com.loper7.date_time_picker.**{*;}
```
## 感谢
[Number Picker](https://github.com/ShawnLin013/NumberPicker)
<br/>
## 联系我
Issues：[Issues](https://github.com/loperSeven/DateTimePicker/issues)
<br/>
邮箱：loper7@163.com
<br/>
## Licenses
```
Copyright 2020 loperSeven

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```


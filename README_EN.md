![](https://github.com/loperSeven/DateTimePicker/blob/master/images/title.png)
<br/>
[![](https://jitpack.io/v/loperSeven/DateTimePicker.svg)](https://jitpack.io/#loperSeven/DateTimePicker)&ensp;[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)&ensp;[![](https://img.shields.io/badge/platform-android-green)](https://github.com/loperSeven)&ensp;[![](https://img.shields.io/badge/license-MIT-blue)](https://opensource.org/licenses/MIT)
<br/>
## Language
[中文](https://github.com/loperSeven/DateTimePicker) | English
<br/>
<br/>
A simple and beautiful date and time picker, supports a large area of custom UI, built-in date and time selection pop-up window, based on Google BottomSheetDialog, can be used directly.
<br/>
## Preview
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/card.gif)
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/cube.gif)
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/sta.gif)
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/custom.gif)
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/main.gif)
<br/>
## Download APK
![](https://github.com/loperSeven/DateTimePicker/blob/master/images/app_qrcode.png)
<br/>
or&ensp;[Download](http://fir.cqtencent.cn/dtpicker)
<br/>
## Import
Step 1. add JitPack repository 
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
Step 2. add Gradle
```
dependencies {
    ...
    implementation 'com.google.android.material:material:1.1.0' //In order to prevent unnecessary dependency conflicts, you need to rely on the google material library yourself starting from 0.0.3
    implementation 'com.github.loperSeven:DateTimePicker:$version'//Please see the jitpack logo at the top for the specific version, only supports androidx
}


```
## Use
DateTimePicker
<br/>
&ensp;&ensp;[How to use DateTimePicker](https://github.com/loperSeven/DateTimePicker/blob/master/readme/DATETIMEPICKER_USE.md)
<br/>
Pop-ups
<br/>
&ensp;&ensp;[How to use CardDatePickerDialog](https://github.com/loperSeven/DateTimePicker/blob/master/readme/CARDDATEPICKERDIALOG_USE.md)
<br/>
## Update Log
### [v0.0.7](https://github.com/loperSeven/DateTimePicker/releases/tag/0.0.7) -> [v0.1.0](https://github.com/loperSeven/DateTimePicker/releases/tag/0.1.0)
* DateTimePicker class is changed to Kotlin
* The new unit label can be configured in the code
* Remove DateTimePicker callback monitoring non-essential fields
* Added CardDatePickerDialog cancel and confirm button text configuration
* Added CardDatePickerDialog cancel button callback listener
* Change the binding of CardDatePickerDialog to the builder

<br/>eg:This update has some changes to the original method and configuration, see details <strong>[ Use ]</strong>

## Garble
```
-dontwarn com.loper7.date_time_picker.**
-keep class com.loper7.date_time_picker.**{*;}
```
## Thanks
[Number Picker](https://github.com/ShawnLin013/NumberPicker)
<br/>
## Licenses
```
Copyright 2020 loperSeven

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```


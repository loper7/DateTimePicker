package com.loper7.datepicker

import android.app.Application
import android.graphics.Color
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.loper7.layout.TitleBar

/**
 *@Author loper7
 *@Date 2021/10/11 10:54
 *@Description
 **/
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TitleBar.getConfig()
            .setTitleTextSize(applicationContext, 18)
            .setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .setMenuTextSize(applicationContext, 16)
            .setMenuTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .setPadding(applicationContext, 16)
            .setCenterTitle(false)
            .setUseRipple(true)
            .setTitleTextBold(true)
            .setTitleEllipsize(TextUtils.TruncateAt.MARQUEE)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
            .setShowBorder(false)
            .setBorderWidth(applicationContext, 0.6f)
    }
}
package com.loper7.tab_expand.ext

import android.content.Context

/**
 *
 * @CreateDate:     2020/8/18 11:15
 * @Description:    java类作用描述
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
/**
 * 根据手机的分辨率dp 转成px(像素)
 */
internal fun Context.dip2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率px(像素) 转成dp
 */
internal fun Context.px2dip(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

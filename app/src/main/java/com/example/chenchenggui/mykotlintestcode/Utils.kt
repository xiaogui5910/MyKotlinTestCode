package com.example.chenchenggui.mykotlintestcode

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/14
 */
fun log(msg: Any) {
    Log.e("my_kotlin_test_code", "$msg")
}

fun toast(msg: Any) {
    Toast.makeText(MyApplication.instance, msg.toString(), Toast.LENGTH_SHORT).show()
}

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun dp2px(context: Context, dpValue: Float): Float {
    val scale = context.resources.displayMetrics.density
    return dpValue * scale + 0.5f
}
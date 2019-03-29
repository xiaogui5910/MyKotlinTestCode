package com.example.chenchenggui.mykotlintestcode

import android.util.Log
import android.widget.Toast

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/14
 */
fun log(msg: Any) {
    Log.e("red_packet", "$msg")
}

fun toast(msg: Any) {
    Toast.makeText(MyApplication.instance, msg.toString(), Toast.LENGTH_SHORT).show()
}
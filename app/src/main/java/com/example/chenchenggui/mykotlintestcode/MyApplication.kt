package com.example.chenchenggui.mykotlintestcode

import android.app.Application

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/28
 */
class MyApplication:Application() {
    companion object {
        var instance:MyApplication?=null
    }
    init {
        instance=this
    }

    override fun onCreate() {
        super.onCreate()
    }
}
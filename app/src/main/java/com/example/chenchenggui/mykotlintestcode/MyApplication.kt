package com.example.chenchenggui.mykotlintestcode

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig

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

        val config = ImagePipelineConfig.newBuilder(this)
                .setBitmapMemoryCacheParamsSupplier(MyBitmapMemoryCacheParamsSupplier(this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager))
                .setDownsampleEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .build()
        Fresco.initialize(this, config)
    }
}
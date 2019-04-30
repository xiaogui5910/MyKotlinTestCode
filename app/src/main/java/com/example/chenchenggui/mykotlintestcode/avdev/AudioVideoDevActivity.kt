package com.example.chenchenggui.mykotlintestcode.avdev

import android.graphics.BitmapFactory
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_audio_video_dev.*

class AudioVideoDevActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_audio_video_dev

    override fun initView() {
        super.initView()

        surface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                if (holder == null) {
                    return
                }

                val paint = Paint().apply {
                    isAntiAlias = true
                    style = Paint.Style.STROKE
                }

                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.city)

                val canvas = holder.lockCanvas()//锁定当前画布
                canvas.drawBitmap(bitmap, 0f, 0f, paint)//绘制
                holder.unlockCanvasAndPost(canvas)//解除锁定并显示在界面上
            }
        })
    }
}

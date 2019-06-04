package com.example.chenchenggui.mykotlintestcode.avdev

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent


/**
 * description ：
 * author : chenchenggui
 * creation date: 2019/5/30
 */
class MyGlSurfaceView : GLSurfaceView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    var mRenderer: MyGLRenderer

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        mRenderer = MyGLRenderer()
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer)

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    private val TOUCH_SCALE_FACTOR = 180.0f / 320
    private var mPreviousX: Float = 0.toFloat()
    private var mPreviousY: Float = 0.toFloat()

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x = e.x
        val y = e.y

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                var dx = x - mPreviousX
                var dy = y - mPreviousY

                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    dx = dx * -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy = dy * -1
                }

                mRenderer.mAngle = (mRenderer.mAngle + (dx + dy) * TOUCH_SCALE_FACTOR)
                requestRender()
            }
        }

        mPreviousX = x
        mPreviousY = y
        return true
    }
}
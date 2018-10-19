package com.example.chenchenggui.mykotlintestcode

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/10/19
 */
class AnimView(context: Context, var attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context,
        attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var isBezierBackDone = false
    private var mWidth = 0
    private var mHeight = 0
    private var pullWidth = 0
    private var pullDelta = 0

    private var start = 0L
    private var stop = 0L
    private var bezierBackRatio = 0f
    private var bezierDelta = 0
        get() {
            bezierBackRatio = getBezierBackRatio()
            return (bezierDelta * bezierBackRatio).toInt()
        }

    private fun getBezierBackRatio(): Float {
        if (System.currentTimeMillis() >= stop) return 1f
        var ratio = (System.currentTimeMillis() - start) / bezierBackDur.toFloat()
        return Math.min(1f, ratio)
    }

    open var bezierBackDur = 0L

    private var backPaint: Paint
    private var path: Path

    private var animStatus = AnimatorStatus.PULL_LEFT

    enum class AnimatorStatus {
        PULL_LEFT, DRAG_LEFT, RELEASE
    }

    init {
        pullWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources
                .displayMetrics).toInt()
        pullDelta = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, resources
                .displayMetrics).toInt()

        path = Path()

        backPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMSpec = widthMeasureSpec
        var width = MeasureSpec.getSize(widthMeasureSpec)
        if (width > pullDelta + pullWidth) {
            widthMSpec = MeasureSpec.makeMeasureSpec(pullDelta + pullWidth, MeasureSpec.getMode
            (widthMeasureSpec))
        }
        super.onMeasure(widthMSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            mWidth = width
            mHeight = height

            if (mWidth < pullWidth) {
                animStatus = AnimatorStatus.PULL_LEFT
            }

            when (animStatus) {
                AnimatorStatus.PULL_LEFT -> {
                    if (mWidth >= pullWidth) {
                        animStatus = AnimatorStatus.DRAG_LEFT
                    }
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (animStatus) {
            AnimatorStatus.PULL_LEFT -> canvas?.drawRect(0f, 0f, mWidth.toFloat(), mHeight.toFloat(),
                    backPaint)
            AnimatorStatus.DRAG_LEFT -> drawDrag(canvas)
            AnimatorStatus.RELEASE -> drawBack(canvas, bezierDelta)
        }
    }

    private fun drawBack(canvas: Canvas?, delta: Int) {
        with(path) {
            reset()
            moveTo(mWidth.toFloat(), 0f)
            lineTo((mWidth - pullWidth).toFloat(), 0f)
            quadTo(delta.toFloat(), (mHeight / 2).toFloat(), (mWidth - pullWidth).toFloat(), mHeight.toFloat())
            lineTo(mWidth.toFloat(), mHeight.toFloat())
        }
        canvas?.drawPath(path, backPaint)

        invalidate()

        if (bezierBackRatio == 1f) {
            isBezierBackDone = true
        }

        if (isBezierBackDone && mWidth <= pullWidth) {
            drawFooterBack(canvas)
        }
    }

    private fun drawFooterBack(canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), backPaint)
    }

    private fun drawDrag(canvas: Canvas?) {
        canvas?.drawRect((mWidth - pullWidth).toFloat(), 0f, mWidth.toFloat(), mHeight.toFloat(),
                backPaint)

        with(path) {
            reset()
            moveTo((mWidth - pullWidth).toFloat(), 0f)
            quadTo(0f, (mHeight / 2).toFloat(), (mWidth - pullWidth).toFloat(), mHeight.toFloat())
        }

        canvas?.drawPath(path, backPaint)
    }

    fun releaseDrag() {
        animStatus = AnimatorStatus.RELEASE
        start = System.currentTimeMillis()
        stop = start + bezierBackDur
        isBezierBackDone = false
        requestLayout()
    }

    fun setBgColor(color: Int) {
        backPaint.color = color
    }
}
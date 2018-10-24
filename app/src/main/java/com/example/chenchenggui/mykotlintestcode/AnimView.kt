package com.example.chenchenggui.mykotlintestcode

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View

/**
 * description ：橡皮筋动画view
 * author : chenchenggui
 * creation date: 2018/10/19
 */
class AnimView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context,
        attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var isBezierBackDone = false
    private var mWidth = 0
    private var mHeight = 0
    /**
     * 默认拉伸宽度，绘制矩形，超过后绘制曲线，默认值20dp
     */
    private var pullWidth = 0
    /**
     * 默认最大拉伸距离，超过后无法继续拉伸，默认值80dp
     */
    private var pullDelta = 0
    var top = 0f

    private var start = 0L
    private var stop = 0L
    private var bezierBackRatio = 0f
    private var bezierDelta = 0
        get() {
            bezierBackRatio = getBezierBackRatio()
            return (field * bezierBackRatio).toInt()
        }

    private fun getBezierBackRatio(): Float {
        if (System.currentTimeMillis() >= stop) return 1f
        val ratio = (System.currentTimeMillis() - start) / bezierBackDur.toFloat()
        return Math.min(1f, ratio)
    }

    var bezierBackDur = 0L

    private var backPaint: Paint
    private var path: Path

    private var animStatus = AnimatorStatus.PULL_LEFT

    enum class AnimatorStatus {
        PULL_LEFT, DRAG_LEFT, RELEASE
    }

    init {
        pullWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources
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
        val width = MeasureSpec.getSize(widthMeasureSpec)
        log("width=$width==pullDelta=$pullDelta==pullWidth=$pullWidth")
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

            log("mWidth=$mWidth===pullWidth=$pullWidth")
            if (mWidth < pullWidth) {
                animStatus = AnimatorStatus.PULL_LEFT
            }

            when (animStatus) {
                AnimatorStatus.PULL_LEFT -> {
                    if (mWidth >= pullWidth) {
                        animStatus = AnimatorStatus.DRAG_LEFT
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.e("anim","+mWidth=$mWidth===animStatus=$animStatus")
        when (animStatus) {
            AnimatorStatus.PULL_LEFT -> canvas?.drawRect(0f, 0f, mWidth.toFloat(),
                    mHeight.toFloat(), backPaint)
            AnimatorStatus.DRAG_LEFT -> drawDrag(canvas)
            AnimatorStatus.RELEASE -> drawBack(canvas, bezierDelta)
        }
    }

    private fun drawBack(canvas: Canvas?, delta: Int) {
        log("drawBack====mWidth=$mWidth=============top=$top")
        with(path) {
            reset()
            moveTo(mWidth.toFloat(), top)
            lineTo((mWidth - pullWidth).toFloat(), top)
            quadTo(delta.toFloat(), (mHeight / 2).toFloat(), (mWidth - pullWidth).toFloat(),
                    mHeight.toFloat()-top)
            lineTo(mWidth.toFloat(), mHeight.toFloat()-top)
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
        canvas?.drawRect((mWidth - pullWidth).toFloat(), top, mWidth.toFloat(), mHeight
                .toFloat() - top, backPaint)

        with(path) {
            reset()
            moveTo((mWidth - pullWidth).toFloat(), top)
            quadTo(0f, (mHeight / 2).toFloat(), (mWidth - pullWidth).toFloat(), mHeight
                    .toFloat()
                    - top)
        }

        canvas?.drawPath(path, backPaint)
    }

    fun releaseDrag(defaultOffsetX: Float) {
        animStatus = AnimView.AnimatorStatus.RELEASE
        start = System.currentTimeMillis()
        stop = start + bezierBackDur
        bezierDelta = mWidth - pullWidth
        isBezierBackDone = false
        offsetX=defaultOffsetX
        requestLayout()
    }
var offsetX=0f
    fun setBgColor(color: Int) {
        backPaint.color = color
    }
}
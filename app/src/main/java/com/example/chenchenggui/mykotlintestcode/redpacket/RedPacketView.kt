package com.example.chenchenggui.mykotlintestcode.redpacket

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.chenchenggui.mykotlintestcode.R
import java.util.*

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/11/27
 */
class RedPacketView : View {
    private var mImgIds = intArrayOf(R.drawable.red_packets_icon)//红包图片
    private var count: Int = 0//红包数量
    private var speed: Int = 0//下落速度
    private var maxSize: Float = 0f//红包大小的范围
    private var minSize: Float = 0f//红包大小的范围
    private var mWidth: Int = 0//view宽度
    private var animator: ValueAnimator? = null//属性动画，用该动画来不断改变红包下落的坐标值

    private var paint: Paint? = null//画笔
    private var prevTime: Long = 0
    private val redPacketList = ArrayList<RedPacket>()//红包数组
    private var redMatrix: Matrix? = null

    private var onRedPacketClickListener: OnRedPacketClickListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RedPacketView)
        count = typedArray.getInt(R.styleable.RedPacketView_count, 20)
        speed = typedArray.getInt(R.styleable.RedPacketView_speed, 20)
        minSize = typedArray.getFloat(R.styleable.RedPacketView_min_size, 0.5f)
        maxSize = typedArray.getFloat(R.styleable.RedPacketView_max_size, 1.2f)
        typedArray.recycle()
        init()
    }


    /**
     * 初始化
     */
    private fun init() {
        paint = Paint().apply {
            isFilterBitmap = true
            isDither = true
            isAntiAlias = true
        }
        redMatrix=Matrix()

        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        initAnimator()
    }

    private fun initAnimator() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            //每次动画更新的时候，更新红包下落的坐标值
            addUpdateListener {
                val nowTime = System.currentTimeMillis()
                val secs = (nowTime - prevTime).toFloat() / 1000f
                prevTime = nowTime

                for (i in redPacketList.indices) {
                    val redPacket = redPacketList[i]
                    //更新红包的下落的位置y
                    redPacket.y += +redPacket.speed * secs

                    //如果y坐标大于view的高度 说明划出屏幕，y重新设置起始位置，以及中奖属性
                    if (redPacket.y > height) {
                        redPacket.y = (0 - redPacket.height).toFloat()
                        redPacket.isRealRed = redPacket.isRealRedPacket
                    }
                    //更新红包的旋转的角度
                    redPacket.rotation += redPacket.rotationSpeed * secs
                }
                invalidate()
            }

            //属性动画无限循环
            repeatCount = ValueAnimator.INFINITE
            //属性值线性变换
            interpolator = LinearInterpolator()
            duration = 2000
        }
    }


    /**
     * 停止动画
     */
    fun stopRainNow() {
        //清空红包数据
        clear()
        //重绘
        invalidate()
        //动画取消
        animator?.cancel()
    }


    /**
     * 开始动画
     */
    fun startRain() {
        //清空红包数据
        clear()
        //添加红包
        setRedPacketCount(count)
        prevTime = System.currentTimeMillis()
        //动画开始
        animator?.start()

    }

    private fun setRedPacketCount(count: Int) {
        if (mImgIds.isEmpty())
            return
        for (i in 0 until count) {
            //获取红包原始图片
            val originalBitmap = BitmapFactory.decodeResource(resources, mImgIds[0])
            //生成红包实体类
            val redPacket = RedPacket(context, originalBitmap, speed, maxSize, minSize, mWidth)
            //添加进入红包数组
            redPacketList.add(redPacket)
        }
    }

    /**
     * 暂停红包雨
     */
    fun pauseRain() {
        animator?.cancel()
    }

    /**
     * 重新开始
     */
    fun restartRain() {
        animator?.start()
    }

    /**
     * 清空红包数据，并回收红包中的bitmap
     */
    private fun clear() {
        for (redPacket in redPacketList) {
            redPacket.recycle()
        }
        redPacketList.clear()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取自定义view的宽度
        mWidth = measuredWidth
    }

    override fun onDraw(canvas: Canvas) {
        //遍历红包数组，绘制红包
        for (i in redPacketList.indices) {
            val redPacket = redPacketList[i]
            //将红包旋转redPacket.rotation角度后 移动到（redPacket.x，redPacket.y）进行绘制红包

            redMatrix?.let {
                it.setTranslate((-redPacket.width / 2).toFloat(), (-redPacket.height / 2).toFloat())
                it.postRotate(redPacket.rotation)
                it.postTranslate(redPacket.width / 2 + redPacket.x, redPacket.height / 2 +
                        redPacket.y)
                //绘制红包
                canvas.drawBitmap(redPacket.bitmap!!, it, paint)
            }

        }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                //根据点击的坐标点，判断是否点击在红包的区域
                val redPacket = isRedPacketClick(motionEvent.x, motionEvent.y)
                if (redPacket != null) {
                    //如果点击在红包上，重新设置起始位置，以及中奖属性
                    redPacket.y = (0 - redPacket.height).toFloat()
                    redPacket.isRealRed = redPacket.isRealRedPacket

                    onRedPacketClickListener?.onRedPacketClickListener(redPacket)
                }
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }

    //根据点击坐标点，遍历红包数组，看是否点击在红包上
    private fun isRedPacketClick(x: Float, y: Float): RedPacket? {
        for (i in redPacketList.indices.reversed()) {
            if (redPacketList[i].isContains(x, y)) {
                return redPacketList[i]
            }
        }
        return null
    }

    interface OnRedPacketClickListener {
        fun onRedPacketClickListener(redPacket: RedPacket)
    }

    fun setOnRedPacketClickListener(onRedPacketClickListener: OnRedPacketClickListener) {
        this.onRedPacketClickListener = onRedPacketClickListener
    }
}

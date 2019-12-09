package com.example.chenchenggui.mykotlintestcode.redpacket

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.*
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.redpacket.RedPacketView.Companion.CLICK_STYLE_SCALE
import com.example.chenchenggui.mykotlintestcode.redpacket.RedPacketView.Companion.CLICK_STYLE_SCALE_ALPHA
import java.lang.Exception


/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/11/27
 */
class RedPacketView : View {
    private var countDownTimer: CountDownTimer?=null
    private var scale2PointDownTimer: CountDownTimer?=null
    private var scale2AlphaDownTimer: CountDownTimer?=null
    private var scale2Point1DownTimer: CountDownTimer?=null
    private var mImgIds = intArrayOf(R.drawable.img_rain_red_packet1, R.drawable
            .img_rain_red_packet1,R.drawable.img_rain_red_packet1,
           R.drawable.img_rain_red_packet1)//红包图片
    private var count: Int = 0//红包数量
    private var speed: Int = 0//下落速度
    private var maxSize: Float = 0f//红包大小的范围
    private var minSize: Float = 0f//红包大小的范围
    private var mWidth: Int = 0//view宽度
    private var animator: ValueAnimator? = null//属性动画，用该动画来不断改变红包下落的坐标值

    private var paint: Paint? = null//画笔
    private var prevTime: Long = 0L
    private val redPacketList = ArrayList<RedPacket>()//红包数组
    private var redMatrix: Matrix? = null
     var clickStyle: Int = CLICK_STYLE_DEFAULT_NO//点击响应样式

    companion object{
        const val CLICK_STYLE_DEFAULT_NO = 0
        const val CLICK_STYLE_SCALE = 1
        const val CLICK_STYLE_SCALE_ALPHA = 2
    }
    private var onRedPacketClickListener: OnRedPacketClickListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, com.example.chenchenggui.mykotlintestcode.R.styleable.RedPacketView)
        count = typedArray.getInt(com.example.chenchenggui.mykotlintestcode.R.styleable.RedPacketView_count, 20)
        speed = typedArray.getInt(com.example.chenchenggui.mykotlintestcode.R.styleable.RedPacketView_speed, 500)
        minSize = typedArray.getFloat(com.example.chenchenggui.mykotlintestcode.R.styleable.RedPacketView_min_size, 0.5f)
        maxSize = typedArray.getFloat(com.example.chenchenggui.mykotlintestcode.R.styleable.RedPacketView_max_size, 1.2f)
        typedArray.recycle()
        init()
    }


    /**
     * 初始化
     */
    private fun init() {
        //初始化画笔
        paint = Paint().apply {
            isFilterBitmap = true
            isDither = true
            isAntiAlias = true
        }
        redMatrix = Matrix()

        //绘制view开启硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        //初始化属性动画
        initAnimator()
    }

    private fun initAnimator() {
        //创建一个属性动画，通过属性动画来控制刷新红包下落的位置
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            //每次动画更新的时候，更新红包下落的坐标值
            addUpdateListener {
                val nowTime = System.currentTimeMillis()
                val secs = (nowTime - prevTime).toFloat() / 1000f
                prevTime = nowTime

                for (i in redPacketList.indices) {
                    val redPacket = redPacketList[i]
                    if (redPacket.clickAnimation ==RedPacket.ClickAnimation.SCALE){
//                        redPacket.width+=1000*secs.toInt()
//                        redPacket.height+=1000*secs.toInt()
                        continue
                    }else if (redPacket.clickAnimation ==RedPacket.ClickAnimation.SCALE_ALPHA){
                        continue
                    }else if (redPacket.clickAnimation ==RedPacket.ClickAnimation.DEFAULT){
                        redPacket.resetWidthAndHeight()

                        //更新红包的下落的位置y
                        redPacket.y += redPacket.speed * secs

                        //如果y坐标大于view的高度 说明划出屏幕，重新设置y起始位置，以及中奖属性
                        if (redPacket.y > height) {
                            redPacket.y = (0 - (redPacket.height * (redPacket.index + 1))).toFloat()
                            redPacket.isRealRed = redPacket.isRealRedPacket
                        }
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
        countDownTimer?.cancel()
        scale2PointDownTimer?.cancel()
        scale2Point1DownTimer?.cancel()
        scale2AlphaDownTimer?.cancel()
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

    fun setRedPacketCount(count: Int) {
        if (mImgIds.isEmpty())
            return

            //获取红包原始图片
//            val randomIndex: Int = when {
//                i % 7 == 1 -> Random().nextInt(mImgIds.size - 1) + 1
//                else -> 0
//            }
            val url = "https://upstatic.qiecdn.com/upload/team_logo/9b2270787117caedaa472bc0b817759b.png"
//            val bitmap = Glide.with(context)
//                    .load(url)
//                    .asBitmap()
//                    .into(width, height)
//                    .get()

            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            var originalBitmap = if (resource!=null){
                                resource
                            }else{
                                val randomIndex = Random().nextInt(mImgIds.size)
                                BitmapFactory.decodeResource(resources, mImgIds[randomIndex])
                            }
                            if (originalBitmap!=null){
                                for (i in 0 until count) {
                                    //生成红包实体类
                                    val redPacket = RedPacket(context, originalBitmap, speed,
                                            maxSize, minSize, mWidth,
                                            true, i)
                                    //添加进入红包数组
                                    redPacketList.add(redPacket)
                                }
                            }
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            Log.e("RedPacketView","onLoadFailed${e?.printStackTrace()}")
                        }

                    })

    }

    /**
     * 暂停红包雨
     */
    fun pauseRain() {
        animator?.cancel()
        countDownTimer?.cancel()
        scale2PointDownTimer?.cancel()
        scale2Point1DownTimer?.cancel()
        scale2AlphaDownTimer?.cancel()
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
            if (!redPacket.isRedPacketStyle) {
                //将红包旋转redPacket.rotation角度后 移动到（redPacket.x，redPacket.y）进行绘制红包

                redMatrix?.let {
                    it.setTranslate((-redPacket.width / 2).toFloat(), (-redPacket.height / 2).toFloat())
                    if (redPacket.isRedPacketStyle) {
//                    it.postRotate(redPacket.rotation)
                    }
                    it.postTranslate(redPacket.width / 2 + redPacket.x, redPacket.height / 2 +
                            redPacket.y)
                    //绘制红包
                    canvas.drawBitmap(redPacket.bitmap!!, it, paint)
                }
            }

        }
        for (i in redPacketList.indices) {
            val redPacket = redPacketList[i]
            if (redPacket.isRedPacketStyle) {

                //将红包旋转redPacket.rotation角度后 移动到（redPacket.x，redPacket.y）进行绘制红包

                redMatrix?.let {
                    it.setTranslate((-redPacket.width / 2).toFloat(), (-redPacket.height / 2).toFloat())
                    if (redPacket.isRedPacketStyle) {
//                    it.postRotate(redPacket.rotation)
                    }
                    when {
                        redPacket.clickAnimation == RedPacket.ClickAnimation.SCALE -> it.postScale(1.2f, 1.2f)
                        redPacket.clickAnimation == RedPacket.ClickAnimation.SCALE_POINT -> it.postScale(0.5f, 0.5f)
                        redPacket.clickAnimation == RedPacket.ClickAnimation.SCALE_POINT1 -> it.postScale(0.1f, 0.1f)
                        redPacket.clickAnimation == RedPacket.ClickAnimation.SCALE_POINT2 -> it.postScale(0.01f, 0.01f)
                    }
                    it.postTranslate(redPacket.width / 2 + redPacket.x, redPacket.height / 2 +
                            redPacket.y)
                    //绘制红包
                    canvas.drawBitmap(redPacket.bitmap!!, it, paint)
                }
            }

        }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                //根据点击的坐标点，判断是否点击在红包的区域
                val redPacket = isRedPacketClick(motionEvent.x, motionEvent.y)
                if (redPacket != null) {
                    if (redPacket.isRedPacketStyle&&redPacket.clickAnimation==RedPacket
                                    .ClickAnimation.DEFAULT) {
                        showClickAnim(redPacket)
                        redPacket.clickAnimation = RedPacket.ClickAnimation.SCALE
                        //如果点击在红包上，重新设置起始位置，以及中奖属性
//                        redPacket.y = (0 - redPacket.height * (redPacket.index + 1)).toFloat()
                        redPacket.isRealRed = redPacket.isRealRedPacket
                        onRedPacketClickListener?.onRedPacketClickListener(redPacket)
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }

    private fun showClickAnim(redPacket: RedPacket) {
        val clickAnimDuration = 1 * 100L
        countDownTimer = object : CountDownTimer(clickAnimDuration, 100) {
            override fun onFinish() {
                if (clickStyle== CLICK_STYLE_SCALE){
                    scale2Point(redPacket,clickAnimDuration)
                    redPacket.clickAnimation = RedPacket.ClickAnimation.SCALE_POINT
                }else if (clickStyle == CLICK_STYLE_SCALE_ALPHA){
//                    scale2Alpha(redPacket,clickAnimDuration)
                    redPacket.clickAnimation = RedPacket.ClickAnimation.DEFAULT
                    var initY = (0 - redPacket.height * (redPacket.index + 1)).toFloat()
                    redPacket.y =initY+ redPacket.speed * clickAnimDuration
                }

            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }.start()
    }

    private fun scale2Alpha(redPacket: RedPacket, clickAnimDuration: Long) {
        val scalePointDuration = 2*100L
        val deltaDuration = clickAnimDuration+scalePointDuration
        scale2AlphaDownTimer = object : CountDownTimer(scalePointDuration, 100) {
            override fun onFinish() {
                redPacket.clickAnimation = RedPacket.ClickAnimation.DEFAULT
                var initY = (0 - redPacket.height * (redPacket.index + 1)).toFloat()
                redPacket.y =initY+ redPacket.speed * deltaDuration
            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }.start()
    }


    private fun scale2Point(redPacket: RedPacket, clickAnimDuration: Long) {
        val scalePointDuration = 2*100L
        val deltaDuration = clickAnimDuration+scalePointDuration
        scale2PointDownTimer = object : CountDownTimer(scalePointDuration, 100) {
            override fun onFinish() {
                scale2Point1(redPacket,deltaDuration)
                redPacket.clickAnimation = RedPacket.ClickAnimation.SCALE_POINT1
//                var initY = (0 - redPacket.height * (redPacket.index + 1)).toFloat()
//                redPacket.y =initY+ redPacket.speed * deltaDuration
            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }.start()
    }

    private fun scale2Point1(redPacket: RedPacket, duration: Long) {
        val scalePointDuration = 1*100L
        val deltaDuration = duration+scalePointDuration
        scale2Point1DownTimer = object : CountDownTimer(scalePointDuration, 100) {
            override fun onFinish() {
                scale2Point2(redPacket,deltaDuration)
                redPacket.clickAnimation = RedPacket.ClickAnimation.SCALE_POINT2
            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }.start()
    }
    private fun scale2Point2(redPacket: RedPacket, duration: Long) {
        val scalePointDuration = 1*100L
        val deltaDuration = duration+scalePointDuration
        scale2Point1DownTimer = object : CountDownTimer(scalePointDuration, 100) {
            override fun onFinish() {
                redPacket.clickAnimation = RedPacket.ClickAnimation.DEFAULT
                var initY = (0 - redPacket.height * (redPacket.index + 1)).toFloat()
                redPacket.y =initY+ redPacket.speed * deltaDuration
            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }.start()
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

///**
// * description ：
// * author : chenchenggui
// * creation date: 2018/11/27
// */
//class RedPacketView : View {
//    private var mImgIds = intArrayOf(R.drawable.img_rain_red_packet1, R.drawable
//            .img_rain_red_packet2, R.drawable.img_rain_red_packet3,
//            R.drawable.img_rain_red_packet4)//红包图片
//    private var count: Int = 0//红包数量
//    private var speed: Int = 0//下落速度
//    private var maxSize: Float = 0f//红包大小的范围
//    private var minSize: Float = 0f//红包大小的范围
//    private var mWidth: Int = 0//view宽度
//    private var animator: ValueAnimator? = null//属性动画，用该动画来不断改变红包下落的坐标值
//
//    private var paint: Paint? = null//画笔
//    private var prevTime: Long = 0L
//    private val redPacketList = ArrayList<RedPacket>()//红包数组
//    private var redMatrix: Matrix? = null
//
//    private var onRedPacketClickListener: OnRedPacketClickListener? = null
//
//    constructor(context: Context) : super(context) {
//        init()
//    }
//
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RedPacketView)
//        count = typedArray.getInt(R.styleable.RedPacketView_count, 20)
//        speed = typedArray.getInt(R.styleable.RedPacketView_speed, 500)
//        minSize = typedArray.getFloat(R.styleable.RedPacketView_min_size, 0.5f)
//        maxSize = typedArray.getFloat(R.styleable.RedPacketView_max_size, 1.2f)
//        typedArray.recycle()
//        init()
//    }
//
//
//    /**
//     * 初始化
//     */
//    private fun init() {
//        //初始化画笔
//        paint = Paint().apply {
//            isFilterBitmap = true
//            isDither = true
//            isAntiAlias = true
//        }
//        redMatrix = Matrix()
//
//        //绘制view开启硬件加速
//        setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        //初始化属性动画
//        initAnimator()
//    }
//
//    private fun initAnimator() {
//        //创建一个属性动画，通过属性动画来控制刷新红包下落的位置
//        animator = ValueAnimator.ofFloat(0f, 1f).apply {
//            //每次动画更新的时候，更新红包下落的坐标值
//            addUpdateListener {
//                val nowTime = System.currentTimeMillis()
//                val secs = (nowTime - prevTime).toFloat() / 1000f
//                prevTime = nowTime
//
//                for (i in redPacketList.indices) {
//                    val redPacket = redPacketList[i]
//                    //更新红包的下落的位置y
//                    redPacket.y += redPacket.speed * secs
////                    redPacket.x -= redPacket.speed * secs / 10
//
//                    //如果y坐标大于view的高度或者x坐标小于负红包宽度 说明划出屏幕，重新设置x,y起始位置，以及中奖属性
//                    if (redPacket.y > height || redPacket.x < -redPacket.width) {
//                        val random = Random()
//                        val rx = random.nextInt(mWidth) + 100 - redPacket.width
////                        Log.e("red_packet", "rx=$rx")
////                        redPacket.x = (if (rx <= 0) 100 else rx).toFloat()
//                        redPacket.y = (0 - redPacket.height).toFloat()
//                        redPacket.isRealRed = redPacket.isRealRedPacket
//                    }
//                    //更新红包的旋转的角度
//                    redPacket.rotation += redPacket.rotationSpeed * secs
//                }
//                invalidate()
//            }
//
//            //属性动画无限循环
//            repeatCount = ValueAnimator.INFINITE
//            //属性值线性变换
//            interpolator = LinearInterpolator()
//            duration = 2000
//        }
//    }
//
//
//    /**
//     * 停止动画
//     */
//    fun stopRainNow() {
//        //清空红包数据
//        clear()
//        //重绘
//        invalidate()
//        //动画取消
//        animator?.cancel()
//    }
//
//
//    /**
//     * 开始动画
//     */
//    fun startRain() {
//        //清空红包数据
//        clear()
//        //添加红包
//        setRedPacketCount(count)
//        prevTime = System.currentTimeMillis()
//        //动画开始
//        animator?.start()
//
//    }
//
//    fun setRedPacketCount(count: Int) {
//        if (mImgIds.isEmpty())
//            return
//        for (i in 0 until count) {
//            //获取红包原始图片
////            val randomIndex: Int = when {
////                i < 3 -> 1
////                i < 9 -> Random().nextInt(mImgIds.size - 2) + 2
////                else -> 0
////            }
//            val randomIndex = Random().nextInt(mImgIds.size)
//            val originalBitmap = BitmapFactory.decodeResource(resources, mImgIds[randomIndex])
//            //生成红包实体类
//            val redPacket = RedPacket(context, originalBitmap, speed, maxSize, minSize, mWidth,
//                    true)
//            //添加进入红包数组
//            redPacketList.add(redPacket)
//        }
//    }
//
//    /**
//     * 暂停红包雨
//     */
//    fun pauseRain() {
//        animator?.cancel()
//    }
//
//    /**
//     * 重新开始
//     */
//    fun restartRain() {
//        animator?.start()
//    }
//
//    /**
//     * 清空红包数据，并回收红包中的bitmap
//     */
//    private fun clear() {
//        for (redPacket in redPacketList) {
//            redPacket.recycle()
//        }
//        redPacketList.clear()
//    }
//
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        //获取自定义view的宽度
//        mWidth = measuredWidth
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        //遍历红包数组，绘制红包
//        for (i in redPacketList.indices) {
//            val redPacket = redPacketList[i]
//            if (!redPacket.isRedPacketStyle) {
//                //将红包旋转redPacket.rotation角度后 移动到（redPacket.x，redPacket.y）进行绘制红包
//
//                redMatrix?.let {
//                    it.setTranslate((-redPacket.width / 2).toFloat(), (-redPacket.height / 2).toFloat())
//                    if (redPacket.isRedPacketStyle) {
////                    it.postRotate(redPacket.rotation)
//                    }
//                    it.postTranslate(redPacket.width / 2 + redPacket.x, redPacket.height / 2 +
//                            redPacket.y)
//                    //绘制红包
//                    canvas.drawBitmap(redPacket.bitmap!!, it, paint)
//                }
//            }
//
//        }
//        for (i in redPacketList.indices) {
//            val redPacket = redPacketList[i]
//            if (redPacket.isRedPacketStyle) {
//
//                //将红包旋转redPacket.rotation角度后 移动到（redPacket.x，redPacket.y）进行绘制红包
//
//                redMatrix?.let {
//                    it.setTranslate((-redPacket.width / 2).toFloat(), (-redPacket.height / 2).toFloat())
//                    if (redPacket.isRedPacketStyle) {
////                    it.postRotate(redPacket.rotation)
//                    }
//                    it.postTranslate(redPacket.width / 2 + redPacket.x, redPacket.height / 2 +
//                            redPacket.y)
//                    //绘制红包
//                    canvas.drawBitmap(redPacket.bitmap!!, it, paint)
//                }
//            }
//
//        }
//    }
//
//    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
//        when (motionEvent.action) {
//            MotionEvent.ACTION_DOWN -> {
//                //根据点击的坐标点，判断是否点击在红包的区域
//                val redPacket = isRedPacketClick(motionEvent.x, motionEvent.y)
//                if (redPacket != null) {
//                    if (redPacket.isRedPacketStyle) {
//                        //如果点击在红包上，重新设置起始位置，以及中奖属性
//                        redPacket.y = (0 - redPacket.height).toFloat()
//                        redPacket.isRealRed = redPacket.isRealRedPacket
//                        onRedPacketClickListener?.onRedPacketClickListener(redPacket)
//                    }
//                }
//            }
//            MotionEvent.ACTION_MOVE -> {
//            }
//            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
//            }
//        }
//        return true
//    }
//
//    //根据点击坐标点，遍历红包数组，看是否点击在红包上
//    private fun isRedPacketClick(x: Float, y: Float): RedPacket? {
//        for (i in redPacketList.indices.reversed()) {
//            if (redPacketList[i].isContains(x, y)) {
//                return redPacketList[i]
//            }
//        }
//        return null
//    }
//
//    interface OnRedPacketClickListener {
//        fun onRedPacketClickListener(redPacket: RedPacket)
//    }
//
//    fun setOnRedPacketClickListener(onRedPacketClickListener: OnRedPacketClickListener) {
//        this.onRedPacketClickListener = onRedPacketClickListener
//    }
//}

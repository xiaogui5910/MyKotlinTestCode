package com.example.chenchenggui.mykotlintestcode.redpacket

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.util.*

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/11/27
 */
class RedPacket(context: Context, originalBitmap: Bitmap, speed: Int, maxSize: Float, minSize:
Float, viewWidth: Int, var isRedPacketStyle: Boolean) {
    var x: Float = 0f
    var y: Float = 0f
    var rotation: Float = 0f
    var speed: Float = 0f
    var rotationSpeed: Float = 0f
    var width: Int = 0
    var height: Int = 0
    var bitmap: Bitmap? = null
    var money: Int = 0
    var isRealRed: Boolean = false
    var prizeName:String=""
    var prizeType:PrizeType=PrizeType.EMPTY

    enum class PrizeType{
        MONEY,COUPON,EMPTY
    }
    /**
     * 随机 是否为中奖红包
     */
    //如果[1,10]随机出的数字是2的倍数 为中奖红包
    //中奖金额
    val isRealRedPacket: Boolean
        get() {
            if (isRedPacketStyle){
                val random = Random()
                val num = random.nextInt(10) + 1
                if (num % 2 == 0) {
                    if (num<=5){
                        prizeType=PrizeType.MONEY
                        prizeName="我的红包"
                    }else{
                        prizeType=PrizeType.COUPON
                        prizeName="你的优惠券"
                    }
                    money = num * 2
                    return true
                }
            }
            return false
        }

    init {
        //获取一个显示红包大小的倍数
        var widthRandom = Math.random()
        if (widthRandom < minSize || widthRandom > maxSize) {
            widthRandom = maxSize.toDouble()
        }
//        if (isRedPacketStyle){
            widthRandom=0.6
//        }
        Log.e("red_packet","widthRandom=$widthRandom")
        //红包的宽度
        width = (originalBitmap.width * widthRandom).toInt()
        //红包的高度
        height = width * originalBitmap.height / originalBitmap.width
        Log.e("red_packet","width=$width---height=$height")
        val mWidth = if (viewWidth == 0) context.resources.displayMetrics.widthPixels else viewWidth
        //生成红包bitmap
        bitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true)
        originalBitmap.recycle()
        val random = Random()
        //红包起始位置x:[0,mWidth-width]
        val rx = random.nextInt(mWidth)+100 - width
        Log.e("red_packet","rx=$rx")
        x = (if (rx <= 0) 100 else rx).toFloat()
        //红包起始位置y
        y = (-height).toFloat()
        //初始化该红包的下落速度
        this.speed = speed + Math.random().toFloat() * 1000
        //初始化该红包的初始旋转角度
        rotation = Math.random().toFloat() * 180 - 90
        //初始化该红包的旋转速度
        rotationSpeed = Math.random().toFloat() * 90 - 45
        //初始化是否为中奖红包
        isRealRed = isRealRedPacket
        Log.e("red_packet","isRealRed=$isRealRed")
    }

    /**
     * 判断当前点是否包含在区域内
     */
    fun isContains(x: Float, y: Float): Boolean {
        //稍微扩大下点击的区域
        return (this.x - 50 < x && this.x + 50f + width.toFloat() > x
                && this.y - 50 < y && this.y + 50f + height.toFloat() > y)
    }

    /**
     * 回收图片
     */
    fun recycle() {
        if (bitmap != null && !bitmap!!.isRecycled) {
            bitmap!!.recycle()
        }
    }
}


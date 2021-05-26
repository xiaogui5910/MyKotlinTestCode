package com.example.chenchenggui.mykotlintestcode.voicechat.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.dp2px
import com.example.chenchenggui.mykotlintestcode.voicechat.bean.AnimationBean
import kotlinx.android.synthetic.main.widget_voice_chat.view.*
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask
import kotlin.math.floor


/**
 * @description ：语音聊天主页
 * @author : chenchenggui
 * @date: 5/20/21
 */
class VoiceChatWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 礼物列表
     */
    var giftList = ArrayList<AnimationBean>()

    /**
     * 嘉宾列表
     */
    var adapter: VoiceChatGuestAdapter? = null

    /**
     * 礼物定时器  用于清除和从礼物队列中取礼物
     */
    private var giftClearTimer: Timer? = null

    private var gridLayoutManager: GridLayoutManager? = null
    private val handler = MyHandler(this)

    private var hostBean: AnimationBean? = null

    companion object {
        /**
         * 礼物定时器执行时间
         */
        const val GIFT_CLEAR_TIMER_INTERVAL = 3000

        /**
         * 清除礼物
         */
        const val HANDLER_WHAT_GIFT_CLEAR = 1
    }

    private class MyHandler constructor(widget: VoiceChatWidget) : Handler() {
        private val weakReference: WeakReference<VoiceChatWidget> = WeakReference<VoiceChatWidget>(widget)
        override fun handleMessage(msg: Message) {
            if (msg.what == 1) {
                val widget: VoiceChatWidget? = weakReference.get()
                widget?.clearGiftAndAddNewGift()
            }
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_voice_chat, this)
        initView()
        initData()
    }

    private fun initView() {
        for (i in 0..7) {
            val view = TextView(context)
            val p = LinearLayout.LayoutParams(dp2px(context, 30f).toInt(), dp2px(context, 30f).toInt())
            p.rightMargin = dp2px(context, 5f).toInt()
            view.layoutParams = p
            view.setBackgroundColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            view.text = (i + 1).toString()
            ll_send_gift_parent.addView(view)

            view.setOnClickListener {
                if (adapter != null) {
                    for ((index, item) in adapter!!.data.withIndex()) {
                        item.isHasGift = i == index
                    }
                    hostBean?.isHasGift = false
                }
                startGiftAnimation()
            }
        }

        gridLayoutManager = GridLayoutManager(context, 4)
        rv_guest.layoutManager = gridLayoutManager
        adapter = VoiceChatGuestAdapter(R.layout.layout_item_voice_chat, null)
        rv_guest.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val drawable = ContextCompat.getDrawable(context!!, R.drawable.item_voice_chat_divider)
        dividerItemDecoration.setDrawable(drawable!!)
        rv_guest.addItemDecoration(dividerItemDecoration)

        initListener()
    }

    private fun initListener() {
        //所有用户都送礼
        btn_send_gift_all.setOnClickListener {
            if (adapter != null) {
                for (item in adapter!!.data) {
                    item.isHasGift = item.isHasUse
                }
                hostBean?.isHasGift = true
            }
            startGiftAnimation()
        }
        //主持人送礼
        btn_send_gift_main.setOnClickListener {
            if (adapter != null) {
                for (item in adapter!!.data) {
                    item.isHasGift = false
                }
                hostBean?.isHasGift = true
            }
            startGiftAnimation()
        }

        //轮盘
        btn_marquee.setOnClickListener {
            if (adapter != null) {
                for (item in adapter!!.data) {
                    item.isShowPlay = false
                }
                adapter!!.notifyDataSetChanged()
            }

            startMarqueeAnim()
        }
        //爆灯/请求中等效果
        btn_light.setOnClickListener {
            if (adapter != null) {
                for (item in adapter!!.data) {
                    item.isShowPlay = true
                }
                adapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun startMarqueeAnim() {
        val fastDuration = 0.1f
        val slowDuration = 0.3f
        val slowRound = 1
        val stayDuration = 2f
        val guestNum = 8

        //TODO 跑马灯测试 8人上麦 第6中奖
        val testTime = 6.5f
        val selectedIndex = 5

        //慢速时长
        val slowTime = slowDuration * guestNum * (slowRound - 1) + slowDuration * (selectedIndex + 1)
        //快速剩余可用时长
        val fastDeltaTime = testTime - stayDuration - slowTime
        //快速一轮时长
        val fastRoundTime = fastDuration * guestNum
        //快速剩余可用时长最多可以走轮数
        val maxRoundNum = floor((fastDeltaTime / fastRoundTime).toDouble())
        //快速时长
        val fastTime = maxRoundNum * fastRoundTime
        //快速时长完整轮数后剩余时长
        val deltaTime = fastDeltaTime - fastTime
        //最后爆灯停留时长
        val stayTime = stayDuration + deltaTime
    }

    private fun initData() {
        //主持人嘉宾
        hostBean = AnimationBean().apply {
            isHasUse = true
            isHasGift = true
        }

        val dataList = ArrayList<AnimationBean>()
        for (i in 0..7) {
            val bean = AnimationBean()
            bean.type = i
            bean.position = i + 1
            if (i <= 3) {
                bean.isHasUse = true
                bean.isShowPlay = true
                bean.isHasGift = true
            }
            bean.isMicClose = i == 3
            bean.isLock = i == 7
            bean.name = "嘉宾${i + 1}号"
            dataList.add(bean)
        }
        adapter?.setNewInstance(dataList)

    }

    private fun addAnimationInfo(animationBean: AnimationBean?) {
        animationBean?.let {
            giftList.add(animationBean)
            startGiftTimer()
        }
    }

    private fun startGiftTimer() {
        giftClearTimer = Timer()
        giftClearTimer?.schedule(timerTask {
            handler.sendEmptyMessage(HANDLER_WHAT_GIFT_CLEAR)
        }, 0L, GIFT_CLEAR_TIMER_INTERVAL.toLong())
    }

    /**
     * 清除礼物列表中礼物,添加新礼物
     */
    private fun clearGiftAndAddNewGift() {
        //TODO 清除礼物
        rl_gift_animation_container.removeAllViews()

        startGiftAnimation()
    }

    /**
     * 开始礼物动画
     */
    private fun startGiftAnimation() {
        val animatorSet = AnimatorSet()
        for (i in 0..rv_guest.childCount) {
            val giftView = LayoutInflater.from(context).inflate(R.layout.view_gift_animation, null)

            //TODO 送给所有人和单独嘉宾 上麦状态:申请中,连接中
            if (adapter != null && i < rv_guest.childCount) {
                val bean = adapter!!.data[i]
                if (!bean.isHasGift) {
                    continue
                }
            }
            if (i == rv_guest.childCount && hostBean != null && !hostBean!!.isHasGift) {
                continue
            }
            rl_gift_animation_container.addView(giftView)

            //礼物出现后放大动画
            val scaleInAnimatorX = ObjectAnimator.ofFloat(giftView, "scaleX", 0f, 1f, 1f)
            val scaleInAnimatorY = ObjectAnimator.ofFloat(giftView, "scaleY", 0f, 1f, 1f)
            scaleInAnimatorX.duration = 1200
            scaleInAnimatorX.interpolator = OvershootInterpolator()
            scaleInAnimatorY.duration = 1200
            scaleInAnimatorY.interpolator = OvershootInterpolator()
            //放大动画
            animatorSet.play(scaleInAnimatorX).with(scaleInAnimatorY)

            scaleInAnimatorX.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    //放大结束后进入平移缩小动画
                    if (i == rv_guest.childCount) {
                        startGiftTranslationAnim(giftView, view_main)
                    } else {
                        startGiftTranslationAnim(giftView, rv_guest.getChildAt(i))
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })
        }
        //开始动画
        animatorSet.start()

    }

    /**
     * 开始礼物平移缩放动画
     */
    private fun startGiftTranslationAnim(animView: View, destView: View) {
        val scaleRate = 0.6f
        val duration = 500L
        val deltaX = destView.width / 2 - animView.width / 2
        val deltaY = destView.height / 2 - animView.height / 2

        val location = getViewLocation(animView)
        val destLocation = getViewLocation(destView)
        val toX = destLocation[0] - location[0] + deltaX
        val toY = destLocation[1] - location[1] + deltaY

        val translateAnimatorX = ObjectAnimator.ofFloat(animView, "translationX", 0f, toX.toFloat())
        val translateAnimatorY = ObjectAnimator.ofFloat(animView, "translationY", 0f, toY.toFloat())
        val scaleAnimatorX = ObjectAnimator.ofFloat(animView, "scaleX", 1f, scaleRate)
        val scaleAnimatorY = ObjectAnimator.ofFloat(animView, "scaleY", 1f, scaleRate)
        translateAnimatorX.duration = duration
        translateAnimatorY.duration = duration
        scaleAnimatorX.duration = duration
        scaleAnimatorY.duration = duration

        val scaleOutAnimatorX = ObjectAnimator.ofFloat(animView, "scaleX", scaleRate, scaleRate, 0f)
        val scaleOutAnimatorY = ObjectAnimator.ofFloat(animView, "scaleY", scaleRate, scaleRate, 0f)
        scaleOutAnimatorX.duration = 1500
        scaleOutAnimatorY.duration = 1500

        val animatorSet = AnimatorSet()
        //先平移和缩小,后缩小消失
        animatorSet.playTogether(translateAnimatorX, translateAnimatorY, scaleAnimatorX, scaleAnimatorY)
        animatorSet.play(scaleOutAnimatorX).with(scaleOutAnimatorY).after(translateAnimatorX)
        animatorSet.start()
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                rl_gift_animation_container.removeAllViews()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }

    /**
     * 获取view在window的位置坐标
     */
    private fun getViewLocation(view: View): IntArray {
        val location = IntArray(2)
        val location1 = IntArray(2)
        view.getLocationInWindow(location)
        view.getLocationOnScreen(location1)
        return location
    }

    inner class VoiceChatGuestAdapter(layoutId: Int, dataList: ArrayList<AnimationBean>?)
        : BaseQuickAdapter<AnimationBean, BaseViewHolder>(layoutId, dataList) {
        override fun convert(helper: BaseViewHolder, item: AnimationBean) {
//            val position = helper.adapterPosition
//            val layoutParams = helper.itemView.layoutParams as ViewGroup.MarginLayoutParams
//            layoutParams.leftMargin = dp2px(context, 10F).toInt()
//            layoutParams.rightMargin = dp2px(context, 10F).toInt()
//            helper.itemView.layoutParams = layoutParams
//            val spanCount = (recyclerView.layoutManager as GridLayoutManager).spanCount
//
//            if (position%spanCount==0) {
//                layoutParams.leftMargin = 0
//                layoutParams.rightMargin = dp2px(context, 5F).toInt()
//            } else if (position%spanCount==spanCount-1) {
//                layoutParams.leftMargin =  dp2px(context, 5F).toInt()
//                layoutParams.rightMargin =0
//            }
//            else  {
//                layoutParams.leftMargin = dp2px(context, 2.5F).toInt()
//                layoutParams.rightMargin = dp2px(context, 2.5F).toInt()
//            }
//            helper.itemView.layoutParams = layoutParams
            helper.getView<VoiceChatItemView>(R.id.item_view).updateItemView(item)
//            helper.setText(R.id.tv_item_voice_chat_name, "嘉宾${position + 1}号")
        }

    }


}
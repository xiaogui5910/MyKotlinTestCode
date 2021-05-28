package com.example.chenchenggui.mykotlintestcode.voicechat.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.dp2px
import com.example.chenchenggui.mykotlintestcode.voicechat.bean.AnimationBean
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.item_voice_chat.view.*
import org.jetbrains.anko.backgroundResource
import java.lang.ref.WeakReference

/**
 * @description ：嘉宾ItemView
 * @author : chenchenggui
 * @date: 5/20/21
 */
class VoiceChatItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        /**
         * 清除跑马灯动画
         */
        const val HANDLER_WHAT_MARQUEE_ANIM_CLEAR = 1

        /**
         * 气泡消失
         */
        const val HANDLER_WHAT_POPUP_WINDOW_DISMISS = 2

        /**
         * 展示骰子结果
         */
        const val HANDLER_WHAT_SHOW_DICE_RESULT = 3

        /**
         * 隐藏骰子结果
         */
        const val HANDLER_WHAT_HIDE_DICE_RESULT = 4
    }

    private var popupWindow: PopupWindow? = null
    private var animationDrawable: AnimationDrawable? = null
    private val handler = MyHandler(this)

    private class MyHandler constructor(widget: VoiceChatItemView) : Handler() {
        private val weakReference: WeakReference<VoiceChatItemView> = WeakReference<VoiceChatItemView>(widget)
        override fun handleMessage(msg: Message) {
            val widget: VoiceChatItemView? = weakReference.get()
            when (msg.what) {
                HANDLER_WHAT_MARQUEE_ANIM_CLEAR -> {
                    widget?.clearAnim()
                }
                HANDLER_WHAT_POPUP_WINDOW_DISMISS -> {
                    widget?.hideGuestChatWords()
                }
                HANDLER_WHAT_SHOW_DICE_RESULT -> {
                    widget?.showDiceResult()
                }
                HANDLER_WHAT_HIDE_DICE_RESULT -> {
                    widget?.hideDiceResult()
                }
            }
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_voice_chat, this)
        initView()
        initData()
    }

    private fun initView() {

    }

    private fun initData() {

    }

    fun updateItemView(bean: AnimationBean) {
        updateFaceViewAnim(bean)
        //item图片/背景
        updateIcon(bean)
        //TODO 贵族 昵称
        tv_item_voice_chat_name.text = bean.name

        //标签页
        if (bean.isHasUse) {
            val tagId = getDrawableResId("icon_guest_tag_${bean.position}_yellow")
            iv_item_voice_chat_num.setImageResource(tagId)
        } else {
            val tagId = getDrawableResId("icon_guest_tag_${bean.position}_gray")
            iv_item_voice_chat_num.setImageResource(tagId)
        }
        if (bean.isMicClose) {
            iv_item_voice_chat_num.setImageResource(R.drawable.icon_guest_mic_close)
        }

        //位置状态
        iv_item_voice_chat_status.visibility = if (bean.isHasUse) GONE else VISIBLE
        if (bean.isLock) {
            iv_item_voice_chat_status.setImageResource(R.drawable.icon_guest_lock)
        } else {
            iv_item_voice_chat_status.setImageResource(R.drawable.icon_guest_add)
        }

        //用户状态
        tv_item_voice_chat_status.text = if (bean.type == 2) "申请中..." else "暂离"
        tv_item_voice_chat_status.visibility = if (bean.type == 2) VISIBLE else GONE
    }

    fun updateIcon(bean: AnimationBean) {
        val icon = findViewById<SimpleDraweeView>(R.id.iv_item_voice_chat_img)
        if (bean.isHasUse) {
            icon.background = ContextCompat.getDrawable(context, R.drawable.shape_voice_chat_default_bg_yellow)
            icon.setActualImageResource(R.drawable.img_viewstub_test4)
        } else {
            icon.background = ContextCompat.getDrawable(context, R.drawable.shape_voice_chat_default_bg_gray)
            icon.setImageDrawable(null)
        }
    }

    /**
     * 根据图片名字获取资源id
     */
    private fun getDrawableResId(resIdName: String): Int {
        return resources.getIdentifier(resIdName, "drawable", context.packageName)
    }

    fun updateMarqueeBg(isShow: Boolean) {
        iv_item_voice_chat_marquee_shade.visibility = if (isShow) VISIBLE else GONE
    }

    fun showMarqueeAnim(stayTime: Double) {
        val bean = AnimationBean()
        bean.isHasUse = true
        bean.isShowPlay = true
        bean.type = 4
        updateFaceViewAnim(bean)
        handler.sendEmptyMessageDelayed(HANDLER_WHAT_MARQUEE_ANIM_CLEAR, (stayTime * 1000).toLong())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnim()
        clearDice()
    }

    fun clearAnim() {
        hideFaceView(iv_voice_chat_face_bottom)
        hideFaceView(iv_voice_chat_face_top)
        handler.removeCallbacksAndMessages(null)
    }

    private fun updateFaceViewAnim(bean: AnimationBean) {
        //互动表情
        val faceView = getFaceAnimView(bean.type)
        //遮罩层
        iv_item_voice_chat_shade.visibility = if (bean.type == 3 || bean.type == 2) VISIBLE else GONE

        //TODO 获取玩法表情json
        faceView.clearAnimation()
        if (bean.isHasUse && bean.isShowPlay) {
            val fileNameArray = getFaceJsonNameStr(bean.type)
            faceView.setAnimation(fileNameArray[0])
            faceView.imageAssetsFolder = fileNameArray[1]
            showFaceView(faceView)
        } else {
            hideFaceView(iv_voice_chat_face_bottom)
            hideFaceView(iv_voice_chat_face_top)
        }

    }

    private fun getFaceAnimView(type: Int): LottieAnimationView {
        return when (type) {
            1, 2, 3 -> {
                iv_voice_chat_face_top
            }
            else -> {
                iv_voice_chat_face_bottom
            }
        }
    }

    private fun getFaceJsonNameStr(type: Int): Array<String?> {
        val fileArray = arrayOfNulls<String>(2)
        when (type) {
            0 -> {
                fileArray[0] = "voicechat/voice_chat_speaking.json"
                fileArray[1] = "voicechat/speak/"
            }
            1 -> {
                fileArray[0] = "voicechat/voice_chat_lighting.json"
                fileArray[1] = "voicechat/light/"
            }
            2 -> {
                fileArray[0] = "voicechat/voice_chat_requesting.json"
                fileArray[1] = "voicechat/request/"
            }
            3 -> {
                fileArray[0] = "voicechat/voice_chat_waiting.json"
                fileArray[1] = "voicechat/wait/"
            }
            4 -> {
                fileArray[0] = "voicechat/voice_chat_marquee.json"
                fileArray[1] = "voicechat/marquee/"
            }
            else -> {
                fileArray[0] = "voicechat/voice_chat_speaking.json"
                fileArray[1] = "voicechat/speak/"
            }
        }
        return fileArray
    }

    fun showFaceView(animView: LottieAnimationView) {
        animView.visibility = VISIBLE
        if (!animView.isAnimating) {
            animView.playAnimation()
        }
    }

    fun hideFaceView(animView: LottieAnimationView) {
        animView.visibility = INVISIBLE
        animView.pauseAnimation()
        animView.cancelAnimation()
        animView.frame = 0
    }

    /**
     * 展示嘉宾发言气泡
     */
    fun showGuestChatWords(words: String?) {
        if (popupWindow != null && popupWindow!!.isShowing) {
            handler.removeMessages(HANDLER_WHAT_POPUP_WINDOW_DISMISS)
            popupWindow?.dismiss()
        }

        val voiceContentView = LayoutInflater.from(context).inflate(R.layout.voice_chat_pop_window, null)
        val tvWords = voiceContentView.findViewById<TextView>(R.id.tv_voice_chat_pop_window)
        tvWords.text = words
        popupWindow = PopupWindow(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            contentView = voiceContentView
            isFocusable = true
            isOutsideTouchable = true
        }

        voiceContentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val popupWidth: Int = voiceContentView.measuredWidth
        val popupHeight: Int = voiceContentView.measuredHeight
        val location = IntArray(2)
        val v = iv_item_voice_chat_img
        v.getLocationInWindow(location)
        popupWindow?.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.width / 2) - popupWidth / 2,
                location[1] - popupHeight + dp2px(context, 6f).toInt())

        handler.sendEmptyMessageDelayed(HANDLER_WHAT_POPUP_WINDOW_DISMISS, 4 * 1000)
    }

    private fun hideGuestChatWords() {
        popupWindow?.dismiss()
        popupWindow = null
    }

    fun showDiceAnim() {
        iv_item_voice_chat_dice.visibility = VISIBLE
        iv_item_voice_chat_dice.backgroundResource = R.drawable.voice_chat_dice_anim
        animationDrawable = iv_item_voice_chat_dice.background as AnimationDrawable
        animationDrawable?.start()
        handler.sendEmptyMessageDelayed(HANDLER_WHAT_SHOW_DICE_RESULT, 3 * 1000)
    }

    fun showDiceResult() {
        if (animationDrawable != null && animationDrawable!!.isRunning) {
            animationDrawable!!.stop()
        }
        //TODO 结果传递
        iv_item_voice_chat_dice.backgroundResource = R.drawable.dice_3
        handler.sendEmptyMessageDelayed(HANDLER_WHAT_HIDE_DICE_RESULT, 3 * 1000)
    }

    fun hideDiceResult() {
        iv_item_voice_chat_dice.visibility = GONE
    }

    fun clearDice() {
        if (animationDrawable != null && animationDrawable!!.isRunning) {
            animationDrawable!!.stop()
        }
        iv_item_voice_chat_dice.visibility = GONE
    }
}
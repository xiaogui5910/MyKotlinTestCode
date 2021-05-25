package com.example.chenchenggui.mykotlintestcode.voicechat.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.voicechat.bean.AnimationBean
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.item_voice_chat.view.*

/**
 * @description ：嘉宾ItemView
 * @author : chenchenggui
 * @date: 5/20/21
 */
class VoiceChatItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
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
        //昵称
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

    private fun updateFaceViewAnim(bean: AnimationBean) {
        //互动表情
        val faceView = getFaceAnimView(bean.type)
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
}
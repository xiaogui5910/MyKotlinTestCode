package com.example.chenchenggui.mykotlintestcode.voicechat

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.dp2px
import kotlinx.android.synthetic.main.dialog_anchor_level_upgrade.*

/**
 * @description ：主播等级升级对话框
 * @author : chenchenggui
 * @date: 6/29/21
 */
class AnchorLevelUpgradeDialog : DialogFragment() {
    private var anchorLevel = 1

    companion object {
        fun newInstance(anchorLevel: Int):
                AnchorLevelUpgradeDialog {
            val dialogFragment = AnchorLevelUpgradeDialog()
            val bundle = Bundle()
            bundle.putInt("anchor_level", anchorLevel)
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            anchorLevel = it.getInt("anchor_level")
        }
//        setStyle(DialogFragment.STYLE_NORMAL, com.tencent.tv.qie.live.R.style.MyDialogFragmentStyleWithoutDim)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.dialog_anchor_level_upgrade, null)
    }

    override fun onStart() {
        super.onStart()
//        dialog.window.setGravity(Gravity.CENTER)
//        dialog.window.setLayout(dp2px(context!!,124f).toInt(), dp2px(context!!,54.5f).toInt())
        val window = dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        isCancelable = true
        val attributes = window?.attributes?.apply {
            gravity = Gravity.CENTER
            width = dp2px(context!!,124f).toInt()
            height = dp2px(context!!, 54.5f).toInt()
            dimAmount = 0f
            flags = flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        }
        window?.attributes = attributes
        initView()
    }


     fun initView() {
        tv_dialog_anchor_level_upgrade_tips.setOnClickListener {
            anchorLevel++
            startUpgradeAnim()

        }
        startUpgradeAnim()
    }

    private fun startUpgradeAnim() {
        val animOut = AnimationUtils.loadAnimation(context, R.anim.dialog_anchor_level_roll_out_alpha)
        val animIn = AnimationUtils.loadAnimation(context, R.anim.dialog_anchor_level_roll_in_alpha)
        val animInBg = AnimationUtils.loadAnimation(context, R.anim.dialog_anchor_level_bg_roll_in_alpha)

        animOut.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
//                if (anchorLevel<=25){
                val c =anchorLevel/10
                val c1 =(anchorLevel-1)/10
                if (c==c1){
                    val anchorLevelDrawable = AnchorLevelUtils.getAnchorLevelTextDrawable(context!!, anchorLevel)
                    iv_dialog_anchor_level.setImageResource(anchorLevelDrawable)
                    iv_dialog_anchor_level.startAnimation(animIn)
                }else{
                    val bglDrawable = AnchorLevelUtils.getAnchorLevelBgDrawable(context!!, anchorLevel)
                    iv_dialog_anchor_level_bg.setImageResource(bglDrawable)
                    iv_dialog_anchor_level_bg.startAnimation(animInBg)

                    val anchorLevelDrawable = AnchorLevelUtils.getAnchorLevelTextDrawable(context!!, anchorLevel)
                    iv_dialog_anchor_level.setImageResource(anchorLevelDrawable)
                    iv_dialog_anchor_level.startAnimation(animIn)
                }
//                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
//        if (anchorLevel<=25){
            val anchorLevelDrawable = AnchorLevelUtils.getAnchorLevelTextDrawable(context!!, anchorLevel - 1)
            iv_dialog_anchor_level.setImageResource(anchorLevelDrawable)
            iv_dialog_anchor_level.startAnimation(animOut)
//        }
    }

}
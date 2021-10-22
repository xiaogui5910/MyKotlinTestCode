package com.example.chenchenggui.mykotlintestcode.dialog

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.dp2px
import kotlinx.android.synthetic.main.dialog_egg_result.*

/**
 * @description ：
 * @author : chenchenggui
 * @date: 2021/10/11
 */
class EggResultDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.dialog_egg_result, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.shape_egg_bg_7
            )
        )

        isCancelable = true
        val attributes = window?.attributes?.apply {
            gravity = Gravity.CENTER
            width = dp2px(context!!, 270f).toInt()
//            height = dp2px(context!!, 230f).toInt()
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            dimAmount = 0.5f
            flags = flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        }
        window?.attributes = attributes
    }


    private fun initView() {
        tv_egg_agree.setOnClickListener { dismiss() }
    }

    private fun initData() {

    }
}
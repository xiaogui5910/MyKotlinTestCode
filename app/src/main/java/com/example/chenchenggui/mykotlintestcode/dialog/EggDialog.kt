package com.example.chenchenggui.mykotlintestcode.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.dp2px
import kotlinx.android.synthetic.main.dialog_egg.*
import kotlinx.android.synthetic.main.dialog_guess_list.*
import kotlinx.android.synthetic.main.dialog_guess_list.iv_close

/**
 * @description ：
 * @author : chenchenggui
 * @date: 2021/10/11
 */
class EggDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.dialog_egg, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        isCancelable = true
        val attributes = window?.attributes?.apply {
            gravity = Gravity.BOTTOM
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = dp2px(context!!, 473f).toInt()
            dimAmount = 0.5f
            flags = flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        }
        window?.attributes = attributes
    }


    private fun initView() {
        iv_close.setOnClickListener { dismiss() }
        iv_egg_1.setOnClickListener {
            EggResultDialog().show(this@EggDialog.childFragmentManager,"EggResultDialog")
        }
    }

    private fun initData() {

    }
}
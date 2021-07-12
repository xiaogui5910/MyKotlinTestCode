package com.example.chenchenggui.mykotlintestcode.activity

import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.voicechat.AnchorLevelUpgradeDialog
import kotlinx.android.synthetic.main.activity_function_test.*

class FunctionTestActivity  : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_function_test
    }

    override fun initView() {
        super.initView()
        btn_anchor_level.setOnClickListener {
            AnchorLevelUpgradeDialog.newInstance(26).show(supportFragmentManager,"AnchorLevelUpgradeDialog")
        }
    }
}
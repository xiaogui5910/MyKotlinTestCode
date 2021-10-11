package com.example.chenchenggui.mykotlintestcode.activity

import android.content.Intent
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.activity.community.CommunityDetailActivity
import com.example.chenchenggui.mykotlintestcode.activity.community.CommunityDetailHeaderActivity
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
        btn_community_detail.setOnClickListener {
            startActivity(Intent(this, CommunityDetailActivity::class.java))
        }
        btn_community_detail_header.setOnClickListener {
            startActivity(Intent(this, CommunityDetailHeaderActivity::class.java))
        }
    }
}
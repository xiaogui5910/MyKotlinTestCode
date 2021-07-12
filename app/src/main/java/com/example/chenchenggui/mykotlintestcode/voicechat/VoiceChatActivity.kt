package com.example.chenchenggui.mykotlintestcode.voicechat

import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.activity.BaseActivity

class VoiceChatActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_voice_chat
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_voice_chat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_count_4 -> {
            }
            R.id.item_count_6 -> {
            }
            R.id.item_count_8 -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initView() {
        super.initView()
    }
}
package com.example.chenchenggui.mykotlintestcode.avdev

import android.view.Menu
import android.view.MenuItem
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.activity.BaseActivity

class AudioVideoDevActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_audio_video_dev

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_av_dev, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.record -> {
                if (supportFragmentManager.fragments[0] is AudioRecordFragment) {
                    return super.onOptionsItemSelected(item)
                }
                val fragment = AudioRecordFragment()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.av_dev_container, fragment)
                ft.commit()
            }
            R.id.camera -> {
                val fragment = CameraFragment()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.av_dev_container, fragment)
                ft.commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initView() {
        super.initView()
        val fragment = AudioRecordFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.av_dev_container, fragment)
        ft.commit()
    }
}

package com.example.chenchenggui.mykotlintestcode.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.avdev.AudioRecordFragment
import com.example.chenchenggui.mykotlintestcode.avdev.CameraFragment
import com.example.chenchenggui.mykotlintestcode.avdev.OpenGLFragment
import com.example.chenchenggui.mykotlintestcode.fragment.GalleryRecyclerViewFragment
import com.example.chenchenggui.mykotlintestcode.fragment.GalleryViewPagerFragment

class ViewPagerGalleryActivity : BaseActivity()  {

    override fun getLayoutId(): Int {
        return R.layout.activity_view_pager_gallery
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gallery, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.viewpager -> {
                if (supportFragmentManager.fragments[0] is GalleryViewPagerFragment) {
                    return super.onOptionsItemSelected(item)
                }
                val fragment = GalleryViewPagerFragment()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fl_gallery_container, fragment)
                ft.commit()
            }
            R.id.recyclerview -> {
                if (supportFragmentManager.fragments[0] is GalleryRecyclerViewFragment) {
                    return super.onOptionsItemSelected(item)
                }
                val fragment = GalleryRecyclerViewFragment()
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fl_gallery_container, fragment)
                ft.commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initView() {
        super.initView()
        val fragment = GalleryViewPagerFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_gallery_container, fragment)
        ft.commit()
    }
}
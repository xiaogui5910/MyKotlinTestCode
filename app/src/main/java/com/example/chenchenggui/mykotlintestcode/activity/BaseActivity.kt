package com.example.chenchenggui.mykotlintestcode.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import com.example.chenchenggui.mykotlintestcode.R

/**
 * description ：
 * author : chenchenggui
 * creation date: 2019/3/28
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initToolbar()
        initView()
        initData()
    }

    abstract fun getLayoutId(): Int

    open fun initData() {
    }

    open fun initView() {
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar?>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getToolbarTitle()
    }

    open fun getToolbarTitle(): CharSequence? {
        return javaClass.simpleName
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
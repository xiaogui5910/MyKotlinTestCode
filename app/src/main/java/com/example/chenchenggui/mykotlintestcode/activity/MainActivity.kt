package com.example.chenchenggui.mykotlintestcode.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.TestFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var fragments: ArrayList<Fragment>
    lateinit var titles: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        titles = ArrayList()
        titles.add("one")
        titles.add("two")

        fragments = ArrayList()
        fragments.add(TestFragment())
        fragments.add(TestFragment())

        viewPager.adapter = TestPageAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        var toolbar = tool_bar
        setSupportActionBar(toolbar)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.pull_to_refresh->{}
            R.id.red_packet->{startActivity(Intent(this,RedPacketActivity::class.java))}
        }
        return super.onOptionsItemSelected(item)
    }

    inner class TestPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(p0: Int): Fragment {
            return fragments[p0]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}

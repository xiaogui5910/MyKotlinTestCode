package com.example.chenchenggui.mykotlintestcode

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

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

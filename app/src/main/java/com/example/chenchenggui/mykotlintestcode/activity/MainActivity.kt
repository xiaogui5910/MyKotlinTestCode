package com.example.chenchenggui.mykotlintestcode.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.TestFragment
import com.example.chenchenggui.mykotlintestcode.avdev.AudioVideoDevActivity
import com.example.chenchenggui.mykotlintestcode.di.TestDaggerActivity
import com.example.chenchenggui.mykotlintestcode.dialog.EggDialog
import com.example.chenchenggui.mykotlintestcode.dialog.GuessListDialog
import com.example.chenchenggui.mykotlintestcode.voicechat.VoiceChatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewPager: androidx.viewpager.widget.ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var fragments: ArrayList<androidx.fragment.app.Fragment>
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

        setSupportActionBar(tool_bar)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.pull_to_refresh->{}
            R.id.red_packet->{startActivity(Intent(this,RedPacketActivity::class.java))}
            R.id.expand_item->{startActivity(Intent(this,ExpandableItemActivity::class.java))}
            R.id.section_item->{startActivity(Intent(this,SectionActivity::class.java))}
            R.id.avdev->{startActivity(Intent(this, AudioVideoDevActivity::class.java))}
            R.id.webview->{startActivity(Intent(this,WebViewTestActivity::class.java))}
            R.id.viewStub->{startActivity(Intent(this,ViewStubTestActivity::class.java))}
            R.id.dagger->{startActivity(Intent(this,TestDaggerActivity::class.java))}
            R.id.scrollbar->{startActivity(Intent(this,TestScrollbarActivity::class.java))}
            R.id.coordinator->{startActivity(Intent(this,CoordinatorActivity::class.java))}
            R.id.guess->{
                GuessListDialog().show(supportFragmentManager,"GuessListDialog")
            }
            R.id.viewPagerGallery->{
                startActivity(Intent(this, ViewPagerGalleryActivity::class.java))
            }
            R.id.voiceChat->{
                startActivity(Intent(this, VoiceChatActivity::class.java))
            }
            R.id.function_test->{
                startActivity(Intent(this, FunctionTestActivity::class.java))
            }
            R.id.egg_video->{
                EggDialog().show(supportFragmentManager,"EggDialog")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class TestPageAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

        override fun getItem(p0: Int): androidx.fragment.app.Fragment {
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

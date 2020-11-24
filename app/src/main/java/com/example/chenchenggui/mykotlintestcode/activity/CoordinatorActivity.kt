package com.example.chenchenggui.mykotlintestcode.activity

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.fragment.NobleFragment
import kotlinx.android.synthetic.main.layout_noble_user.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


class CoordinatorActivity : BaseActivity() {
    val mTitleDataList = arrayOf("大乔", "小乔", "刘备", "曹操", "孙权")
    override fun getLayoutId(): Int {
        return R.layout.activity_coordingator
    }

    override fun initView() {
        super.initView()
        initPerson()
        initViewPager()
    }

    private fun initPerson() {
        val content1 = "2121"
        val content2 = "骑士"
        val showText = getString(R.string.show_upgrade_text, content1, content2)
        val startIndex1 = showText.indexOf(content1)
        val startIndex2 = showText.indexOf(content2)

        val lightColor = ContextCompat.getColor(this, R.color.noble_yellow_caa869)
        val textSpanned1 = SpannableString(showText)
        textSpanned1.setSpan(ForegroundColorSpan(lightColor),
                startIndex1, startIndex1 + content1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textSpanned1.setSpan(ForegroundColorSpan(lightColor),
                startIndex2, startIndex2 + content2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_noble_upgrade.text = textSpanned1
    }

    private fun initViewPager() {
        val mViewPager = findViewById<View>(R.id.view_pager) as ViewPager
        val fragmentList = arrayListOf<Fragment>()
        mTitleDataList.forEach {
            fragmentList.add(NobleFragment.newInstance(it))
        }
        val adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return fragmentList.size
            }

            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getItemPosition(`object`: Any): Int {
                return super.getItemPosition(`object`)
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitleDataList[position]
            }
        }
        mViewPager.adapter = adapter

        val magicIndicator = findViewById<View>(R.id.magic_indicator) as MagicIndicator
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mTitleDataList.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = Color.WHITE
                colorTransitionPagerTitleView.selectedColor = ContextCompat.getColor(context!!, R.color.noble_yellow_caa869)
                colorTransitionPagerTitleView.typeface = Typeface.DEFAULT_BOLD
                colorTransitionPagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                colorTransitionPagerTitleView.setText(mTitleDataList[index])
                colorTransitionPagerTitleView.setOnClickListener { mViewPager.currentItem = index }
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                return indicator
            }
        }
        commonNavigator.isAdjustMode = true
        magicIndicator.navigator = commonNavigator

        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    override fun initData() {
        super.initData()

    }
}
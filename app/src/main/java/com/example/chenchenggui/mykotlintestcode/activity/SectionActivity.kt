package com.example.chenchenggui.mykotlintestcode.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.log
import com.example.chenchenggui.mykotlintestcode.sectionitem.MySection
import com.example.chenchenggui.mykotlintestcode.sectionitem.SectionItemAdapter
import com.example.chenchenggui.mykotlintestcode.sectionitem.UserInfo
import com.example.chenchenggui.mykotlintestcode.toast
import kotlinx.android.synthetic.main.activity_section.*

class SectionActivity : AppCompatActivity() {

    lateinit var dataList: ArrayList<MySection>
    lateinit var adapter: SectionItemAdapter
    lateinit var rvExpand: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section)
        initToolbar()
        rvExpand = findViewById(R.id.rv_section)

        dataList = generateData()

        adapter = SectionItemAdapter(R.layout.item_section_content, R.layout.item_section_header,
                dataList)

        rvExpand.adapter = adapter
        rvExpand.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.setOnItemClickListener { adapter, view, position ->
            val mySection = adapter.getItem(position) as MySection
            if (!mySection.isHeader) {
                toast("onclick--$position--${mySection.t.title}")
            }
        }
        adapter.setOnItemChildClickListener { adapter, view, position ->
            val mySection = adapter.getItem(position) as MySection
            toast(mySection.header + "--position=$position")
            val dataList = adapter.data
            dataList.addAll(position, mySection.subList)
            dataList.removeAt(position+mySection.subList.size)
            adapter.setNewData(dataList)
            log(adapter.data.toString())
        }
    }

    private fun generateData(): ArrayList<MySection> {
        val list = ArrayList<MySection>()

        val lvCounts = arrayOf(5, 2, 4, 1, 6)

        val nameArr = arrayOf("篮球", "足球", "台球", "乒乓球", "羽毛球")
        for ((index, name) in nameArr.withIndex()) {
            val lv0Section = MySection(true, name, false)
            list.add(lv0Section)

            val level0Count = lvCounts[index]

            var subList: ArrayList<MySection> = ArrayList()
            var moreSection = MySection(true, "展开更多", true)
            for (j in 0 until level0Count) {
                if (j >= 2) {
                    if (j == 2) {
                        list.add(moreSection)
                    }

                    val subSection = MySection(UserInfo("name $j", "今日2场直播", ""))
                    subList.add(subSection)

                    if (j == level0Count - 1) {
                        moreSection.subList = subList
                    }
                } else {
                    val lv1Section = MySection(UserInfo("name $j", "今日2场直播", ""))
                    list.add(lv1Section)
                }
            }
        }
        return list
    }

    private fun initToolbar() {
        setSupportActionBar(tb_section)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "SectionItem"
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}

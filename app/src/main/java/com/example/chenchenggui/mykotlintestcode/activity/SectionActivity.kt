package com.example.chenchenggui.mykotlintestcode.activity

import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.log
import com.example.chenchenggui.mykotlintestcode.sectionitem.MySection
import com.example.chenchenggui.mykotlintestcode.sectionitem.SectionItemAdapter
import com.example.chenchenggui.mykotlintestcode.sectionitem.UserInfo
import com.example.chenchenggui.mykotlintestcode.toast

class SectionActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_section

    lateinit var dataList: ArrayList<MySection>
    lateinit var adapter: SectionItemAdapter
    lateinit var rvExpand: androidx.recyclerview.widget.RecyclerView

    override fun initView() {
        rvExpand = findViewById(R.id.rv_section)

        dataList = generateData()

        rvExpand.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        adapter = SectionItemAdapter(R.layout.item_section_content, R.layout.item_section_header,
                dataList)

        rvExpand.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val mySection = adapter.getItem(position) as MySection
            if (!mySection.isHeader) {
                toast("onclick--$position--${mySection.userInfo.title}")
            }
        }
        adapter.setOnItemChildClickListener { adapter, view, position ->
            val mySection = adapter.getItem(position) as MySection
            toast(mySection.header + "--position=$position")
            val dataList:ArrayList<MySection> = adapter.data as ArrayList<MySection>

            val newDataList =ArrayList<MySection>()
            newDataList.addAll(dataList)
            newDataList.addAll(position, mySection.subList)
            newDataList.removeAt(position + mySection.subList.size)
            adapter.setNewInstance(newDataList)
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
}

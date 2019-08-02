package com.example.chenchenggui.mykotlintestcode.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.expandableitem.ExpandableItemAdapter
import com.example.chenchenggui.mykotlintestcode.expandableitem.Level0Item
import com.example.chenchenggui.mykotlintestcode.expandableitem.Level1Item
import kotlinx.android.synthetic.main.activity_expandable_item.*
import java.util.*

class ExpandableItemActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_expandable_item

    lateinit var dataList: ArrayList<MultiItemEntity>
    lateinit var adapter: ExpandableItemAdapter
    lateinit var rvExpand: RecyclerView

    override fun initView() {

        rvExpand = findViewById(R.id.rv_expand)

        dataList = generateData()

        adapter = ExpandableItemAdapter(dataList)

        rvExpand.adapter = adapter
        rvExpand.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.expandAll()

//        adapter.setOnItemClickListener { adapter, view, position ->
//            toast("点击了$position")
//        }
//        adapter.collapse(3)
//        adapter.collapse(10)

        val nums = ArrayList<Int>()
        for ((index, item) in dataList.withIndex()) {
            if (item is Level1Item) {
                if (item.hasSubItem()) {
                    nums.add(index)
                }
            }
        }
        var k = 0
        nums.forEach {
            adapter.collapse(it - k)
            val item = dataList[it - k]
            val lv1 = item as Level1Item
            k += lv1.subItems.size
        }
    }


    private fun generateData(): ArrayList<MultiItemEntity> {
        val list = ArrayList<MultiItemEntity>()
        val lvCounts = arrayOf(5, 2, 4, 1, 6)

        val nameArr = arrayOf("篮球", "足球", "台球", "乒乓球", "羽毛球")
        for ((index, name) in nameArr.withIndex()) {
            val lv0 = Level0Item(name, "this is $index item", "")
            val level0Count = lvCounts[index]

            val lv1More = Level1Item("查看更多", "今日2场直播", "")
            for (j in 0 until level0Count) {
                if (j >= 2) {
                    val lv2 = Level1Item("name $j", "今日2场直播", "")
                    lv1More.addSubItem(lv2)

                    if (j == level0Count - 1) {
                        lv0.addSubItem(lv1More)
                    }
                } else {
                    val lv1 = Level1Item("name $j", "今日2场直播", "")
                    lv0.addSubItem(lv1)
                }
            }

            list.add(lv0)
        }

        return list
    }

}

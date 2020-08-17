package com.example.chenchenggui.mykotlintestcode.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.MenuItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.entity.node.BaseNode
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.expandableitem.ExpandableItemAdapter
import com.example.chenchenggui.mykotlintestcode.expandableitem.Level0Item
import com.example.chenchenggui.mykotlintestcode.expandableitem.Level1Item
import kotlinx.android.synthetic.main.activity_expandable_item.*
import java.util.*

class ExpandableItemActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_expandable_item

    lateinit var dataList: ArrayList<BaseNode>
    lateinit var adapter: ExpandableItemAdapter
    lateinit var rvExpand: androidx.recyclerview.widget.RecyclerView

    override fun initView() {

        rvExpand = findViewById(R.id.rv_expand)

        dataList = generateData()

        adapter = ExpandableItemAdapter()
        adapter.setList(dataList)

        rvExpand.adapter = adapter
        rvExpand.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

//        adapter.setOnItemClickListener { adapter, view, position ->
//            toast("点击了$position")
//        }
//        adapter.collapse(3)
//        adapter.collapse(10)

        val nums = ArrayList<Int>()
        for ((index, item) in dataList.withIndex()) {
            if (item is Level1Item) {
                if (item.childNode != null && item.childNode!!.size > 0) {
                    nums.add(index)
                }
            }
        }
        var k = 0
        nums.forEach {
            adapter.collapse(it - k)
            val item = dataList[it - k]
            val lv1 = item as Level1Item
            k += lv1.childNode!!.size
        }
    }


    private fun generateData(): ArrayList<BaseNode> {
        val list = ArrayList<BaseNode>()
        val lvCounts = arrayOf(5, 2, 4, 1, 6)

        val nameArr = arrayOf("篮球", "足球", "台球", "乒乓球", "羽毛球")
        for ((index, name) in nameArr.withIndex()) {
            val lv0 = Level0Item(name, "this is $index item", "", ArrayList<BaseNode>())

            val level0Count = lvCounts[index]
            val lv1More = Level1Item("查看更多", "今日2场直播", "", ArrayList<BaseNode>())
            for (j in 0 until level0Count) {
                if (j >= 2) {
                    val lv2 = Level1Item("name $j", "今日2场直播", "", ArrayList<BaseNode>())
                    lv1More.childNode?.add(lv2)

                    if (j == level0Count - 1) {
                        lv0.childNode?.add(lv1More)
                    }
                } else {
                    val lv1 = Level1Item("name $j", "今日2场直播", "", ArrayList<BaseNode>())
                    lv0.childNode?.add(lv1)
                }
            }
            lv0.isExpanded = true
            list.add(lv0)
        }

        return list
    }

}

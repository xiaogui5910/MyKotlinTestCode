package com.example.chenchenggui.mykotlintestcode

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dataList = ArrayList<String>()
        for (c in 'A'..'D') {
            dataList.add(c.toString())
        }
        for ((index, str) in dataList.withIndex()) {
            Log.e("main", "index=$index---value=$str")
        }
        val rvTest = rv_test
        rvTest.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val testAdapter = TestAdapter(R.layout.item, dataList)
        rvTest.adapter = testAdapter

        var footerView = LayoutInflater.from(this).inflate(R.layout.footer, rvTest.parent as
                ViewGroup,
                false)
        //        testAdapter.addFooterView(footerView,0, LinearLayout.HORIZONTAL)

        LinearSnapHelper().attachToRecyclerView(rvTest)
        rvTest.adapter = RvAdapter(this, dataList)
        rvTest.setOnClickListener { }
        hrl_main.setOnRefreshListener {
            Toast.makeText(this@MainActivity, "刷新数据成功", Toast.LENGTH_SHORT).show()
        }
    }

    class RvAdapter(var context: Context, var dataList: List<String>) : RecyclerView.Adapter<RvAdapter
    .RvViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RvViewHolder {
            var view: View = LayoutInflater.from(context).inflate(R.layout.item, p0, false)
            return RvViewHolder(view)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onBindViewHolder(p0: RvViewHolder, p1: Int) {
            p0.name.text = dataList[p1]
        }


        class RvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById(R.id.tv_text)
            var image: ImageView = itemView.findViewById(R.id.iv_img)

        }
    }

    class TestAdapter(layoutResId: Int, data: List<String>) : BaseQuickAdapter<String,
            BaseViewHolder>(layoutResId, data) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
            Log.e("main", "item=$item")
            helper?.setText(R.id.tv_text, "icon-$item")
        }
    }
}

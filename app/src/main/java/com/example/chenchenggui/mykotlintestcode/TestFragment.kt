package com.example.chenchenggui.mykotlintestcode

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.pulltorefresh.PullToRefreshLayout

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/11/7
 */
class TestFragment : androidx.fragment.app.Fragment() {
    lateinit var rvTest: androidx.recyclerview.widget.RecyclerView
    lateinit var hrlMain:PullToRefreshLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        rvTest= view.findViewById(R.id.rv_test)
//        hrlMain=view.findViewById(R.id.hrl_main)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dataList = ArrayList<String>()
        for (c in 'A'..'D') {
            dataList.add(c.toString())
        }
        for ((index, str) in dataList.withIndex()) {
            Log.e("main", "index=$index---value=$str")
        }

        rvTest.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)

        val testAdapter = TestAdapter(R.layout.item, dataList)
        rvTest.adapter = testAdapter
//
//        var footerView = LayoutInflater.from(this).inflate(R.layout.footer, rvTest.parent as
//                ViewGroup,
//                false)
//                testAdapter.addFooterView(footerView,0, LinearLayout.HORIZONTAL)

//        LinearSnapHelper().attachToRecyclerView(rvTest)
//        rvTest.adapter = RvAdapter(this, dataList)
        testAdapter.setOnItemClickListener { adapter, view, position ->
            Toast.makeText(context, "position=$position", Toast.LENGTH_SHORT).show()
        }
//        hrlMain.setOnRefreshListener {
//            Toast.makeText(context, "刷新数据成功", Toast.LENGTH_SHORT).show()
//        }
    }

    class RvAdapter(var context: Context, var dataList: List<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<RvAdapter
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


        class RvViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById(R.id.tv_text)
            var image: ImageView = itemView.findViewById(R.id.iv_img)

        }
    }

    class TestAdapter(layoutResId: Int, data: MutableList<String>) : BaseQuickAdapter<String,
            BaseViewHolder>(layoutResId, data) {
        override fun convert(holder: BaseViewHolder, item: String) {
            Log.e("main", "item=$item")
            holder.setText(R.id.tv_text, "icon-$item")
            holder.setText(R.id.tv_text2, "icon-$item-desc")
        }
    }
}
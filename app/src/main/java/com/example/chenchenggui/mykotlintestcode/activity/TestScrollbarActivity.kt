package com.example.chenchenggui.mykotlintestcode.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import kotlinx.android.synthetic.main.activity_test_scrollbar.*

class TestScrollbarActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test_scrollbar
    }

    override fun initView() {
        super.initView()
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val list = ArrayList<String>()
        for (i in 0..30) {
            list.add(i.toString())
        }
        val adapter = TestScrollbarAdapter(R.layout.item_test_scrollbar, list)
        adapter.bindToRecyclerView(recyclerview)
    }

    class TestScrollbarAdapter(id: Int, data: List<String>) : BaseQuickAdapter<String, BaseViewHolder>
    (id, data) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
        }

    }
}

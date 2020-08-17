package com.example.chenchenggui.mykotlintestcode.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import kotlinx.android.synthetic.main.activity_test_scrollbar.*

class TestScrollbarActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test_scrollbar
    }

    override fun initView() {
        super.initView()
        recyclerview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        val list = ArrayList<String>()
        for (i in 0..30) {
            list.add(i.toString())
        }
        val adapter = TestScrollbarAdapter(R.layout.item_test_scrollbar, list)
        recyclerview.adapter=adapter
    }

    class TestScrollbarAdapter(id: Int, data: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>
    (id, data) {
        override fun convert(holder: BaseViewHolder, item: String) {
        }

    }
}

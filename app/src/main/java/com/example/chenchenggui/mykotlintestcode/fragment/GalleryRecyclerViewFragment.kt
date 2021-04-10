package com.example.chenchenggui.mykotlintestcode.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import kotlinx.android.synthetic.main.fragment_gallery_recyclerview.*

class GalleryRecyclerViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val list = ArrayList<String>()
        for (i in 0..10) {
            list.add("我就是名字$i")
        }

        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = GalleryRecyclerViewAdapter(R.layout.layout_item_gallery_recyclerview, list)
        recyclerview.adapter = adapter

    }

    class GalleryRecyclerViewAdapter(layoutId: Int, dataList: ArrayList<String>)
        : BaseQuickAdapter<String, BaseViewHolder>(layoutId, dataList) {
        override fun convert(holder: BaseViewHolder, item: String) {

            holder.setText(R.id.tv_item_gallery_name,item)
                    .setText(R.id.tv_item_gallery_desc,"我就是描述${holder.adapterPosition}")
            val imageView = holder.getView<ImageView>(R.id.iv_item_gallery_pager)
            if (holder.adapterPosition % 2 == 0) {
                imageView.setImageResource(R.drawable.img_viewstub_test1)
            } else {
                imageView.setImageResource(R.drawable.img_viewstub_test2)
            }
        }

    }

}
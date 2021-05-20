package com.example.chenchenggui.mykotlintestcode.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.dp2px
import kotlinx.android.synthetic.main.fragment_gallery_viewpager.*

class GalleryViewPagerFragment : Fragment() {
    private var dataList = ArrayList<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery_viewpager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        for (i in 0..10) {
            dataList.add("我就是名字$i")
        }

        val pagerAdapter = GalleryPagerAdapter()
        viewpager.pageMargin = dp2px(context!!, 21f).toInt()
        viewpager.adapter = pagerAdapter
    }

    inner class GalleryPagerAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return dataList.size
        }

        override fun isViewFromObject(view: View, o: Any): Boolean {
            return view == o
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val rootView = LayoutInflater.from(context).inflate(R.layout.layout_item_gallery_viewpager, null)
            val imageView = rootView.findViewById<ImageView>(R.id.iv_item_gallery_pager)
            val tvName = rootView.findViewById<TextView>(R.id.tv_item_gallery_name)
            val tvDesc = rootView.findViewById<TextView>(R.id.tv_item_gallery_desc)
            if (position % 2 == 0) {
                imageView.setImageResource(R.drawable.img_viewstub_test1)
            } else {
                imageView.setImageResource(R.drawable.img_viewstub_test2)
            }
            tvName.text = dataList[position]
            tvDesc.text = "我就是描述$position"

            imageView.setOnClickListener {
                viewpager.currentItem = position
            }

            container.addView(rootView)
            return rootView
        }

        override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
            container.removeView(o as View?)
        }

    }
}
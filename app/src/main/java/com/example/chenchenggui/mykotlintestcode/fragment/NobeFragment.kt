package com.example.chenchenggui.mykotlintestcode.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R


class NobeFragment : Fragment() {
    companion object {
        fun newInstance(type: String): NobeFragment {
            val dialogFragment = NobeFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }

    lateinit var recyclerview: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_nobe, null)
        recyclerview = rootView.findViewById<RecyclerView>(R.id.recyclerview)
//        val tvTitle=rootView.findViewById<TextView>(R.id.tv_title)
        arguments?.let {
            val string = it.getString("type")
//            tvTitle.text = string
        }
        initView(rootView)
        return rootView
    }

    private fun initView(rootView: View) {
        val nobeAdapter = NobeAdapter()

        //头布局
        val headerView = LayoutInflater.from(context).inflate(R.layout.header_nobe, null)
        val tvHeader = headerView.findViewById<TextView>(R.id.tv_header)
        val content1 = "1000"
        val content2 = "大礼包"
        val showText = getString(R.string.show_text, content1, content2)
        val startIndex1 = showText.indexOf(content1)
        val startIndex2 = showText.indexOf(content2)

        val textSpanned1 = SpannableString(showText)
        textSpanned1.setSpan(ForegroundColorSpan(Color.RED),
                startIndex1, startIndex1 + content1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textSpanned1.setSpan(ForegroundColorSpan(Color.YELLOW),
                startIndex2, startIndex2 + content2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvHeader.text = textSpanned1
        nobeAdapter.addHeaderView(headerView)

        recyclerview.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        recyclerview.adapter = nobeAdapter

        val dataList = arrayListOf<String>()
        for (i in 0..50) {
            dataList.add(i.toString())
        }
        nobeAdapter.setNewInstance(dataList)
    }

    class NobeAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_nobe) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_item, "item$item")
        }
    }

}
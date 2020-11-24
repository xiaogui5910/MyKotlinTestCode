package com.example.chenchenggui.mykotlintestcode.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R


class NobleFragment : Fragment() {
    companion object {
        fun newInstance(type: String): NobleFragment {
            val dialogFragment = NobleFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }

    lateinit var recyclerview: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_noble, null)
        recyclerview = rootView.findViewById<RecyclerView>(R.id.recyclerview)
        arguments?.let {
            val string = it.getString("type")
        }
        initView(rootView)
        return rootView
    }

    private fun initView(rootView: View) {
        val nobleAdapter = NobleAdapter()

        //头布局
        val headerView = LayoutInflater.from(context).inflate(R.layout.header_noble, null)
        val tvHeader = headerView.findViewById<TextView>(R.id.tv_noble_header)
        val tvHeaderDesc = headerView.findViewById<TextView>(R.id.tv_noble_header_desc)
        val content1 = "1000"
        val content2 = "大礼包"
        val showText = getString(R.string.show_text, content1, content2)
        val startIndex1 = showText.indexOf(content1)
        val startIndex2 = showText.indexOf(content2)

        val lightColor = ContextCompat.getColor(context!!, R.color.noble_yellow_caa869)
        val textSpanned1 = SpannableString(showText)
        textSpanned1.setSpan(ForegroundColorSpan(lightColor),
                startIndex1, startIndex1 + content1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textSpanned1.setSpan(ForegroundColorSpan(lightColor),
                startIndex2, startIndex2 + content2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvHeader.text = textSpanned1

        val content3 = "100/200"
        val showText2 = getString(R.string.show_privilege_text, content3)
        val startIndex3 = showText2.indexOf(content3)
        val textSpanned3 = SpannableString(showText2)
        textSpanned3.setSpan(ForegroundColorSpan(lightColor),
                startIndex3, startIndex3 + content3.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvHeaderDesc.text = textSpanned3

        nobleAdapter.addHeaderView(headerView)

        recyclerview.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        recyclerview.adapter = nobleAdapter

        val dataList = arrayListOf<String>()
        for (i in 0..12) {
            dataList.add(i.toString())
        }
        nobleAdapter.setNewInstance(dataList)
    }

    class NobleAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_noble) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_item, "item$item")
        }
    }

}
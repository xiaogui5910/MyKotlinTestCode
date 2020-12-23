package com.example.chenchenggui.mykotlintestcode.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.dp2px
import kotlinx.android.synthetic.main.dialog_guess_list.*

/**
 * description ：
 * author : chenchenggui
 * creation date: 2020/12/22
 */
class GuessListDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.dialog_guess_list, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        isCancelable = true
        val attributes = window?.attributes?.apply {
            gravity = Gravity.BOTTOM
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = dp2px(context!!, 473f).toInt()
            dimAmount = 0f
            flags = flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        }
        window?.attributes = attributes
    }


    private fun initView() {
        iv_close.setOnClickListener { dismiss() }
    }

    private fun initData() {
        rv_king_guess_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val list = ArrayList<String>()
        for (i in 0..20) {
            list.add("item$i")
        }
        val adapter = KingGuessAdapter(R.layout.item_king_guess_list, list)
        val matchHeaderView = LayoutInflater.from(context).inflate(R.layout.header_king_guess_match, null)
        val userHeaderView = LayoutInflater.from(context).inflate(R.layout.header_king_guess_user, null)
        adapter.addHeaderView(matchHeaderView)
        adapter.addHeaderView(userHeaderView)
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        val drawable = ContextCompat.getDrawable(context!!, R.drawable
                .king_guess_shape_item_divider)
        dividerItemDecoration.setDrawable(drawable!!)
        rv_king_guess_list.addItemDecoration(dividerItemDecoration)
        rv_king_guess_list.adapter = adapter

    }

    class KingGuessAdapter(layoutId: Int, dataList: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutId, dataList) {
        override fun convert(holder: BaseViewHolder, item: String) {
        }

    }

}
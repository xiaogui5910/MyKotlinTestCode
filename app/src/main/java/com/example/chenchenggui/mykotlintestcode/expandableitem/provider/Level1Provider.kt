package com.example.chenchenggui.mykotlintestcode.expandableitem.provider

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.expandableitem.ExpandableItemAdapter
import com.example.chenchenggui.mykotlintestcode.expandableitem.Level0Item
import com.example.chenchenggui.mykotlintestcode.expandableitem.Level1Item
import com.example.chenchenggui.mykotlintestcode.log
import com.example.chenchenggui.mykotlintestcode.toast

/**
 * description ：
 * author : chenchenggui
 * creation date: 2020/8/17
 */
class Level1Provider: BaseNodeProvider() {
    override val itemViewType: Int
        get() = ExpandableItemAdapter.TYPE_LEVEL_1
    override val layoutId: Int
        get() = R.layout.item_expandable_lv1

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val lv1 = item as Level1Item
        val llContent = helper.getView<LinearLayout>(R.id.ll_item_content)
        val llMore = helper.getView<LinearLayout>(R.id.ll_more)

        val adapter = getAdapter()

        if (lv1.childNode!=null&&lv1.childNode!!.size>0) {
            llContent.visibility = View.GONE
            llMore.visibility = View.VISIBLE
            helper.itemView.setBackgroundResource(R.drawable.shape_lv1)
        } else {
            llMore.visibility = View.GONE
            llContent.visibility = View.VISIBLE
            helper.itemView.setBackgroundColor(Color.WHITE)

            helper.setText(R.id.tv_lv1_name, lv1.title)
                    .setText(R.id.tv_lv1_sub, lv1.subTitle)

            val pos = helper.adapterPosition
            val next = pos + 1
            if (next < adapter!!.data.size && adapter.data[next] is Level0Item) {
                helper.itemView.setBackgroundResource(R.drawable.shape_lv1)
            }
        }

        helper.itemView.setOnClickListener {
            val pos = helper.adapterPosition
            if (!lv1.isExpanded && lv1.childNode!=null&&lv1.childNode!!.size>0) {
                adapter!!.expand(pos, false)

                val cp = adapter.findParentNode(lv1)
                (adapter.data[cp] as Level0Item).childNode!!.remove(lv1)
                adapter.data.removeAt(helper.layoutPosition)
                adapter.notifyItemRemoved(helper.layoutPosition)

            }
            log("onclick---$pos")
            toast("onclick---$pos--name=${lv1.title}")
        }
    }
}
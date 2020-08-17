package com.example.chenchenggui.mykotlintestcode.expandableitem.provider

import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.expandableitem.ExpandableItemAdapter
import com.example.chenchenggui.mykotlintestcode.expandableitem.Level0Item

/**
 * description ：
 * author : chenchenggui
 * creation date: 2020/8/17
 */
class Level0Provider: BaseNodeProvider() {
    override val itemViewType: Int
        get() = ExpandableItemAdapter.TYPE_LEVEL_0
    override val layoutId: Int
        get() = R.layout.item_expandable_lv0

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val lv0 = item as Level0Item
        helper.setText(R.id.tv_lv0_name, lv0.title)

//                holder.itemView.setOnClickListener {
//                    val pos = holder.adapterPosition
//                    if (lv0.isExpanded) {
//                        collapse(pos)
//                    } else {
//                        expand(pos)
//                    }
//                }
    }
}
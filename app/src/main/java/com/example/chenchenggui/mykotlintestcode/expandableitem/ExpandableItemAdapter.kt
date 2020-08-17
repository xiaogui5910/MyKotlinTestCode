package com.example.chenchenggui.mykotlintestcode.expandableitem

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.expandableitem.provider.Level0Provider
import com.example.chenchenggui.mykotlintestcode.expandableitem.provider.Level1Provider
import com.example.chenchenggui.mykotlintestcode.log
import com.example.chenchenggui.mykotlintestcode.toast

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/27
 */
class ExpandableItemAdapter: BaseNodeAdapter() {

    companion object {
        val TYPE_LEVEL_0 = 0
        val TYPE_LEVEL_1 = 1
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        when(data[position]){
            is Level0Item->return TYPE_LEVEL_0
            is Level1Item->return TYPE_LEVEL_1
        }
        return -1
    }
    init {
        addNodeProvider(Level0Provider())
        addNodeProvider(Level1Provider())
    }

//    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {
//        when (holder.itemViewType) {
//            TYPE_LEVEL_0 -> {
//                val lv0 = item as Level0Item
//                holder.setText(R.id.tv_lv0_name, lv0.title)
//
////                holder.itemView.setOnClickListener {
////                    val pos = holder.adapterPosition
////                    if (lv0.isExpanded) {
////                        collapse(pos)
////                    } else {
////                        expand(pos)
////                    }
////                }
//            }
//            TYPE_LEVEL_1 -> {
//                val lv1 = item as Level1Item
//                val llContent = holder.getView<LinearLayout>(R.id.ll_item_content)
//                val llMore = holder.getView<LinearLayout>(R.id.ll_more)
//                if (lv1.hasSubItem()) {
//                    llContent.visibility = View.GONE
//                    llMore.visibility = View.VISIBLE
//                    holder.itemView.setBackgroundResource(R.drawable.shape_lv1)
//                } else {
//                    llMore.visibility = View.GONE
//                    llContent.visibility = View.VISIBLE
//                    holder.itemView.setBackgroundColor(Color.WHITE)
//
//                    holder.setText(R.id.tv_lv1_name, lv1.title)
//                            .setText(R.id.tv_lv1_sub, lv1.subTitle)
//
//                    val pos = holder.adapterPosition
//                    val next = pos + 1
//                    if (next < data.size && data[next] is Level0Item) {
//                        holder.itemView.setBackgroundResource(R.drawable.shape_lv1)
//                    }
//                }
//
//                holder.itemView.setOnClickListener {
//                    val pos = holder.adapterPosition
//                    if (!lv1.isExpanded && lv1.childNode!=null&&lv1.childNode!!.size>0) {
//                        expand(pos, false)
//
//                        val cp = getParentPosition(lv1)
//                        (data[cp] as Level0Item).removeSubItem(lv1)
//                        data.removeAt(holder.layoutPosition)
//                        notifyItemRemoved(holder.layoutPosition)
//
//                    }
//                    log("onclick---$pos")
//                    toast("onclick---$pos--name=${lv1.title}")
//                }
//
//            }
//        }
//    }

}
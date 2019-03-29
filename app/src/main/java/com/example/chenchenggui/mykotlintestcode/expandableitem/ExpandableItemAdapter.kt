package com.example.chenchenggui.mykotlintestcode.expandableitem

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.log
import com.example.chenchenggui.mykotlintestcode.toast

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/27
 */
class ExpandableItemAdapter(dataList: List<MultiItemEntity>)
    : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(dataList) {

    init {
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0)
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        when (helper?.itemViewType) {
            TYPE_LEVEL_0 -> {
                val lv0 = item as Level0Item
                helper.setText(R.id.tv_lv0_name, lv0.title)

//                helper.itemView.setOnClickListener {
//                    val pos = helper.adapterPosition
//                    if (lv0.isExpanded) {
//                        collapse(pos)
//                    } else {
//                        expand(pos)
//                    }
//                }
            }
            TYPE_LEVEL_1 -> {
                val lv1 = item as Level1Item
                val llContent = helper.getView<LinearLayout>(R.id.ll_item_content)
                val llMore = helper.getView<LinearLayout>(R.id.ll_more)
                if (lv1.hasSubItem()) {
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
                    if (next < data.size && data[next] is Level0Item) {
                        helper.itemView.setBackgroundResource(R.drawable.shape_lv1)
                    }
                }

                helper.itemView.setOnClickListener {
                    val pos = helper.adapterPosition
                    if (!lv1.isExpanded && lv1.hasSubItem()) {
                        expand(pos, false)

                        val cp = getParentPosition(lv1)
                        (data[cp] as Level0Item).removeSubItem(lv1)
                        data.removeAt(helper.layoutPosition)
                        notifyItemRemoved(helper.layoutPosition)

                    }
                    log("onclick---$pos")
                    toast("onclick---$pos--name=${lv1.title}")
                }

            }
        }
    }

    companion object {
        val TYPE_LEVEL_0 = 0
        val TYPE_LEVEL_1 = 1
    }
}
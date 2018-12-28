package com.example.chenchenggui.mykotlintestcode.expandableitem

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/27
 */
data class Level1Item(var title:String,var subTitle:String,var iconUrl:String)
    :AbstractExpandableItem<Level1Item>(), MultiItemEntity{
    override fun getLevel(): Int {
        return 1
    }

    override fun getItemType(): Int {
        return ExpandableItemAdapter.TYPE_LEVEL_1
    }
}
package com.example.chenchenggui.mykotlintestcode.expandableitem

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/27
 */
data class Level1Item(var title: String, var subTitle: String, var iconUrl: String,
                      var childNodeList: MutableList<BaseNode>?) : BaseExpandNode() {
    init {
        isExpanded = false
    }

    override val childNode: MutableList<BaseNode>?
        get() = childNodeList
}
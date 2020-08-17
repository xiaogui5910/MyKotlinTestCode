package com.example.chenchenggui.mykotlintestcode.sectionitem

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/28
 */
class MySection : SectionEntity {
    var isMore: Boolean = false
    var subList: List<MySection> = ArrayList()
    var isMyHeader: Boolean = false
    var header: String = ""
    var userInfo: UserInfo = UserInfo("", "", "")

    constructor(userInfo: UserInfo) {
        this.userInfo = userInfo
    }

    constructor(isMyHeader: Boolean, header: String, isMore: Boolean) {
        this.isMore = isMore
        this.isMyHeader = isMyHeader
        this.header = header
    }

    override val isHeader: Boolean
        get() = isMyHeader

    override fun toString(): String {
        return "MySection(isMore=$isMore, subList=$subList, isMyHeader=$isMyHeader, header='$header', userInfo=$userInfo)"
    }

}
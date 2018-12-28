package com.example.chenchenggui.mykotlintestcode.sectionitem

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/28
 */
class MySection : SectionEntity<UserInfo> {
    var isMore: Boolean = false
    var subList: List<MySection> = ArrayList<MySection>()

    constructor(userInfo: UserInfo) : super(userInfo)
    constructor(isHeader: Boolean, header: String, isMore: Boolean) : super(isHeader, header) {
        this.isMore = isMore
    }
}
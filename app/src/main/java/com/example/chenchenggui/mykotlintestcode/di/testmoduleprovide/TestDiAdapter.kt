package com.example.chenchenggui.mykotlintestcode.di.testmoduleprovide

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R

/**
 * description ：
 * author : chenchenggui
 * creation date: 2020-01-10
 */
class TestDiAdapter(layoutResId: Int, data: MutableList<TestDiModuleBean>?) : BaseQuickAdapter<TestDiModuleBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(holder: BaseViewHolder, item: TestDiModuleBean) {
        holder.setText(R.id.tv_test_di_class_name, item.className)
        holder.setText(R.id.tv_test_di_class_student, "学生：${item.studentNum}")
        holder.setText(R.id.tv_test_di_class_teacher, "班主任：${item.teacher}")
    }
}
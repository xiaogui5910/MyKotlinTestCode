package com.example.chenchenggui.mykotlintestcode.sectionitem

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/28
 */
class SectionItemAdapter:BaseSectionQuickAdapter<MySection, BaseViewHolder> {
    constructor(layoutResId:Int,headerResId:Int,data:MutableList<MySection>):super(
            headerResId,data){
        setNormalLayout(layoutResId)
    }

    override fun convertHeader(helper: BaseViewHolder, item: MySection) {
        helper.setText(R.id.tv_header,item.header)
        val header = helper.getView<TextView>(R.id.tv_header)
        val llMore = helper.getView<LinearLayout>(R.id.ll_more)
        if (item.isMore){
            header.visibility=View.GONE
            llMore.visibility=View.VISIBLE

            val params = helper.itemView.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            params.topMargin=0
            helper.itemView.layoutParams=params
            helper.itemView.setBackgroundResource(R.drawable.shape_lv1)
        }else{
            llMore.visibility=View.GONE
            header.visibility=View.VISIBLE

            val params = helper.itemView.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            params.topMargin= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,6f,context
                    .resources.displayMetrics).toInt()
            helper.itemView.layoutParams=params
            helper.itemView.setBackgroundResource(R.drawable.shape_lv0)
        }
        addChildClickViewIds(R.id.ll_more)
    }

    override fun convert(holder: BaseViewHolder, item: MySection) {
        val userInfo=item.userInfo
        holder.setText(R.id.tv_section_name,userInfo.title)
                .setText(R.id.tv_section_sub,userInfo.subTitle)

        holder.itemView.setBackgroundColor(Color.WHITE)

        val pos = holder.adapterPosition
        val next = pos + 1
        if (next < data.size && data[next].isHeader) {
            holder.itemView.setBackgroundResource(R.drawable.shape_lv1)
        }
    }
}
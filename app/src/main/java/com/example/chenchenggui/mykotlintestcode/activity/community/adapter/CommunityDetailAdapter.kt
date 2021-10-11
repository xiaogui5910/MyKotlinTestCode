package com.example.chenchenggui.mykotlintestcode.activity.community.adapter

import android.graphics.drawable.Animatable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.activity.community.bean.CommunityDetailBean
import com.example.chenchenggui.mykotlintestcode.dp2px
import com.example.chenchenggui.mykotlintestcode.getScreenWidth
import com.example.chenchenggui.mykotlintestcode.isWifiConnected
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo

/**
 * @description ：社区详情页Adapter
 * @author : chenchenggui
 * @date: 7/15/21
 */

class CommunityDetailAdapter(dataList: ArrayList<CommunityDetailBean>?)
    : BaseMultiItemQuickAdapter<CommunityDetailBean, BaseViewHolder>(dataList) {
    init {
        addItemType(CommunityDetailBean.ITEM_TYPE_TITLE, R.layout.item_community_detail_title)
        addItemType(CommunityDetailBean.ITEM_TYPE_OWNER, R.layout.item_community_detail_owner)
        addItemType(CommunityDetailBean.ITEM_TYPE_TEXT, R.layout.item_community_detail_text)
        addItemType(CommunityDetailBean.ITEM_TYPE_IMG, R.layout.item_community_detail_img)
        addItemType(CommunityDetailBean.ITEM_TYPE_MANAGER_OPERATION, R.layout.item_community_detail_manager_operation)
        addItemType(CommunityDetailBean.ITEM_TYPE_REPLY_HEADER, R.layout.item_community_detail_reply_header)
        addItemType(CommunityDetailBean.ITEM_TYPE_REPLY_CONTENT, R.layout.item_community_detail_reply_content)
        addItemType(CommunityDetailBean.ITEM_TYPE_REPLY_EMPTY, R.layout.item_community_detail_reply_empty)
    }

    override fun convert(holder: BaseViewHolder, item: CommunityDetailBean) {
        val position = holder.adapterPosition
        when (item.itemType) {
            CommunityDetailBean.ITEM_TYPE_REPLY_CONTENT -> {
                //更多回复
                var isGone = false
                if (item.replyCount > 0) {
                    isGone = true
                }
                holder.setGone(R.id.view_item_community_detail_reply_bg, isGone)
                holder.setGone(R.id.tv_item_community_detail_replies, isGone)

                //楼主标志
                holder.setVisible(R.id.iv_item_community_detail_owner_tag, position == 0)
            }
            CommunityDetailBean.ITEM_TYPE_IMG -> {
                val img = holder.getView<SimpleDraweeView>(R.id.iv_img)
                val ivGiftTag = holder.getView<ImageView>(R.id.iv_gift_tag)
                val screenWidth = getScreenWidth(context)
                val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
                val imgW = screenWidth - dp2px(context, 30f).toInt()
                layoutParams.width = imgW
                layoutParams.height = dp2px(context, 230f).toInt()
                holder.itemView.layoutParams = layoutParams

                val controllerBuilder = Fresco.newDraweeControllerBuilder()
                val controllerListener = object : BaseControllerListener<ImageInfo>() {
                    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                        super.onFinalImageSet(id, imageInfo, animatable)
//                        if (imageInfo == null) {
//                            return
//                        }
//                        layoutParams.height = (imgW * (imageInfo.height.toFloat() / imageInfo.width.toFloat())).toInt()
//                        holder.itemView.layoutParams = layoutParams
                        ivGiftTag.visibility = if (context.isWifiConnected()) View.GONE else View.VISIBLE
                    }

                    override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
                        super.onIntermediateImageSet(id, imageInfo)
                    }

                    override fun onFailure(id: String?, throwable: Throwable?) {
                        super.onFailure(id, throwable)
//                        layoutParams.height = dp2px(context, 230f).toInt()
//                        holder.itemView.layoutParams = layoutParams
                    }
                }
                if (item.imgType == CommunityDetailBean.IMG_TYPE_PIC) {
//                        img.setImageURI(item.img)
                } else {
                    //自动播放动画
                    controllerBuilder.setAutoPlayAnimations(context.isWifiConnected())
                }
                val draweeController: DraweeController = controllerBuilder.setControllerListener(controllerListener).setUri(Uri.parse(item.img)).build()
                img.setController(draweeController)
            }
            CommunityDetailBean.ITEM_TYPE_TEXT -> {
                holder.setText(R.id.tv_item_community_detail_text, item.text)
            }
        }
    }

}
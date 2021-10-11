package com.example.chenchenggui.mykotlintestcode.activity.community

import android.graphics.drawable.Animatable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.activity.BaseActivity
import com.example.chenchenggui.mykotlintestcode.activity.community.adapter.CommunityDetailAdapter
import com.example.chenchenggui.mykotlintestcode.activity.community.bean.CommunityDetailBean
import com.example.chenchenggui.mykotlintestcode.dp2px
import com.example.chenchenggui.mykotlintestcode.getScreenWidth
import com.example.chenchenggui.mykotlintestcode.isWifiConnected
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import kotlinx.android.synthetic.main.activity_community_detail.*
import kotlinx.android.synthetic.main.activity_community_detail.iv_title_back
import kotlinx.android.synthetic.main.activity_community_detail.iv_title_more
import kotlinx.android.synthetic.main.activity_community_detail.rv_replies
import kotlinx.android.synthetic.main.activity_community_detail.tv_title_reply
import kotlinx.android.synthetic.main.activity_community_detail_header.*


class CommunityDetailActivity : BaseActivity() {

    private var adapter: CommunityDetailAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_community_detail
    }

    override fun initView() {
        super.initView()
        iv_title_back.setOnClickListener {
            finish()
        }
        tv_title_reply.setOnClickListener {
            if (adapter == null) {
                return@setOnClickListener
            }
            for (i in 0 until adapter!!.data.size) {
                val communityDetailBean = adapter!!.data[i]
                if (communityDetailBean.itemType == CommunityDetailBean.ITEM_TYPE_REPLY_HEADER) {
                    val layoutManager = rv_replies.layoutManager as LinearLayoutManager
                    layoutManager.scrollToPositionWithOffset(i,0)
                    break
                }
            }
        }
    }

    override fun initData() {
        super.initData()

        rv_replies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = CommunityDetailAdapter(null)
        rv_replies.adapter = adapter

        //初始化回复
        initReplies()

        //TODO 处理视频还是图文
        val moreDrawable = VectorDrawableCompat.create(resources, R.drawable.icon_more_land, theme)
//        moreDrawable?.setTint(Color.WHITE)
        iv_title_more.setImageDrawable(moreDrawable)
    }

    private fun initReplies() {
        val dataList = CommunityDetailUtils.initRepliesTest()
        adapter?.setNewInstance(dataList)
    }
}
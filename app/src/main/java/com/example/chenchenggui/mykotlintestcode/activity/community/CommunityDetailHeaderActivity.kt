package com.example.chenchenggui.mykotlintestcode.activity.community

import android.graphics.drawable.Animatable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.*
import com.example.chenchenggui.mykotlintestcode.activity.BaseActivity
import com.example.chenchenggui.mykotlintestcode.activity.community.bean.CommunityDetailBean
import com.example.chenchenggui.mykotlintestcode.activity.community.bean.CommunityDetailHeaderBean
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import kotlinx.android.synthetic.main.activity_community_detail.*
import kotlinx.android.synthetic.main.activity_community_detail_header.*
import kotlinx.android.synthetic.main.activity_community_detail_header.iv_title_back
import kotlinx.android.synthetic.main.activity_community_detail_header.rv_replies
import kotlinx.android.synthetic.main.activity_community_detail_header.tv_title_reply


class CommunityDetailHeaderActivity : BaseActivity() {

    private var adapter: CommunityDetailAdapter? = null
    private var headerView: View? = null
    private var llImgContainer: LinearLayout? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_community_detail_header
    }

    override fun initView() {
        super.initView()
        iv_title_back.setOnClickListener {
            finish()
        }
        tv_title_reply.setOnClickListener {
//            rv_replies.scrollToPositionWithOffset(1)
            val layoutManager = rv_replies.layoutManager as LinearLayoutManager
            layoutManager.scrollToPositionWithOffset(1,0)
            rv_replies.scrollBy(0, dp2px(this,30f).toInt())
        }
    }

    override fun initData() {
        super.initData()

        rv_replies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = CommunityDetailAdapter(R.layout.item_community_detail, null)
        headerView = LayoutInflater.from(this).inflate(R.layout.header_community_detail, null)
        adapter?.addHeaderView(headerView!!)
//        val headerView1 = LayoutInflater.from(this).inflate(R.layout.item_community_detail_reply_header, null)
//        adapter?.addHeaderView(headerView1!!)
//        val layoutParams = headerView1.layoutParams as ViewGroup.MarginLayoutParams
//        layoutParams.topMargin = dp2px(this,30f).toInt()
//        layoutParams.leftMargin = dp2px(this,15f).toInt()
//        layoutParams.rightMargin = dp2px(this,15f).toInt()


        rv_replies.adapter = adapter

        //item间距
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val drawable = ContextCompat.getDrawable(this, R.drawable.item_voice_chat_divider)
        dividerItemDecoration.setDrawable(drawable!!)
        rv_replies.addItemDecoration(dividerItemDecoration)

        //头部初始化
        initHeaderView()

        //初始化回复
        initReplies()

        rv_replies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    //停止滚动
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (llImgContainer != null) {
                            for (i in 0 until llImgContainer!!.childCount) {
                                val childAt = llImgContainer!!.getChildAt(i)
                                val visibleLocal = childAt.isVisibleLocal()
                                log("community-----scroll-----visibleLocal=${visibleLocal}")
                            }
                        }

                    }
                    //手指滚动
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                    }
                    //自动滚动
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun initReplies() {
        val dataList = ArrayList<CommunityDetailHeaderBean>()
        for (i in 0..9) {
            val bean = CommunityDetailHeaderBean()
            dataList.add(bean)
        }
        adapter?.setNewInstance(dataList)
    }

    private fun initHeaderView() {
        if (headerView == null) {
            return
        }
        val item = CommunityDetailUtils.generateTestData()
        val tvHeaderTitle = headerView!!.findViewById<TextView>(R.id.tv_community_detail_header_title)
        val tvHeaderNickname = headerView!!.findViewById<TextView>(R.id.tv_community_detail_header_nickname)

        val llTextContainer = headerView!!.findViewById<LinearLayout>(R.id.ll_community_detail_header_text_container)
        llImgContainer = headerView!!.findViewById<LinearLayout>(R.id.ll_community_detail_header_img_container)

        //文字内容
        val textList = item.textList
        for (i in 0 until textList.size) {
            val textBean = textList[i]
            val textChildView = LayoutInflater.from(this).inflate(R.layout.layout_community_detail_text, null) as TextView
            textChildView.text = textBean
            llTextContainer.addView(textChildView)
            val layoutParams = textChildView.layoutParams as LinearLayout.LayoutParams
            layoutParams.topMargin = dp2px(this, 18f).toInt()
        }

        //图片内容
        val imgList = item.imgList
        for (i in 0 until imgList.size) {
            val imgBean = imgList[i]
            val childView = LayoutInflater.from(this).inflate(R.layout.layout_community_detail_img, null)
            val ivImg = childView.findViewById<SimpleDraweeView>(R.id.iv_img)
            val ivGiftTag = childView.findViewById<ImageView>(R.id.iv_gift_tag)

            llImgContainer?.addView(childView)
            val layoutParams = ivImg.layoutParams as ViewGroup.MarginLayoutParams
//            layoutParams.topMargin = dp2px(this, 18f).toInt()
//            layoutParams.height = dp2px(this, 230f).toInt()
//            imgChildView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    imgChildView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    val visibleLocal = imgChildView.isVisibleLocal()
//                    log("community-----visibleLocal=${visibleLocal}")
//                }
//
//            })

            val screenWidth = getScreenWidth(this)
            layoutParams.topMargin = dp2px(this, 18f).toInt()
            val imgW = screenWidth - dp2px(this, 30f).toInt()
            layoutParams.width = imgW

            val controllerBuilder = Fresco.newDraweeControllerBuilder()
            val controllerListener = object : BaseControllerListener<ImageInfo>() {
                override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                    super.onFinalImageSet(id, imageInfo, animatable)
                    if (imageInfo == null) {
                        return
                    }
                    layoutParams.height = (imgW * (imageInfo.height.toFloat() / imageInfo.width.toFloat())).toInt()
                    ivImg.layoutParams = layoutParams
                    ivGiftTag.visibility = if (this@CommunityDetailHeaderActivity.isWifiConnected()) View.GONE else View.VISIBLE
                }

                override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
                    super.onIntermediateImageSet(id, imageInfo)
                }

                override fun onFailure(id: String?, throwable: Throwable?) {
                    super.onFailure(id, throwable)
                    layoutParams.height = dp2px(this@CommunityDetailHeaderActivity, 230f).toInt()
                    ivImg.layoutParams = layoutParams
                }
            }
            if (imgBean.imgType == CommunityDetailBean.IMG_TYPE_PIC) {
//                        img.setImageURI(item.img)
            } else {
                //自动播放动画
                controllerBuilder.setAutoPlayAnimations(this.isWifiConnected())
            }
            val draweeController: DraweeController = controllerBuilder.setControllerListener(controllerListener).setUri(Uri.parse(imgBean.imgurl)).build()
            ivImg.setController(draweeController)
        }
    }

    class CommunityDetailAdapter(layoutId: Int, dataList: ArrayList<CommunityDetailHeaderBean>?)
        : BaseQuickAdapter<CommunityDetailHeaderBean, BaseViewHolder>(layoutId, dataList) {
        override fun convert(holder: BaseViewHolder, item: CommunityDetailHeaderBean) {
            val position = holder.adapterPosition

            //更多回复
            var isGone = false
            if (position > 5) {
                isGone = true
            }
            holder.setGone(R.id.view_item_community_detail_reply_bg, isGone)
            holder.setGone(R.id.tv_item_community_detail_replies, isGone)

            //楼主标志
            holder.setVisible(R.id.iv_item_community_detail_owner_tag, position == 0)

        }

    }

}
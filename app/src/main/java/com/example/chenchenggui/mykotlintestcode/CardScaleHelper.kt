package com.example.chenchenggui.mykotlintestcode

import android.content.Context
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * description ：recyclerview
 * author : chenchenggui
 * creation date: 2018/10/19
 */
class CardScaleHelper {
    private lateinit var recyclerView: RecyclerView
    private lateinit var context: Context

    var scale = 1.0f //两边视图scale,为1.0f表示不缩放
    private var pagePadding = 15 //卡片的padding，卡片间的距离等于2倍的pagePadding
    private var showLeftCardWidth = 15 //左边卡片显示大小

    private var cardWidth = 0 //卡片宽度
    private var onePageWidth = 0 //滑动一页的距离
    private var cardGalleryWidth = 0

    var currentItemPos = 0
    private var currentItemOffset = 0

    private var linearSnapHelper = CardLinearSnapHelper()

    fun attachToRecyclerView(rv: RecyclerView) {
        recyclerView = rv
        context = rv.context
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    linearSnapHelper.noNeedToScroll = currentItemOffset == 0 ||
                            currentItemOffset == getDestItemOffset(rv.adapter?.itemCount!! - 1)
                } else {
                    linearSnapHelper.noNeedToScroll = false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //dx>0表示右滑，dx<0表示左滑，dy<0表示上滑，dy>0表示下滑
                if (dx != 0) {
                    currentItemOffset += dx
                    computeCurrentItemPos()
                    onScrolledChangedCallback()
                }
            }
        })

        initWidth()
        linearSnapHelper.attachToRecyclerView(rv)
    }

    private fun initWidth() {
        with(recyclerView) {
            post {
                cardGalleryWidth = width
                val dp = 2 * (pagePadding + showLeftCardWidth)
                cardWidth = (cardGalleryWidth - context.resources.displayMetrics.density * dp + 0.5f).toInt()
                onePageWidth = cardWidth
                smoothScrollToPosition(currentItemPos)
                onScrolledChangedCallback()
            }
        }
    }

    /**
     * RecyclerView位移事件监听，view大小随位移变化
     */
    private fun onScrolledChangedCallback() {
        val offset = currentItemOffset - currentItemPos * onePageWidth
        val percent = Math.max(Math.abs(offset) * 1.0f / onePageWidth, 0.0001f)
        var leftView: View? = null
        var currentView: View? = null
        var rightView: View? = null
        if (currentItemPos > 0) {
            leftView = recyclerView.layoutManager?.findViewByPosition(currentItemPos - 1)
        }
        currentView = recyclerView.layoutManager?.findViewByPosition(currentItemPos)
        if (currentItemPos < recyclerView.adapter?.itemCount!! - 1) {
            rightView = recyclerView.layoutManager?.findViewByPosition(currentItemPos + 1)
        }
        leftView?.scaleY = (1 - scale) * percent + scale
        currentView?.scaleY = (scale - 1) * percent + 1
        rightView?.scaleY = (1 - scale) * percent + scale

    }

    /**
     * 计算currentItemOffset
     */
    private fun computeCurrentItemPos() {
        if (onePageWidth <= 0) return
        var pageChanged = false
        //滑动超过一页说明已翻页
        if (Math.abs(currentItemOffset - currentItemPos * onePageWidth) >= onePageWidth) {
            pageChanged = true
        }
        if (pageChanged) {
            currentItemPos = currentItemOffset / onePageWidth
        }
    }

    private fun getDestItemOffset(destPos: Int) = onePageWidth * destPos

    /**
     *  防止卡片在第一页和最后一页因无法"居中"而一直循环调用onScrollStateChanged-->
     *  SnapHelper.snapToTargetExistingView-->onScrollStateChanged
     */
    class CardLinearSnapHelper : LinearSnapHelper() {
        var noNeedToScroll = false
        override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
            return if (noNeedToScroll) {
                intArrayOf(0, 0)
            } else {
                super.calculateDistanceToFinalSnap(layoutManager, targetView)
            }
        }
    }
}
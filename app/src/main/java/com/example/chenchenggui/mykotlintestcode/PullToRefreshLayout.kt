package com.example.chenchenggui.mykotlintestcode

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.support.annotation.NonNull
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

/**
 * description ：弹性拉伸刷新布局
 * author : chenchenggui
 * creation date: 2018/10/19
 */
class PullToRefreshLayout(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    companion object {
        private const val BACK_ANIM_DUR = 500L
        private const val BEZIER_ANIM_DUR = 350L
        private const val ROTATION_ANIM_DUR = 200L
        private const val DEFAULT_MARGIN_RIGHT = 26f
        private const val DEFAULT_PULL_WIDTH = 100f
        private const val DEFAULT_MOVE_MAX_DIMEN = 40f
        private const val DEFAULT_FOOTER_WIDTH = 20f

        private const val DEFAULT_SCAN_MORE = "查看更多"
        private const val DEFAULT_RELEASE_SCAN_MORE = "释放查看"
        private const val ROTATION_ANGLE = 180f

        private val animationInterpolator = LinearInterpolator()

        /**
         * 滑动最大距离
         */
        private var moreViewMoveMaxDimen = 0f
        private var scanMore: String? = null
        private var releaseScanMore: String? = null
    }

    private var touchStartX = 0f
    private var touchCurrX = 0f

    /**
     * 拉伸距离
     */
    private var pullWidth = 0f
    /**
     * 脚布局宽度
     */
    private var footerWidth = 0f

    /**
     * more_view右边距
     */
    private var moreViewMarginRight = 0
    /**
     * 脚局部背景颜色
     */
    private var footerViewBgColor: Int = Color.GRAY

    private var isRefresh = false
    private var scrollState = false

    private var childView: View? = null
    private var footerView: AnimView? = null
    private var moreView: View? = null
    /**
     * 加载更多文字
     */
    private var moreText: TextView? = null
    /**
     * 可显示拖拽方向图标
     */
    private var arrowIv: ImageView? = null

    private var backAnimator: ValueAnimator? = null
    private var arrowRotateAnim: RotateAnimation? = null
    private var arrowRotateBackAnim: RotateAnimation? = null

    /**
     * 滑动监听
     */
    var scrollListener: ((Boolean) -> Unit)? = null

    fun setOnScrollListener(listener: (Boolean) -> Unit) {
        this.scrollListener = listener
    }

    /**
     * 刷新监听
     */
    var refreshListener: (() -> Unit)? = null

    fun setOnRefreshListener(listener: (() -> Unit)) {
        this.refreshListener = listener
    }

    private var interpolator = DecelerateInterpolator(10f)

    init {
        val displayMetrics = resources.displayMetrics
        val defaultPullWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_PULL_WIDTH, displayMetrics)
        val defaultMoreViewMoveMaxDimen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_MOVE_MAX_DIMEN, displayMetrics)
        val defaultFooterWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_FOOTER_WIDTH, displayMetrics)
        val defaultMoreViewMarginRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_MARGIN_RIGHT, displayMetrics)
        scanMore = DEFAULT_SCAN_MORE
        releaseScanMore = DEFAULT_RELEASE_SCAN_MORE

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshLayout)
        pullWidth = ta.getDimension(R.styleable.PullToRefreshLayout_pullWidth, defaultPullWidth)
        moreViewMoveMaxDimen = ta.getDimension(R.styleable.PullToRefreshLayout_moreViewMoveMaxDimen,
                defaultMoreViewMoveMaxDimen)
        moreViewMarginRight = -ta.getDimension(R.styleable.PullToRefreshLayout_moreViewMarginRight,
                defaultMoreViewMarginRight).toInt()

        footerViewBgColor = ta.getColor(R.styleable.PullToRefreshLayout_footerBgColor, Color.GRAY)
        footerWidth = ta.getDimension(R.styleable.PullToRefreshLayout_footerWidth, defaultFooterWidth)


        val scanMoreText = ta.getString(R.styleable.PullToRefreshLayout_scanMoreText)
        val releaseScanMoreText = ta.getString(R.styleable.PullToRefreshLayout_releaseScanMoreText)

        if (!TextUtils.isEmpty(scanMoreText)) {
            scanMore = scanMoreText
        }
        if (!TextUtils.isEmpty(releaseScanMoreText)) {
            releaseScanMore = releaseScanMoreText
        }
        ta.recycle()

        post {
            childView = getChildAt(0)
            addFooterView()
            addMoreView()
            initBackAnim()
            initRotateAnim()
        }
    }

    private fun addFooterView() {
        val params = FrameLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        val margin = (resources.displayMetrics.density * 10 + 0.5f).toInt()
        params.topMargin = margin
        params.bottomMargin = margin
        params.gravity = Gravity.RIGHT

        footerView = AnimView(context).apply {
            layoutParams = params
            setBgColor(footerViewBgColor)
            bezierBackDur = BEZIER_ANIM_DUR
        }
        addViewInternal(footerView!!)
    }

    private fun addMoreView() {
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        params.setMargins(0, 0, moreViewMarginRight, 0)

        moreView = LayoutInflater.from(context).inflate(R.layout.item_load_more, this, false).apply {
            layoutParams = params
            moreText = findViewById(R.id.tv_more_text)
        }
        addViewInternal(moreView!!)

    }

    private fun initBackAnim() {
        if (childView == null) {
            return
        }
        backAnimator = ValueAnimator.ofFloat(pullWidth, 0f).apply {
            addListener(AnimListener())

            addUpdateListener {
                var value: Float = it.animatedValue as Float
                if (value <= footerWidth) {
                    value *= interpolator.getInterpolation(value / footerWidth)
                    footerView?.layoutParams?.width = value.toInt()
                    footerView?.requestLayout()
                }

                childView?.translationX = -value

                moveMoreView(value, true)
            }

            duration = BACK_ANIM_DUR
        }
    }

    private fun moveMoreView(offsetX: Float, release: Boolean) {
        val dx = offsetX / 2
        if (dx <= moreViewMoveMaxDimen) {
            moreView?.translationX = -dx
            if (!release && switchMoreText(scanMore)) {
                arrowIv?.clearAnimation()
                arrowIv?.startAnimation(arrowRotateBackAnim)
            }
        } else {
            if (switchMoreText(releaseScanMore)) {
                arrowIv?.clearAnimation()
                arrowIv?.startAnimation(arrowRotateAnim)
            }
        }
    }

    private fun switchMoreText(text: String?): Boolean {
        if (TextUtils.equals(text, moreText?.text.toString())) {
            return false
        }
        moreText?.text = text
        return true
    }

    private fun initRotateAnim() {
        val pivotType = Animation.RELATIVE_TO_SELF
        val pivotValue = 0.5f
        arrowRotateAnim = RotateAnimation(0f, ROTATION_ANGLE, pivotType, pivotValue, pivotType,
                pivotValue).apply {
            interpolator = animationInterpolator
            duration = ROTATION_ANIM_DUR
            fillAfter = true
        }
        arrowRotateBackAnim = RotateAnimation(ROTATION_ANGLE, 0f, pivotType, pivotValue, pivotType,
                pivotValue).apply {
            interpolator = animationInterpolator
            duration = ROTATION_ANIM_DUR
            fillAfter = true
        }
    }

    private fun addViewInternal(@NonNull child: View) {
        super.addView(child)
    }

    override fun addView(child: View?) {
        if (childCount >= 1) {
            throw  RuntimeException("only can attach one child")
        }
        childView = child
        super.addView(child)
    }

    inner class AnimListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            if (isRefresh) {
                refreshListener?.invoke()
            }
            moreText?.text = scanMore
            arrowIv?.clearAnimation()
            isRefresh = false
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isRefresh) return true

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStartX = ev.x
                touchCurrX = touchStartX
                setScrollState(false)
            }
            MotionEvent.ACTION_MOVE -> {
                val currX = ev.x
                val dx = currX - touchStartX
                if (dx < -10 && !canScrollRight()) {
                    setScrollState(true)
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isRefresh) {
            return super.onTouchEvent(event)
        }
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                touchCurrX = event.x
                var dx = touchStartX - touchCurrX
                dx = Math.min(pullWidth * 2, dx)
                dx = Math.max(0f, dx)

                if (childView == null || dx <= 0) {
                    return true
                }

                val unit = dx / 2
                val offsetX = interpolator.getInterpolation(unit / pullWidth) * unit
                //超过最大距离后开始计算偏移量
                val offsetY = interpolator.getInterpolation(unit / height) * unit - moreViewMoveMaxDimen
                childView?.translationX = -offsetX
                footerView?.layoutParams?.width = offsetX.toInt()
                footerView?.top = offsetY
                footerView?.requestLayout()

                moveMoreView(offsetX, false)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (childView == null) {
                    return true
                }

                val childDx = Math.abs(childView?.translationX!!)
                backAnimator?.setFloatValues(childDx, 0f)
                backAnimator?.start()

                if (childDx >= footerWidth) {
                    footerView?.releaseDrag()

                    if (reachReleasePoint()) {
                        isRefresh = true
                    }
                }
                setScrollState(false)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun reachReleasePoint(): Boolean = TextUtils.equals(releaseScanMore, moreText?.text
            .toString())

    private fun canScrollRight(): Boolean = childView?.canScrollHorizontally(1) ?: false

    private fun setScrollState(scrollState: Boolean) {
        if (this.scrollState == scrollState) return
        this.scrollState = scrollState
        scrollListener?.invoke(scrollState)
    }
}


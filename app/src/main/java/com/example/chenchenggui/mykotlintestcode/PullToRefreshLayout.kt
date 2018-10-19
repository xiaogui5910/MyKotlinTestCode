package com.example.chenchenggui.mykotlintestcode

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.support.annotation.NonNull
import android.text.TextUtils
import android.util.AttributeSet
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
 * description ：滑动刷新view
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
        private const val DEFAULT_MARGIN_RIGHT = 26

        private const val DEFAULT_SCAN_MORE = "查看更多"
        private const val DEFAULT_RELEASE_SCAN_MORE = "释放查看"
        private const val ROTATION_ANGLE = 180f

        private val animationInterpolator = LinearInterpolator()
        private var moreViewMoveDimen = 0f
        private var scanMore: String? = null
        private var releaseScanMore: String? = null
    }

    private var touchStartX = 0f
    private var touchCurrX = 0f

    private var pullWidth = 0f
    private var footerWidth = 0f

    private var moreViewMarginRight = 0
    private var footerViewBgColor: Int = Color.GRAY

    private var isRefresh = false
    private var scrollState = false

    private var childView: View? = null
    private var footerView: AnimView? = null
    private var moreView: View? = null
    private var moreText: TextView? = null
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
        pullWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, displayMetrics)
        moreViewMoveDimen = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, displayMetrics)
        footerWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, displayMetrics)
        moreViewMarginRight = -(DEFAULT_MARGIN_RIGHT * displayMetrics.density + 0.5f).toInt()
        scanMore = DEFAULT_SCAN_MORE
        releaseScanMore = DEFAULT_RELEASE_SCAN_MORE

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshLayout)
        footerViewBgColor = ta.getColor(R.styleable.PullToRefreshLayout_footerBgColor, Color.GRAY)
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
        var dx = offsetX / 2
        if (dx <= moreViewMoveDimen) {
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
                var currX = ev.x
                var dx = currX - touchStartX
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

                var unit = dx / 2
                var offsetX = interpolator.getInterpolation(unit / pullWidth) * unit
                childView?.translationX = -offsetX
                footerView?.layoutParams?.width = offsetX.toInt()
                footerView?.requestLayout()

                moveMoreView(offsetX, false)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (childView == null) {
                    return true
                }

                var childDx = Math.abs(childView?.translationX!!)
                if (childDx >= footerWidth) {
                    backAnimator?.setFloatValues(childDx, 0f)
                    backAnimator?.start()

                    if (reachReleasePoint()) {
                        isRefresh = true
                    }
                } else {
                    backAnimator?.setFloatValues(childDx, 0f)
                    backAnimator?.start()
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


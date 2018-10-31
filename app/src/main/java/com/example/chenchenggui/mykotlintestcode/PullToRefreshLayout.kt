package com.example.chenchenggui.mykotlintestcode

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.support.annotation.NonNull
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
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
import android.widget.Toast


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
        private const val DEFAULT_MARGIN_RIGHT = 10f
        private const val DEFAULT_PULL_WIDTH = 100f
        private const val DEFAULT_MOVE_MAX_DIMEN = 50f
        private const val DEFAULT_FOOTER_WIDTH = 50f
        private const val DEFAULT_VISIBLE_WIDTH = 40f

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
        private var defaultOffsetX = 0f
    }

    private var touchStartX = 0f
    private var touchCurrX = 0f
    private var touchLastX = 0f

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

    private var childView: RecyclerView? = null
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
    private var isFooterViewShow = false

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
        defaultOffsetX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_VISIBLE_WIDTH, displayMetrics)
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
            childView = getChildAt(0) as RecyclerView?
            childView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        //加载更多功能的代码
                        log("newState0=$newState")
                        if (!canScrollRight()) {

                            isFooterViewShow = true
                            childView?.translationX = -defaultOffsetX
                            footerView?.layoutParams?.width = defaultOffsetX.toInt()
                            footerView?.requestLayout()

                            moveMoreView(defaultOffsetX, false)
                            animateScroll(0f, 600, false)
                        }

//                        //得到当前显示的最后一个item的view
//                        val lastChildView=childView?.layoutManager?.getChildAt(childView?.layoutManager?.childCount!!-1)
//                        //得到lastChildView的bottom坐标值
//                        val lastChildBottom = lastChildView?.bottom
//                        //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
//                        val recyclerBottom =  childView?.bottom!!-childView?.paddingBottom!!
//                        //通过这个lastChildView得到这个view当前的position值
//                        val lastPosition  = childView?.layoutManager?.getPosition(lastChildView!!)
//
//                        //判断lastChildView的bottom值跟recyclerBottom
//                        //判断lastPosition是不是最后一个position
//                        //如果两个条件都满足则说明是真正的滑动到了底部
//                        if(lastChildBottom == recyclerBottom && lastPosition == childView?.layoutManager?.itemCount!!-1
//                        &&scrollX== -defaultOffsetX.toInt()){
//                            log("到底了。。不能滑动咯")
//                            recyclerView.smoothScrollToPosition(childView?.adapter?.itemCount!!-1)
//                            animateScroll(0f, 600, false)
//                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
            addFooterView()
            addMoreView()
            initBackAnim()
            initRotateAnim()
        }
    }


    private fun addFooterView() {
        val params = FrameLayout.LayoutParams(0, ViewGroup.LayoutParams
                .MATCH_PARENT)
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
        Log.e("layout", "moreViewMarginRight=$moreViewMarginRight")
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
                var offsetX = value
                log("value=$value===footerWidth=$footerWidth")
                var offsetY = interpolator.getInterpolation(value / height) * value
                log("addUpdateListener-offsetY11=$offsetY")
                val interpolation = interpolator.getInterpolation(value / footerWidth)

                if (value <= footerWidth) {
//                    log("addUpdateListener-offsetY22=$offsetY")
                    offsetX *= interpolation
                    if (offsetX <= defaultOffsetX) {
                        log("offsetX=$offsetX==defaultOffsetX=$defaultOffsetX")
                        offsetX = defaultOffsetX
                    }
                    footerView?.layoutParams?.width = offsetX.toInt()
                    footerView?.top = offsetY
                    footerView?.requestLayout()
                    log("offsetX <= footerWidth===$offsetX==offsetY=$offsetY")
                } else {
                    log("addUpdateListener--value=$value")
                    //记录当前收缩动画的宽高
                    if (offsetY >= animStartTop) {
                        offsetY = animStartTop
                    }
                    footerView?.top = offsetY
                    footerView?.layoutParams?.width = offsetX.toInt()
                }

                childView?.translationX = -offsetX

                moveMoreView(offsetX, true)
            }

            duration = BACK_ANIM_DUR
        }
    }

    private fun moveMoreView(offsetX: Float, release: Boolean, move: Boolean = false) {
        val dx = offsetX / 2
        log("moveMoreView==dx=$dx==moreViewMoveMaxDimen=$moreViewMoveMaxDimen==release=$release" +
                "==canScrollRight()=${canScrollRight()}")
        moreView?.visibility = if (move) View.VISIBLE else View.INVISIBLE
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
        childView = child as RecyclerView?
        super.addView(child)
    }

    inner class AnimListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {

            moreText?.text = scanMore
            arrowIv?.clearAnimation()
            isRefresh = false
            log("onAnimation--isFooterViewShow----")
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
            if (isRefresh) {
                refreshListener?.invoke()
                log("onAnimation--start----0000")
            }
            animStartTop = footerView?.top!!
            log("onAnimation--start----1111")
        }
    }

    var animStartTop = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isRefresh) return true

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStartX = ev.x
                touchLastX = ev.x
                touchCurrX = touchStartX
                setScrollState(false)
            }
            MotionEvent.ACTION_MOVE -> {
                val currX = ev.x
                val dx = touchStartX - currX
                touchLastX = currX
                Log.e("layout", "onInterceptTouchEvent---111==dx=$dx--scrollX=$scrollX")
                val canScrollHorizontally = childView?.canScrollHorizontally(1)
                log("onInterceptTouchEvent---canScrollHorizontally11=$canScrollHorizontally" +
                        "===footerView?.width=${footerView?.width}")
                if (dx > 10 && !canScrollRight() && scrollX >= 0) {
                    setScrollState(true)
                    Log.e("layout", "onInterceptTouchEvent---111---canScrollRight")
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
                Log.e("layout", "onTouchEvent=touchStartX - touchCurrX==${touchStartX - touchCurrX}")
                var dx = touchStartX - touchCurrX

                log("dx==$dx")
                if (childView == null) return true
                log("dx22=$dx")
                dx = Math.min(pullWidth * 2, dx)
                dx = Math.max(0f, dx)
                val unit = dx / 2
                val input = unit / (pullWidth)

                val interpolation = interpolator.getInterpolation(input)
                var offsetX = interpolation * unit
                //超过最大距离后开始计算偏移量
                log("input=$input==interpolation=$interpolation")


                val offsetY = interpolator.getInterpolation(unit / height) * unit - moreViewMoveMaxDimen
                if (isFooterViewShow) {
                    log("isFooterViewShow=$isFooterViewShow")
                    offsetX += defaultOffsetX
                }
                log("offsetX22=$offsetX")
                childView?.translationX = -offsetX
                footerView?.layoutParams?.width = offsetX.toInt()
                footerView?.top = offsetY
                footerView?.requestLayout()

                moveMoreView(offsetX, false, true)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (childView == null) {
                    return true
                }
                if (childView?.translationX!! >= 0) {
                    return true
                }
                val childDx = Math.abs(childView?.translationX!!)
                log("ACTION_UP===childDx=$childDx--scrollX=$scrollX")
                if (reachReleasePoint()) {
                    isFooterViewShow = true
                    isRefresh = true
                }
                backAnimator?.setFloatValues(childDx, 0f)
                backAnimator?.start()

                if (childDx >= footerWidth) {
                    footerView?.releaseDrag(defaultOffsetX)

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

    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_HORIZONTAL != 0
    }

    override fun onNestedPreScroll(target: View?, dx: Int, dy: Int, consumed: IntArray) {
        log("onNestedPreScroll---dx=$dx--dy=$dy--scrollX=$scrollX--footerwidth=${footerView?.width}")
        val hiddenMoreView = dx < 0 && scrollX > -defaultOffsetX && !canScrollRight()
                && footerView?.width != 0

        val showMoreView = dx > 0 && scrollX < 0 && !canScrollRight()
        log("onNestedPreScroll---hiddenMoreView=$hiddenMoreView--showMoreView=$showMoreView")
        if (hiddenMoreView || showMoreView) {
            scrollBy(dx, 0)
            consumed[0] = dx
        }
    }

    override fun onStopNestedScroll(child: View?) {
        super.onStopNestedScroll(child)
        log("onStopNestedScroll---tranx=${childView?.translationX}---scrollX=$scrollX--cancrollright=${canScrollRight()}")

        animateScroll(0f, 600, false)

        log("onStopNestedScroll---")
    }

    override fun onNestedFling(target: View?, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        var realConsumed = consumed

        log("onNestedFling--velocityX=$velocityX")
        if (target is RecyclerView && velocityX > 0) {
            val firstChild = target.getChildAt(0)
            val childAdapterPosition = target.getChildAdapterPosition(firstChild)
            realConsumed = childAdapterPosition > 3
        }
        log("onNestedFling--realConsumed=$realConsumed---consumed=$consumed")

        if (!realConsumed) {
            animateScroll(velocityX, computeDuration(0f), realConsumed)
        } else {
            animateScroll(velocityX, computeDuration(velocityX), realConsumed)
        }
        return true
    }

    private fun computeDuration(velocityX: Float): Int {
        val distance: Int = if (velocityX > 0) {
            Math.abs(defaultOffsetX.toInt() - scrollX)
        } else {
            Math.abs(defaultOffsetX.toInt() - (defaultOffsetX.toInt() - scrollX))
        }

        val duration: Int
        val realVelocityX = Math.abs(velocityX)

        duration = if (realVelocityX > 0) {
            3 * Math.round(1000 * (distance / realVelocityX))
        } else {
            val distanceRatio = distance.toFloat() / width
            ((distanceRatio + 1) * 150).toInt()
        }

        return duration
    }

    override fun onNestedPreFling(target: View?, velocityX: Float, velocityY: Float): Boolean {
        //隐藏moreView过程中消费掉fling
        log("onNestedPreFling--velocityX=$velocityX--scrollX=$scrollX--footerWidth" +
                "=${footerView?.width}--${mOffsetAnimator?.isRunning}")

        var isAnimRunning = false
        if (mOffsetAnimator != null) {
            isAnimRunning = mOffsetAnimator!!.isRunning
        }
        if (velocityX < 0 && scrollX >= -defaultOffsetX && !canScrollRight()
                || isAnimRunning) {
            return true
        }
        return false
    }

    private var mOffsetAnimator: ValueAnimator? = null
    private fun animateScroll(velocityX: Float, duration: Int, consumed: Boolean) {
        if (canScrollRight()) {
            return
        }
        val currentOffset = scrollX
        if (mOffsetAnimator == null) {
            mOffsetAnimator = ValueAnimator()
            mOffsetAnimator?.addUpdateListener { animation ->
                if (animation.animatedValue is Int) {
                    scrollTo(animation.animatedValue as Int, 0)
                }
            }
        } else {
            mOffsetAnimator?.cancel()
        }
        mOffsetAnimator?.duration = Math.min(duration, 600).toLong()

        if (velocityX >= 0) {
            mOffsetAnimator?.setIntValues(currentOffset, 0)
            mOffsetAnimator?.start()
        } else {
            //如果子View没有消耗down事件 那么就让自身滑倒0位置
            if (!consumed) {
                mOffsetAnimator?.setIntValues(currentOffset, 0)
                mOffsetAnimator?.start()
            }

        }
    }

    /**
     * 限定滚动的范围，scrollBy默认调用scrollTo
     */
    override fun scrollTo(x: Int, y: Int) {
        log("x=$x--scrollX=$scrollX")
        var realX = x
        if (x >=0) {
            realX = 0
        }
        if (x <= -defaultOffsetX) {
            realX = -defaultOffsetX.toInt()
        }
        if (realX != scrollX) {
            super.scrollTo(realX, y)
        }
    }

    /**
     * 获取嵌套滑动的轴
     * @see ViewCompat.SCROLL_AXIS_HORIZONTAL 垂直
     * @see ViewCompat.SCROLL_AXIS_VERTICAL 水平
     * @see ViewCompat.SCROLL_AXIS_NONE 都支持
     */
    override fun getNestedScrollAxes(): Int {
        return ViewCompat.SCROLL_AXIS_NONE
    }
}

inline fun log(msg: Any) {
    Log.e("layout", msg.toString())
}


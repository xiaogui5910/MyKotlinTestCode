package com.example.chenchenggui.mykotlintestcode

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.gyf.immersionbar.ImmersionBar


/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/12/14
 */
fun log(msg: Any) {
    Log.e("my_kotlin_test_code", "$msg")
}

fun toast(msg: Any) {
    Toast.makeText(MyApplication.instance, msg.toString(), Toast.LENGTH_SHORT).show()
}

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun dp2px(context: Context, dpValue: Float): Float {
    val scale = context.resources.displayMetrics.density
    return dpValue * scale + 0.5f
}

/**
 * 判断view是否完全展示在屏幕中,true:完全可见;false:不完全可见
 * 1. 只要该视图没有被遮挡rectImage.getLocalVisibleRect()的坐标总是等于:(0, 0, rectImage.getwidth(), rectImage.getheight()),
 * 这时很明显调用getLocalVisibleRect(Rect r)后localRect变量中的right和bottom正是图片的长和宽。而遮挡后的left和top则为以View本身左上角进行计算的偏移量
 * 2. 调用getGlobalVisibleRect(Rect r)后localRect变量中的top是ActionBar的高度+View的上边距,bottom是top+View的高;left则为View的左边距，right为left+View的宽。
 * 3. getLocalVisibleRect(Rect r)方法可以把View的长和宽映射到一个Rect对象上，映射的参照物就是View本身左上角的坐标系。
 * 而getGlobalVisibleRect(Rect r)方法可以把View在整个屏幕中的偏移量映射到一个Rect对象上，映射的参照物是屏幕左上角的坐标系。
 */
fun View.isVisibleLocal(): Boolean {
    val rect = Rect()
    //获取视图本身可见的坐标区域，坐标以自己的左上角为原点（0，0）1. true : View 全部或者部分 可见 2. false : View 全部不可见
    getLocalVisibleRect(rect)
//    val rect1 = Rect()
    //获取视图在屏幕坐标中的可视区域,屏幕左上角为原地(0,0)
//    target.getGlobalVisibleRect(rect1)
    //方法2:通过差值和宽高比较来判断是否完全展示
//    return rect.right - rect.left == width && rect.bottom - rect.top == height
    return rect.top == 0 && rect.left == 0 && rect.right == width && rect.bottom == height
}

/**
 * 通过getLocationInWindow()去判断viw是否展示在屏幕中,需要手动计算,
 * 剔除掉view上面的topView和view底部的bottomView
 */
fun isViewShowOnScreen(activity: Activity, view: View, topView: View, bottomView: View): Boolean {
    val p = Point()
    activity.windowManager.defaultDisplay.getSize(p)
    val screenWidth: Int = p.x
    val screenHeight: Int = p.y
    val location = IntArray(2)
    view.getLocationInWindow(location)
//    val location1 = IntArray(2)
//    view.getLocationOnScreen(location1)
    val statusH = ImmersionBar.getStatusBarHeight(activity)
    val topHeight = topView.height
    val bottomHeight = bottomView.height
    return location[1] > statusH + topHeight && location[1] + view.height < screenHeight - bottomHeight
}

/**
 * 获取屏幕真实宽高
 */
fun getRealScreenSize(context: Context): Point? {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getRealSize(size)
    return size
}

/**
 * 获取应用可屏幕宽高
 */
fun getAppUsableScreenSize(context: Context): Point? {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

fun getDisplayMetrics(context: Context): DisplayMetrics? {
    return context.resources.displayMetrics
}

/**
 * 获取屏幕宽度
 */
fun getScreenWidth(context: Context): Int {
    return getDisplayMetrics(context)!!.widthPixels
}

/**
 * 获取屏幕高度
 */
fun getScreenHeight(context: Context): Int {
    return getDisplayMetrics(context)!!.heightPixels
}

/**
 * 获取真实屏幕宽度
 */
fun getRealScreenWidth(context: Context): Int {
    val metrics = DisplayMetrics()
    getDisplay(context)?.getRealMetrics(metrics)
    return metrics.widthPixels
}

/**
 * 获取真实屏幕高度
 */
fun getRealScreenHeight(context: Context): Int {
    val metrics = DisplayMetrics()
    getDisplay(context)?.getRealMetrics(metrics)
    return metrics.heightPixels
}

fun getDisplay(context: Context): Display? {
    return (context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
}

fun getDisplay(activity: Activity): Display? {
    return activity.windowManager.defaultDisplay
}

fun Context.isWifiConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
}

fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = true
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = true
                }
            }
        }
    }
    return result
}
//    fun getLocalVisibleRect(activity: Activity, view: View, offsetY: Int): Boolean {
//        val p = Point()
//        activity.windowManager.defaultDisplay.getSize(p)
//        val screenWidth: Int = p.x
//        val screenHeight: Int = p.y
//        val rect = Rect(0, 0, screenWidth, screenHeight)
//        val location = IntArray(2)
//        location[1] = location[1] + dp2px(activity, offsetY.toFloat()).toInt()
//        view.getLocationInWindow(location)
//        view.tag = location[1] //存储y方向的位置
//        return view.getLocalVisibleRect(rect)
//    }
//
//    fun isViewShowOnScreen(view: View): Boolean {
//        val p = Point()
//        windowManager.defaultDisplay.getSize(p)
//        val screenWidth: Int = p.x
//        val screenHeight: Int = p.y
//        val rect = Rect(0, 0, screenWidth, screenHeight)
//        val location = IntArray(2)
//        view.getLocationInWindow(location)
//        val location1 = IntArray(2)
//        view.getLocationOnScreen(location1)
//        val statusH = ImmersionBar.getStatusBarHeight(this)
//        val topHeight = iv_title_back.height
//        val bottomHeight = cv_bottom_view.height
//        val isVisible = location[1] > statusH + topHeight && location[1]+view.height < screenHeight - bottomHeight
//        return isVisible
//    }
//
//    //当 View 有一点点不可见时立即返回false!
//    fun isVisibleLocal(target: View): Boolean {
//        val rect = Rect()
//        target.getLocalVisibleRect(rect)
//        val rect1 = Rect()
//        target.getGlobalVisibleRect(rect1)
//        return rect.top == 0
//    }






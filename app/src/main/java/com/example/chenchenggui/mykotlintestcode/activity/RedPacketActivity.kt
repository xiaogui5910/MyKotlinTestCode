package com.example.chenchenggui.mykotlintestcode.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.TextView
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.redpacket.RedPacket
import com.example.chenchenggui.mykotlintestcode.redpacket.RedPacketDialog
import com.example.chenchenggui.mykotlintestcode.redpacket.RedPacketView
import kotlinx.android.synthetic.main.activity_red_packet.*

class RedPacketActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var redRainView1: RedPacketView
    private lateinit var start: Button
    private lateinit var stop: Button
    private lateinit var money: TextView
    private var totalmoney = 0
    private lateinit var ab: AlertDialog.Builder
    private lateinit var tvCount: TextView
    private lateinit var tbRedPacket: Toolbar
    private var countDownTimer: CountDownTimer? = null
    private var countDownbgTimer: CountDownTimer? = null
    /**
     * 为了模仿网络卡顿，来迟了~~
     */
    private var clickCount = 0

    //展示、回退、隐藏红包雨降临提示条动画
    private var showAnim: ValueAnimator? = null
    private var hideAnim: ValueAnimator? = null
    private var backAnim: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_red_packet)


        ab = AlertDialog.Builder(this)
        start = findViewById(R.id.start)
        stop = findViewById(R.id.stop)
        money = findViewById(R.id.money)
        redRainView1 = findViewById(R.id.red_packets_view1)
        tvCount = findViewById(R.id.tv_count)
        tbRedPacket = findViewById(R.id.tb_red_packet)

        start.setOnClickListener(this)
        stop.setOnClickListener(this)
        rl_red_packet_entrance.setOnClickListener(this)
        iv_rain_close.setOnClickListener(this)
        iv_count_down_close.setOnClickListener(this)

        setSupportActionBar(tbRedPacket)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "红包雨"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_red_packet, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.show -> {
                start.visibility = View.VISIBLE
                stop.visibility = View.VISIBLE
                money.visibility = View.VISIBLE

                ll_time.visibility = View.GONE
                rl_red_packet_entrance.visibility = View.GONE
            }
            R.id.project -> {
                start.visibility = View.GONE
                stop.visibility = View.GONE
                money.visibility = View.GONE

                rl_red_packet_entrance.visibility = View.VISIBLE
                rl_red_packet_entrance.viewTreeObserver.addOnGlobalLayoutListener(object
                    : ViewTreeObserver.OnGlobalLayoutListener {
                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
                    override fun onGlobalLayout() {
                        rl_red_packet_entrance.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        rl_red_packet_entrance.translationX = rl_red_packet_entrance.measuredWidth.toFloat()
                    }
                })

                rl_red_packet_entrance.post {
                    showAnim = ValueAnimator.ofInt(0, rl_red_packet_entrance.measuredWidth)
                            .apply {
                                addListener(object : RedPacketAnimatorListener() {
                                    override fun onAnimationEnd(animation: Animator?) {
                                        startBackAnim()
                                    }
                                })
                                addUpdateListener {
                                    val dx = it.animatedValue as Int
                                    rl_red_packet_entrance.translationX = (rl_red_packet_entrance
                                            .measuredWidth - dx).toFloat()
                                }
                                duration = 2000
                                interpolator = BounceInterpolator()
                                start()
                            }
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startBackAnim() {
        rl_red_packet_entrance.postDelayed({
            backAnim = ValueAnimator
                    .ofInt(0, (this.resources.displayMetrics.density * 86).toInt())
                    .apply {
                        addUpdateListener {
                            val dx = it.animatedValue as Int
                            rl_red_packet_entrance.translationX = dx.toFloat()
                        }
                        duration = 2000
                        start()
                        addListener(object : RedPacketAnimatorListener() {
                            override fun onAnimationEnd(animation: Animator?) {
                                startHideAnim()
                            }
                        })

                    }
        }, 2000)
    }

    private fun startHideAnim() {
        Handler().postDelayed({
            hideAnim = ValueAnimator.ofInt((this.resources.displayMetrics.density * 86).toInt(),
                    rl_red_packet_entrance.measuredWidth)
                    .apply {
                        addUpdateListener {
                            val dx = it.animatedValue as Int
                            rl_red_packet_entrance.translationX = dx.toFloat()
                        }
                        addListener(object : RedPacketAnimatorListener() {
                            override fun onAnimationEnd(animation: Animator?) {
//                                showCountDownBoard()
                            }

                            override fun onAnimationStart(animation: Animator?) {
                                super.onAnimationStart(animation)
                            }
                        })
                        duration = 1000
                        start()
                    }
        }, 2000)
    }

    private fun showCountDownBoard() {
        fl_count_down_bg.visibility = View.VISIBLE
        countDownbgTimer = object : CountDownTimer(4 * 1000, 1000) {
            override fun onFinish() {
                fl_count_down_bg.visibility = View.GONE
                ll_time.visibility = View.VISIBLE

                tvCount.text = "5"
                startRedRain1()
                updateUiWithStartRain()
                countDownTimer = object : CountDownTimer(6 * 1000, 1000) {
                    override fun onFinish() {
                        tvCount.text = "5"
                        ll_time.visibility = View.GONE
                        stopRedRain()
                        updateUiWithStopRain()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        tvCount.text = (millisUntilFinished / 1000).toString()
                    }

                }.start()
            }

            override fun onTick(millisUntilFinished: Long) {
                val count = millisUntilFinished / 1000
                when (count.toInt()) {
                    3,2,1,0 -> {
                        showCountDown(count.toInt())
                    }
                    else -> {
                        iv_count_down.visibility = View.VISIBLE
                        iv_count_down.setImageResource(R.drawable.rain_num_3)
                    }
                }
            }

        }.start()
    }
    private fun showCountDown(repeatCount:Int) {
        iv_anim_countdown.visibility = View.VISIBLE
        iv_count_down.visibility = View.VISIBLE
        when (repeatCount) {
            3 -> {
                iv_anim_countdown.setImageResource(R.drawable.rain_num_3)
                iv_count_down.setImageResource(R.drawable.rain_num_3)
            }
            2 -> {
                iv_anim_countdown.setImageResource(R.drawable.rain_num_2)
                iv_count_down.setImageResource(R.drawable.rain_num_2)
            }
            1 -> {
                iv_anim_countdown.setImageResource(R.drawable.rain_num_1)
                iv_count_down.setImageResource(R.drawable.rain_num_1)
            }
            0 -> {
                iv_anim_countdown.setImageResource(R.drawable.rain_num_0)
                iv_count_down.setImageResource(R.drawable.rain_num_0)
            }
            -1 -> {
                iv_anim_countdown.setImageResource(R.drawable.rain_num_3)
                iv_count_down.setImageResource(R.drawable.rain_num_3)
                iv_anim_countdown.visibility = View.GONE
                iv_count_down.visibility = View.GONE
            }
        }

        val animSet = AnimatorSet()
        val scaleX = ObjectAnimator.ofFloat(iv_anim_countdown, "scaleX", 1f, 2.5f)
        val scaleY = ObjectAnimator.ofFloat(iv_anim_countdown, "scaleY", 1f, 2.5f)
        val alpha = ObjectAnimator.ofFloat(iv_anim_countdown, "alpha", 1f, 0.15f)
        scaleX.repeatCount = 5
        scaleY.repeatCount = 5
        alpha.repeatCount = 5
        animSet.duration = 1000
//        scaleX.interpolator = AccelerateInterpolator(5f)
//        scaleY.interpolator = AccelerateInterpolator(5f)
//        alpha.interpolator = AccelerateInterpolator(5f)
        animSet.play(scaleX).with(scaleY).with(alpha)
        animSet.start()
    }

    private fun updateUiWithStartRain() {
        iv_rain_bg.visibility = View.VISIBLE
        iv_rain_close.visibility =View.VISIBLE
    }
    private fun updateUiWithStopRain(){
        iv_rain_bg.visibility = View.GONE
        iv_rain_close.visibility =View.GONE
    }

    override fun onClick(v: View?) {
        Log.e("test", "onclick")
        when (v?.id) {
            R.id.start -> {
//                startRedRain()
                countDownTimer?.cancel()
                tvCount.text = "5"
                startRedRain1()
            }
            R.id.stop -> stopRedRain()
            R.id.rl_red_packet_entrance -> {
                if (clickCount >= 2) {
                    showEndDialog()
                    clickCount = 0
                } else {
                    showCountDownBoard()
                }
                rl_red_packet_entrance.visibility = View.GONE
                clearAnim()
                clickCount++
            }
            //红包雨进行时关闭
            R.id.iv_rain_close->{
                redRainView1.stopRainNow()
                countDownTimer?.cancel()
                ll_time.visibility = View.GONE
                updateUiWithStopRain()
            }
            //倒计时开始时关闭
            R.id.iv_count_down_close->{
                countDownbgTimer?.cancel()
                fl_count_down_bg.visibility = View.GONE
            }
        }
    }

    /**
     * 清空动画
     */
    private fun clearAnim() {
        showAnim?.cancel()
        backAnim?.cancel()
        hideAnim?.cancel()
    }

    private fun startRedRain1() {

        redRainView1.startRain()

        redRainView1.setOnRedPacketClickListener(object : RedPacketView.OnRedPacketClickListener {
            override fun onRedPacketClickListener(redPacket: RedPacket) {
                redRainView1.stopRainNow()
                countDownTimer?.cancel()
                ll_time.visibility = View.GONE
                updateUiWithStopRain()

                val dialog = RedPacketDialog()
                dialog.setOnDialogPositiveListener {
                    Log.e("red_pack", "activity-isRealRed=${redPacket.isRealRed}")
                    if (redPacket.isRealRed) {
                        dialog.openRedPacket(redPacket)
                        totalmoney += redPacket.money
                    } else {
                        dialog.openRedPacket(redPacket)
                    }
                }

                redRainView1.post {
                    dialog.show(supportFragmentManager, "red_packet")
                }
            }
        })
    }

    /**
     * 抽奖结束
     */
    private fun showEndDialog() {
        val dialog = RedPacketDialog()
        dialog.isEnd = true
        dialog.show(supportFragmentManager, "red_packet")
    }

    /**
     * 开始下红包雨
     */
    private fun startRedRain() {
        redRainView1.startRain()
        redRainView1.setOnRedPacketClickListener(object : RedPacketView.OnRedPacketClickListener {
            override fun onRedPacketClickListener(redPacket: RedPacket) {
                redRainView1.pauseRain()
                ab.setCancelable(false)
                ab.setTitle("红包提醒")
                ab.setNegativeButton("继续抢红包") { dialog, which -> redRainView1.restartRain() }

                if (redPacket.isRealRed) {
                    ab.setMessage("恭喜你，抢到了" + redPacket.money + "元！")
                    totalmoney += redPacket.money
                    money.text = ("中奖金额: $totalmoney")
                } else {
                    ab.setMessage("很遗憾，下次继续努力！")
                }
                redRainView1.post { ab.show() }
            }

        })
    }

    /**
     * 停止下红包雨
     */
    private fun stopRedRain() {
        totalmoney = 0//金额清零
        countDownTimer?.cancel()
        tvCount.text = "5"
        redRainView1.stopRainNow()
    }

    /**
     * 实现回调，方便只需要单独重新部分方法
     */
    open class RedPacketAnimatorListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }

    }
}

package com.example.chenchenggui.mykotlintestcode.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.redpacket.RedPacket
import com.example.chenchenggui.mykotlintestcode.redpacket.RedPacketDialog
import com.example.chenchenggui.mykotlintestcode.redpacket.RedPacketView

class RedPacketActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var redRainView1: RedPacketView
    private lateinit var start: Button
    private lateinit var stop: Button
    private lateinit var money: TextView
    private var totalmoney = 0
    private lateinit var ab: AlertDialog.Builder
    private lateinit var tvCount:TextView
    private var countDownTimer:CountDownTimer?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_red_packet)


        ab = AlertDialog.Builder(this)
        start = findViewById(R.id.start)
        stop = findViewById(R.id.stop)
        money = findViewById(R.id.money)
        redRainView1 = findViewById(R.id.red_packets_view1)
        tvCount=findViewById(R.id.tv_count)

        start.setOnClickListener(this)
        stop.setOnClickListener(this)
        tvCount.setOnClickListener(this)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "红包雨"

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start -> {
//                startRedRain()
                countDownTimer?.cancel()
                tvCount.text="5"
                startRedRain1()
            }
            R.id.stop -> stopRedRain()
            R.id.tv_count->{
                startRedRain1()
                countDownTimer=object :CountDownTimer(5*1000,1000){
                    override fun onFinish() {
                        tvCount.text="5"
                        stopRedRain()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        tvCount.text=(millisUntilFinished/1000).toString()
                    }

                }.start()
            }
        }
    }

    private fun startRedRain1() {
        redRainView1.startRain()

        redRainView1.setOnRedPacketClickListener(object : RedPacketView.OnRedPacketClickListener {
            override fun onRedPacketClickListener(redPacket: RedPacket) {
                redRainView1.stopRainNow()

                val dialog = RedPacketDialog()
                dialog.setOnDialogPositiveListener {
                    Log.e("red_pack","activity-isRealRed=${redPacket.isRealRed}")
                    if (redPacket.isRealRed) {
                        dialog.openRedPacket(redPacket)
                        totalmoney += redPacket.money
                    } else {
                        dialog.openRedPacket(redPacket)
                    }
                }

                redRainView1.post { dialog.show(supportFragmentManager, "red_packet") }
                Log.e("red_packet", "onRedPacketClickListener")
            }

        })
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
        tvCount.text="5"
        redRainView1.stopRainNow()
    }


}

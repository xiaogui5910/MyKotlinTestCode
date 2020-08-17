package com.example.chenchenggui.mykotlintestcode.redpacket

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/11/29
 */
class RedPacketRainInfoDialog : androidx.fragment.app.DialogFragment() {
    private lateinit var rvShowPrize: androidx.recyclerview.widget.RecyclerView
    private lateinit var ivClose: ImageView
    private lateinit var tvMinDecade: TextView
    private lateinit var tvMinUnit: TextView
    private lateinit var tvSecDecade: TextView
    private lateinit var tvSecUnit: TextView
    private val prizeList: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true

        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_red_packet_rain_info, null)
        rvShowPrize = rootView.findViewById(R.id.rv_show_prize)
        ivClose = rootView.findViewById(R.id.iv_red_packet_info_close)

        tvMinDecade = rootView.findViewById(R.id.tv_info_min_decade)
        tvMinUnit = rootView.findViewById(R.id.tv_info_min_unit)
        tvSecDecade = rootView.findViewById(R.id.tv_info_sec_decade)
        tvSecUnit = rootView.findViewById(R.id.tv_info_sec_unit)

        ivClose.setOnClickListener { dismiss() }
        initPrize()
        initCountDownTimer()
        return rootView
    }

    private var countDownTimer: CountDownTimer? = null
    /**
     * 倒计时
     */
    private fun initCountDownTimer() {
        countDownTimer = object : CountDownTimer(10 * 60 * 1000L, 1000L) {
            override fun onFinish() {
                updateShowTime(0)
            }

            override fun onTick(millisUntilFinished: Long) {
                updateShowTime(millisUntilFinished)
            }

        }
        countDownTimer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    private fun updateShowTime(millisUntilFinished: Long) {
        resetTime(millisUntilFinished)
    }

    companion object {
        private const val HOUR = 1000 * 60 * 60
        private const val MIN = 1000 * 60
        private const val SEC = 1000
    }

    private fun resetTime(ms: Long) {
        if (ms < 0) {
            setTimes(0, 0, 0, 0)
            return
        }

        val hour: Int = (ms / HOUR).toInt()
        val min: Int = ((ms % HOUR) / MIN).toInt()
        val sec:Int = ((ms% MIN)/SEC).toInt()

        val minDecade = min / 10
        val minUnit = min - minDecade * 10

        val secDecade = sec / 10
        val secUnit = sec - secDecade * 10

        setTimes( minDecade, minUnit,secDecade,secUnit)
    }

    private fun setTimes(minDecade: Int, minUnit: Int, secDecade: Int, secUnit: Int) {
        tvMinDecade.text = minDecade.toString()
        tvMinUnit.text = minUnit.toString()
        tvSecDecade.text = secDecade.toString()
        tvSecUnit.text = secUnit.toString()
    }

    /**
     * 奖品列表
     */
    private fun initPrize() {
        rvShowPrize.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        prizeList.add("礼物道具")
        prizeList.add("现金")
        prizeList.add("优惠券")
        prizeList.add("礼物道具")
        val prizeAdapter = PrizeAdapter(R.layout.item_red_packet_info_prize, prizeList)
        rvShowPrize.adapter = prizeAdapter

        val layoutParams = rvShowPrize.layoutParams as RelativeLayout.LayoutParams
        if (prizeList.size <= 3) {
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        }
        rvShowPrize.layoutParams = layoutParams
    }

    override fun onResume() {
        super.onResume()
        val density = context?.resources?.displayMetrics?.density
        val w = 305 * density!!.toInt()
        val h = 460 * density.toInt()
        dialog?.window?.setLayout(w, h)
    }

    private var listener: (() -> Unit)? = null
    fun setOnDialogPositiveListener(l: (() -> Unit)?) {
        listener = l
    }

    class PrizeAdapter(layoutResId: Int, data: MutableList<String>) : BaseQuickAdapter<String,
            BaseViewHolder>(layoutResId, data) {
        override fun convert(holder: BaseViewHolder, item: String) {
            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams?
            val density = holder.itemView.context?.resources?.displayMetrics?.density
            val leftMargin = 10 * density!!.toInt()
            layoutParams?.leftMargin = if (holder.adapterPosition != 0) leftMargin else 0
            holder.itemView.layoutParams = layoutParams
            holder.setText(R.id.tv_red_packet_info_prize, item)
        }

    }
}
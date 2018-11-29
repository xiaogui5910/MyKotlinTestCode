package com.example.chenchenggui.mykotlintestcode.redpacket

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.chenchenggui.mykotlintestcode.R

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/11/29
 */
class RedPacketDialog : DialogFragment() {
    private lateinit var rlRedPacket: RelativeLayout
    private lateinit var flOpening: FrameLayout
    private lateinit var llMoney: LinearLayout
    private lateinit var llShowMoney: LinearLayout
    private lateinit var llMoneyNum: LinearLayout
    private lateinit var tvThanks: TextView
    private lateinit var tvMoneyTip: TextView
    private lateinit var tvName: TextView
    private lateinit var tvCoupon: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true

        val openDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_red_packet, null)
        rlRedPacket = openDialogView.findViewById(R.id.rl_red_packet_open)
        flOpening = openDialogView.findViewById(R.id.fl_opening)
        llMoney = openDialogView.findViewById(R.id.ll_money)
        llShowMoney = openDialogView.findViewById(R.id.ll_show_money)
        llMoneyNum = openDialogView.findViewById(R.id.ll_money_num)
        tvThanks = openDialogView.findViewById(R.id.tv_thanks)
        tvMoneyTip = openDialogView.findViewById(R.id.tv_money_tip)
        tvName = openDialogView.findViewById(R.id.tv_name)
        tvCoupon = openDialogView.findViewById(R.id.tv_coupon)

        flOpening.setOnClickListener {
            //            openRedPacket()
            listener?.invoke()
        }
        return openDialogView
    }

    fun openRedPacket(redPacket: RedPacket) {
        when(redPacket.prizeType){
            RedPacket.PrizeType.MONEY->showMoneyRedPacket(redPacket)
            RedPacket.PrizeType.COUPON->showCouponRedPacket(redPacket)
            RedPacket.PrizeType.EMPTY->showEmptyRedPacket(redPacket)
        }
    }

    private fun showMoneyRedPacket(redPacket: RedPacket) {
        rlRedPacket.setBackgroundResource(R.drawable.rain_red_packet_opend)
        flOpening.visibility = View.GONE
        llMoney.visibility = View.VISIBLE

        tvName.text = redPacket.prizeName
    }
    private fun showCouponRedPacket(redPacket: RedPacket) {
        rlRedPacket.setBackgroundResource(R.drawable.rain_red_packet_opend)
        flOpening.visibility = View.GONE
        llMoney.visibility = View.VISIBLE
        tvName.text = redPacket.prizeName

        llMoneyNum.visibility=View.GONE
        tvCoupon.visibility=View.VISIBLE

    }

    private fun showEmptyRedPacket(redPacket: RedPacket) {
        rlRedPacket.setBackgroundResource(R.drawable.rain_red_packet_opend)
        flOpening.visibility = View.GONE
        llMoney.visibility = View.VISIBLE
        llShowMoney.visibility = View.GONE
        tvThanks.visibility = View.VISIBLE
        tvMoneyTip.text="再接再厉"
        tvMoneyTip.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f)
    }


    override fun onResume() {
        super.onResume()
        val density = context?.resources?.displayMetrics?.density
        val w = 280 * density!!.toInt()
        val h = 360 * density.toInt()
        dialog?.window?.setLayout(w, h)
    }

    private var listener: (() -> Unit)? = null
    fun setOnDialogPositiveListener(l: (() -> Unit)?) {
        listener = l
    }
}
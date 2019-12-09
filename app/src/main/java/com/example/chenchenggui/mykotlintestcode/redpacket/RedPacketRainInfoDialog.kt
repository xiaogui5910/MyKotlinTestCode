package com.example.chenchenggui.mykotlintestcode.redpacket

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.chenchenggui.mykotlintestcode.R
import kotlinx.android.synthetic.main.dialog_red_packet_rain_info.*

/**
 * description ：
 * author : chenchenggui
 * creation date: 2018/11/29
 */
class RedPacketRainInfoDialog : DialogFragment() {
    private lateinit var rvShowPrize: RecyclerView
    private lateinit var ivClose: ImageView
    private val prizeList: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true

        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_red_packet_rain_info, null)
        rvShowPrize = rootView.findViewById(R.id.rv_show_prize)
        ivClose = rootView.findViewById(R.id.iv_red_packet_info_close)
        ivClose.setOnClickListener { dismiss() }
        initPrize()
        return rootView
    }

    private fun initPrize() {
        rvShowPrize.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        prizeList.add("礼物道具")
        prizeList.add("现金")
        prizeList.add("优惠券")
        prizeList.add("礼物道具")
        val prizeAdapter = PrizeAdapter(R.layout.item_red_packet_info_prize, prizeList)
        rvShowPrize.adapter = prizeAdapter

        val layoutParams = rvShowPrize.layoutParams as RelativeLayout.LayoutParams
        if (prizeList.size<=3){
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        }else{
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

    class PrizeAdapter(layoutResId: Int, data: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
            val layoutParams = helper?.itemView?.layoutParams as ViewGroup.MarginLayoutParams?
            val density = helper?.itemView?.context?.resources?.displayMetrics?.density
            val leftMargin = 10 * density!!.toInt()
            layoutParams?.leftMargin = if (helper?.adapterPosition != 0) leftMargin else 0
            helper?.itemView?.layoutParams = layoutParams
            helper?.setText(R.id.tv_red_packet_info_prize, item)
        }

    }
}
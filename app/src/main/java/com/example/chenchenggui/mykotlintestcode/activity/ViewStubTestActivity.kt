package com.example.chenchenggui.mykotlintestcode.activity

import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.toast
import kotlinx.android.synthetic.main.activity_view_stub_test.*
import java.util.*

class ViewStubTestActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_view_stub_test
    var layoutView: View? = null
    var viewStubById: ViewStub? = null
    override fun initView() {
        //1. 演示viewStub.setVisibility(View.GONE)
        viewStubById = findViewById<ViewStub>(R.id.viewStub)
        // 设置加载监听回调，成功加载后回调[只会回调一次]
        viewStub.setOnInflateListener { stub, inflated ->
            toast("ViewStub 加载被我监听到了")
        }


        btn_show.setOnClickListener {
            //inflate 方法只能被调用一次，因为调用后viewStub对象就被移除了视图树；
            // 所以，如果此时再次点击显示按钮，就会崩溃，错误信息：ViewStub must have a non-null ViewGroup viewParent；
            // 所以使用try catch ,当此处发现exception 的时候，在catch中使用setVisibility()重新显示
//            layoutView = viewStub.inflate()
            try {
                layoutView = viewStubById?.inflate()
            } catch (e: Exception) {
                viewStubById?.visibility = View.VISIBLE
            }
        }
        btn_hide.setOnClickListener {
            //kotlin中如果先show再hide使用id操作会报错，因为viewStub后面不存在了,可以用步骤1中的对象操作
//            viewStub.visibility = View.GONE
            viewStubById?.visibility = View.GONE
        }
        btn_change.setOnClickListener {
            val ivGirl = layoutView?.findViewById<ImageView>(R.id.iv_viewStub_girl)
            val tvDesc = layoutView?.findViewById<TextView>(R.id.tv_viewStub_desc)
            val num=Random().nextInt(10)
            when(num/4){
                0->{
                    ivGirl?.setImageResource(R.drawable.img_viewstub_test2)
                    tvDesc?.text = "猜不透你哦~"
                }
                1->{
                    ivGirl?.setImageResource(R.drawable.img_viewstub_test3)
                    tvDesc?.text = "秀儿请坐下~"
                }
                2->{
                    ivGirl?.setImageResource(R.drawable.img_viewstub_test4)
                    tvDesc?.text = "来看美女的~"
                }
                3->{
                    ivGirl?.setImageResource(R.drawable.img_viewstub_test1)
                    tvDesc?.text = "不进来坐坐~"
                }
            }
        }
    }

}

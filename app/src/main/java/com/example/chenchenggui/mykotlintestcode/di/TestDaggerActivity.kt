package com.example.chenchenggui.mykotlintestcode.di

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.activity.BaseActivity
import com.example.chenchenggui.mykotlintestcode.di.testinjectcomponent.DaggerTestInjectComponent
import com.example.chenchenggui.mykotlintestcode.di.testinjectcomponent.TestInjectBean
import com.example.chenchenggui.mykotlintestcode.di.testinjectcomponent.testjavacode.DaggerTestDiComponent
import com.example.chenchenggui.mykotlintestcode.di.testmoduleprovide.DaggerTestDiModuleComponent
import com.example.chenchenggui.mykotlintestcode.di.testmoduleprovide.TestDiAdapter
import com.example.chenchenggui.mykotlintestcode.di.testmoduleprovide.TestDiModuleBean
import kotlinx.android.synthetic.main.activity_test_dagger.*
import javax.inject.Inject

class TestDaggerActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_test_dagger
//
//    @Inject
//    lateinit var testInjectBean: TestInjectBean

    //    @Inject
//    fun setTestBean(testInjectBean: TestInjectBean) {
//        this.testInjectBean = testInjectBean
//    }
    @Inject
    lateinit var testDiAdapter: TestDiAdapter

    override fun initData() {
        super.initData()
        val testInjectBean = DaggerTestInjectComponent.builder().build().showTestInjectBean()
        tv_test_di_inject.text = testInjectBean.toString()

        val showTestDiBean = DaggerTestDiComponent.builder().build().showTestDiBean()
        tv_test_di.text = showTestDiBean.toString()

//        val dataList = ArrayList<TestDiModuleBean>()
//        for (i in 1..9) {
//            val item = TestDiModuleBean().apply {
//                className = "高一($i)班"
//                teacher = "李$i"
//                studentNum = i * 10
//            }
//            dataList.add(item)
//        }
        DaggerTestDiModuleComponent.builder().build().inject(this)
        rv_test_di.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        val testDiAdapter = TestDiAdapter(R.layout.item_test_di, dataList)
        rv_test_di.adapter = testDiAdapter
        rv_test_di.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}

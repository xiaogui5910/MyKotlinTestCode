package com.example.chenchenggui.mykotlintestcode.di.testmoduleprovide

import com.example.chenchenggui.mykotlintestcode.R
import com.example.chenchenggui.mykotlintestcode.di.testcustom.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Inject

/**
 * description ：
 * author : chenchenggui
 * creation date: 2020-01-14
 */
@Module
class TestDiModule {

    @Provides
    fun provideTestDiAdapter(layoutResId: Int, dataList: ArrayList<TestDiModuleBean>):
            TestDiAdapter {
        return TestDiAdapter(layoutResId, dataList)
    }

    @Provides
    fun provideLayoutResId(): Int {
        return R.layout.item_test_di
    }

    @Provides
    fun provideDataList(): ArrayList<TestDiModuleBean> {
        val dataList = ArrayList<TestDiModuleBean>()
        for (i in 1..9) {
            val item = TestDiModuleBean().apply {
                className = "高一($i)班"
                teacher = "李$i"
                studentNum = i * 10
            }
            dataList.add(item)
        }
        return dataList
    }
}
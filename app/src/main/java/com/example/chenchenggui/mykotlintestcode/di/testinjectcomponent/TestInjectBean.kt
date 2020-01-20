package com.example.chenchenggui.mykotlintestcode.di.testinjectcomponent

import android.util.Log
import javax.inject.Inject

/**
 * description ：测试单个@Inject和@Component组合注入
 * author : chenchenggui
 * creation date: 2020-01-13
 */
class TestInjectBean constructor(val desc: String) {
    @Inject
    constructor() : this("constructor Test @Inject desc")

    init {
        Log.e("TestInjectBean", "init TestInjectBean")
    }

    @Inject
    fun showDesc() {
        Log.e("TestInjectBean", "show TestInjectBean desc")
    }

    override fun toString(): String {
        Log.e("TestInjectBean", desc)
        return "TestInjectBean(desc=$desc)"
    }
}
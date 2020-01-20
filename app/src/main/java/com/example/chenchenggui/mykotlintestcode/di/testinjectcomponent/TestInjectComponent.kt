package com.example.chenchenggui.mykotlintestcode.di.testinjectcomponent

import com.example.chenchenggui.mykotlintestcode.di.TestDaggerActivity
import dagger.Component

/**
 * description ：
 * author : chenchenggui
 * creation date: 2020-01-13
 */
@Component
interface TestInjectComponent {
    fun showTestInjectBean(): TestInjectBean
    //不用，因为和TestDiModuleComponent冲突，没有提供provide方法或者TestDiAdapter构造没有@Inject，所以改用showTestInjectBean()
//    fun inject(activity: TestDaggerActivity)
}
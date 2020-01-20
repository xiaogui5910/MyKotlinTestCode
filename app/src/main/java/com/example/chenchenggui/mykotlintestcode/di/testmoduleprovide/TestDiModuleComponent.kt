package com.example.chenchenggui.mykotlintestcode.di.testmoduleprovide

import com.example.chenchenggui.mykotlintestcode.di.TestDaggerActivity
import com.example.chenchenggui.mykotlintestcode.di.testcustom.ActivityScope
import dagger.Component

/**
 * description ：
 * author : chenchenggui
 * creation date: 2020-01-14
 */
@Component(modules = [TestDiModule::class])
interface TestDiModuleComponent {
    fun inject(testDaggerActivity: TestDaggerActivity)
}
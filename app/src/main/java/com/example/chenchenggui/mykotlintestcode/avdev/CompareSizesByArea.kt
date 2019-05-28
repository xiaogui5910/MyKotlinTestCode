package com.example.chenchenggui.mykotlintestcode.avdev

import android.util.Size
import java.lang.Long.signum

/**
 * description ：Compares two `Size`s based on their areas.

 * author : chenchenggui
 * creation date: 2019/5/28
 */
internal class CompareSizesByArea :Comparator<Size>{
    override fun compare(lhs: Size, rhs: Size) =
        signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
}
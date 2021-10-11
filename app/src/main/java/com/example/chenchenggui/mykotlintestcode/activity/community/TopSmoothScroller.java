package com.example.chenchenggui.mykotlintestcode.activity.community;

import android.content.Context;

import androidx.recyclerview.widget.LinearSmoothScroller;

/**
 * @author : chenchenggui
 * @description ：
 * @date: 7/15/21
 */
public class TopSmoothScroller extends LinearSmoothScroller {
    TopSmoothScroller(Context context) {
        super(context);
    }
    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_START;
    }
    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }
}
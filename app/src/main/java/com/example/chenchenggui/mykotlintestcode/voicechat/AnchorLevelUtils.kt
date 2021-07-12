package com.example.chenchenggui.mykotlintestcode.voicechat

import android.content.Context

/**
 * @description ：主播等级工具类
 * @author : chenchenggui
 * @date: 6/29/21
 */
class AnchorLevelUtils {
    companion object {
        /**
         * 根据图片资源id名称获取到资源id
         */
        fun getDrawableId(context: Context, drawableResIdName: String): Int {
            return context.resources.getIdentifier(drawableResIdName, "drawable", context.packageName)
        }

        /**
         * 根据主播等级,获取对应等级图片资源id
         */
        fun getAnchorLevelDrawable(context: Context, anchorLevel: Int): Int {
            val name = "icon_anchor_level_$anchorLevel"
            var drawableId = getDrawableId(context, name)
            if (drawableId == 0) {
                drawableId = getDrawableId(context, "icon_anchor_level_1")
            }
            return drawableId
        }

        /**
         * 根据主播等级,获取对应等级文字图片资源id
         */
        fun getAnchorLevelTextDrawable(context: Context, anchorLevel: Int): Int {
            val name = "icon_anchor_level_text_$anchorLevel"
            var drawableId = getDrawableId(context, name)
            if (drawableId == 0) {
                drawableId = getDrawableId(context, "icon_anchor_level_text_1")
            }
            return drawableId
        }

        /**
         * 根据主播等级,获取对应等级背景图片资源id
         */
        fun getAnchorLevelBgDrawable(context: Context, anchorLevel: Int): Int {
            val name = "icon_anchor_level_bg_${anchorLevel/10}"
            var drawableId = getDrawableId(context, name)
            if (drawableId == 0) {
                drawableId = getDrawableId(context, "icon_anchor_level_bg_0")
            }
            return drawableId
        }
    }
}
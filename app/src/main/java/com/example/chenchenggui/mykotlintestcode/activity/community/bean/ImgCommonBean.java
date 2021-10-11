package com.example.chenchenggui.mykotlintestcode.activity.community.bean;

import java.io.Serializable;

public class ImgCommonBean implements Serializable {
    /**
     * 图片类型 0:静态图片 1:动态图片gif
     */
    public static final int IMG_TYPE_PIC = 0;
    public static final int IMG_TYPE_GIF = 1;
    public int height;
    public String imgurl;
    public int width;
    public String desc;
    public String title;
    public int imgType;
}

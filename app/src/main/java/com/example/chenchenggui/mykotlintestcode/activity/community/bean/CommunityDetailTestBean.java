package com.example.chenchenggui.mykotlintestcode.activity.community.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : chenchenggui
 * @description ：
 * @date: 7/14/21
 */
public class CommunityDetailTestBean implements Serializable {
    private String title;
    private CommunityDetailOwnerBen owner;
    private List<String> textList;
    private List<ImgCommonBean> imgList;
    private boolean isManager;
    private boolean like;
    private int replyCount;

    public boolean isLike() {
        return like;
    }

    public CommunityDetailOwnerBen getOwner() {
        return owner;
    }

    public void setOwner(CommunityDetailOwnerBen owner) {
        this.owner = owner;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTextList() {
        return textList;
    }

    public List<ImgCommonBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgCommonBean> imgList) {
        this.imgList = imgList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

}

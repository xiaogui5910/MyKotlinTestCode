package com.example.chenchenggui.mykotlintestcode.activity.community.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author : chenchenggui
 * @description ：
 * @date: 7/14/21
 */
public class CommunityDetailBean implements Serializable, MultiItemEntity {
    /**
     * 社区详情页item类型 1:标题 2:发布者信息 3:文字内容 4:图片 5:管理员操作选项 6:回复header 7:回复内容 8:空数据
     */
    public static final int ITEM_TYPE_TITLE = 1;
    public static final int ITEM_TYPE_OWNER = 2;
    public static final int ITEM_TYPE_TEXT = 3;
    public static final int ITEM_TYPE_IMG = 4;
    public static final int ITEM_TYPE_MANAGER_OPERATION = 5;
    public static final int ITEM_TYPE_REPLY_HEADER = 6;
    public static final int ITEM_TYPE_REPLY_CONTENT = 7;
    public static final int ITEM_TYPE_REPLY_EMPTY = 8;
    /**
     * 图片类型 0:静态图片 1:动态图片gif
     */
    public static final int IMG_TYPE_PIC = 0;
    public static final int IMG_TYPE_GIF = 1;
    /**
     * 社区详情页item类型 1:标题 2:发布者信息 3:文字内容 4:图片 5:管理员操作选项 6:回复header 7:回复内容 8:空数据
     */
    private int itemViewType;

    private String title;
    private String text;
    private String img;
    private String avatar;
    private CommunityDetailOwnerBen owner;
    private CommunityDetailReplyTestBean replyTestBean;
    private List<String> textList;
    private List<String> imgList;
    private boolean isManager;
    private boolean like;
    /**
     * 图片类型 0:静态图片 1:动态图片gif
     */
    private int imgType;
    private int replyCount;

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public CommunityDetailReplyTestBean getReplyTestBean() {
        return replyTestBean;
    }

    public void setReplyTestBean(CommunityDetailReplyTestBean replyTestBean) {
        this.replyTestBean = replyTestBean;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public boolean isLike() {
        return like;
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

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public CommunityDetailOwnerBen getOwner() {
        return owner;
    }

    public void setOwner(CommunityDetailOwnerBen owner) {
        this.owner = owner;
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int getItemType() {
        return itemViewType;
    }
}

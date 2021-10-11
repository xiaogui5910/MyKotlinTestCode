package com.example.chenchenggui.mykotlintestcode.activity.community.bean;

import java.io.Serializable;

/**
 * @author : chenchenggui
 * @description ：
 * @date: 7/14/21
 */
public class CommunityDetailReplyTestBean implements Serializable {
    private String nickname;
    private String avatar;
    private String time;
    private String replyContent;
    private boolean showTag;
    private boolean like;
    private int replyCount;

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isShowTag() {
        return showTag;
    }

    public void setShowTag(boolean showTag) {
        this.showTag = showTag;
    }

}

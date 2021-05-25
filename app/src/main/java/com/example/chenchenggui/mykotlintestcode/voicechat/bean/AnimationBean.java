package com.example.chenchenggui.mykotlintestcode.voicechat.bean;

/**
 * @author : chenchenggui
 * @description ：动画信息
 * @date: 5/21/21
 */
public class AnimationBean {
    private int type;
    private String name;
    private String iconUrl;
    private boolean hasUse;
    private boolean hasGift;
    private int position;
    private boolean micClose;
    private boolean lock;
    private boolean showPlay;

    public boolean isShowPlay() {
        return showPlay;
    }

    public void setShowPlay(boolean showPlay) {
        this.showPlay = showPlay;
    }

    public boolean isHasGift() {
        return hasGift;
    }

    public void setHasGift(boolean hasGift) {
        this.hasGift = hasGift;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isMicClose() {
        return micClose;
    }

    public void setMicClose(boolean micClose) {
        this.micClose = micClose;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isHasUse() {
        return hasUse;
    }

    public void setHasUse(boolean hasUse) {
        this.hasUse = hasUse;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

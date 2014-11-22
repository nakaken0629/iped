package com.iped_system.iped.common.main;

import java.util.Date;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalkValue {
    private long faceId;
    private String youText;
    private String youPictogramKey;
    private String authorName;
    private String meText;
    private String mePictogramKey;
    private Date createdAt;

    public long getFaceId() {
        return faceId;
    }

    public void setFaceId(long faceId) {
        this.faceId = faceId;
    }

    public String getYouText() {
        return youText;
    }

    public void setYouText(String youText) {
        this.youText = youText;
    }

    public String getYouPictogramKey() {
        return youPictogramKey;
    }

    public void setYouPictogramKey(String youPictogramKey) {
        this.youPictogramKey = youPictogramKey;
    }

    public String getAuthorName() {

        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getMeText() {

        return meText;
    }

    public void setMeText(String meText) {
        this.meText = meText;
    }

    public String getMePictogramKey() {
        return mePictogramKey;
    }

    public void setMePictogramKey(String mePictogramKey) {
        this.mePictogramKey = mePictogramKey;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

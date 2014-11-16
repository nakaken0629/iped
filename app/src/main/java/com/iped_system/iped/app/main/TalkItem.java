package com.iped_system.iped.app.main;

import java.util.Date;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalkItem {
    private long faceId;
    private String youText;
    private String authorName;
    private String meText;
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

    public void setYouText(String text) {
        this.youText = text;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

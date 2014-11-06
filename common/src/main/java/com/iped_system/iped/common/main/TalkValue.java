package com.iped_system.iped.common.main;

import java.util.Date;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalkValue {
    private String faceKey;
    private String youText;
    private String authorName;
    private String meText;
    private Date createdAt;

    public String getFaceKey() {
        return faceKey;
    }

    public void setFaceKey(String faceKey) {
        this.faceKey = faceKey;
    }

    public String getYouText() {
        return youText;
    }

    public void setYouText(String youText) {
        this.youText = youText;
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

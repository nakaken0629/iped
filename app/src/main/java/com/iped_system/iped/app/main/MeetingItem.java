package com.iped_system.iped.app.main;

import java.util.Date;
import java.util.List;

/**
 * Created by kenji on 2014/08/09.
 */
public class MeetingItem {
    private String faceKey;
    private String authorName;
    private Date createdAt;
    private String text;
    private List<String> pictureKeys;

    public String getFaceKey() {
        return faceKey;
    }

    public void setFaceKey(String faceKey) {
        this.faceKey = faceKey;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getPictureKeys() {
        return pictureKeys;
    }

    public void setPictureKeys(List<String> pictureKeys) {
        this.pictureKeys = pictureKeys;
    }
}

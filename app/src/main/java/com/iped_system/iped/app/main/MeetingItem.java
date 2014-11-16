package com.iped_system.iped.app.main;

import java.util.Date;
import java.util.List;

/**
 * Created by kenji on 2014/08/09.
 */
public class MeetingItem {
    private long faceId;
    private String authorName;
    private Date createdAt;
    private String text;
    private List<Long> pictureIdList;

    public long getFaceId() {
        return faceId;
    }

    public void setFaceId(long faceId) {
        this.faceId = faceId;
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

    public List<Long> getPictureIdList() {
        return pictureIdList;
    }

    public void setPictureIdList(List<Long> pictureIdList) {
        this.pictureIdList = pictureIdList;
    }
}

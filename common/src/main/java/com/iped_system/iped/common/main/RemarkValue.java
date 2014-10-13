package com.iped_system.iped.common.main;

import java.util.Date;
import java.util.List;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarkValue {
    private long id;
    private String faceKey;
    private String authorName;
    private Date createdAt;
    private String text;
    private List<String> pictures;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getFaceKey() {
        return faceKey;
    }

    public void setFaceKey(String faceKey) {
        this.faceKey = faceKey;
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

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
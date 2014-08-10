package com.iped_system.iped.server.backend.model;

import java.util.Date;

/**
 * Created by kenji on 2014/08/10.
 */
public class Remark {
    private String patientId;
    private String authorName;
    private Date createdAt;
    private String text;

    public String getPatientId() {

        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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
}

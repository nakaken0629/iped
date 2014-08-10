package com.iped_system.iped.server.backend.model;

/**
 * Created by kenji on 2014/08/10.
 */
public class Remark {
    private String authorName;
    private String patientId;
    private String text;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPatientId() {

        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

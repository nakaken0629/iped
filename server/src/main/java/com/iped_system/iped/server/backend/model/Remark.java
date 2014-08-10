package com.iped_system.iped.server.backend.model;

/**
 * Created by kenji on 2014/08/10.
 */
public class Remark {
    private String authorId;
    private String patientId;
    private String text;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
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

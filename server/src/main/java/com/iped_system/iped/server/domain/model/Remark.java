package com.iped_system.iped.server.domain.model;

import com.google.appengine.api.datastore.Entity;

import java.util.Date;
import java.util.List;

/**
 * Created by kenji on 2014/08/10.
 */
public class Remark extends EntityWrapper {
    @EntityProperty private String userId;
    @EntityProperty private String patientId;
    @EntityProperty private Date createdAt;
    @EntityProperty private String text;
    @EntityProperty private List<Long> pictureIdList;

    public Remark() {
        super();
    }

    public Remark(Entity entity) {
        super(entity);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPatientId() {

        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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

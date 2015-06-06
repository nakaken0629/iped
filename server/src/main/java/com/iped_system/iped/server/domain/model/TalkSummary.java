package com.iped_system.iped.server.domain.model;

import com.google.appengine.api.datastore.Entity;

import java.util.Date;

/**
 * Created by kenji on 15/06/06.
 */
public class TalkSummary extends EntityWrapper {
    @EntityProperty private String userId;
    @EntityProperty private Date talkDate;
    @EntityProperty private long talkCount;
    @EntityProperty private long pictureCount;
    @EntityProperty private long pictogramCount;

    public TalkSummary() {
        super();
    }

    public TalkSummary(Entity entity) {
        super(entity);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTalkDate() {
        return talkDate;
    }

    public void setTalkDate(Date talkDate) {
        this.talkDate = talkDate;
    }

    public long getTalkCount() {
        return talkCount;
    }

    public void setTalkCount(long talkCount) {
        this.talkCount = talkCount;
    }

    public long getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(long pictureCount) {
        this.pictureCount = pictureCount;
    }

    public long getPictogramCount() {
        return pictogramCount;
    }

    public void setPictogramCount(long pictogramCount) {
        this.pictogramCount = pictogramCount;
    }

    public void incrementTalkCount() {
        this.talkCount++;
    }

    public void incrementPictogram() {
        this.pictogramCount++;
    }

    public void incrementPicture() {
        this.pictureCount++;
    }
}

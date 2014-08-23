package com.iped_system.iped.common;

import net.arnx.jsonic.JSONHint;

import java.util.Date;
import java.util.List;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksNewRequest extends BaseRequest {
    private Date lastUpdate;
    private String authorName;
    private String text;
    private List<String> pictures;

    @Override
    @JSONHint(ignore=true)
    public Class<? extends BaseResponse> getResponseClass() {
        return RemarksNewResponse.class;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

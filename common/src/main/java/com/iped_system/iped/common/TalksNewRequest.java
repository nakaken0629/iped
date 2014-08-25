package com.iped_system.iped.common;

import java.util.Date;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalksNewRequest extends BaseRequest {
    private Date lastUpdate;
    private String text;

    @Override
    public Class<? extends BaseResponse> getResponseClass() {
        return TalksNewResponse.class;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

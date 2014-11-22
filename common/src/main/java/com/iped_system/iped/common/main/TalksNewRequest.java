package com.iped_system.iped.common.main;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;

import net.arnx.jsonic.JSONHint;

import java.util.Date;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalksNewRequest extends BaseRequest {
    private Date lastUpdate;
    private String text;
    private String pictogramKey;

    @Override
    @JSONHint(ignore=true)
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

    public String getPictogramKey() {
        return pictogramKey;
    }

    public void setPictogramKey(String pictogramKey) {
        this.pictogramKey = pictogramKey;
    }
}

package com.iped_system.iped.common;

import net.arnx.jsonic.JSONHint;

import java.util.Date;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalksRequest extends BaseRequest {
    private Date lastUpdate;

    @Override
    @JSONHint(ignore=true)
    public Class<? extends BaseResponse> getResponseClass() {
        return TalksResponse.class;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

package com.iped_system.iped.common.main;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;

import net.arnx.jsonic.JSONHint;

import java.util.Date;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksRequest extends BaseRequest {
    private Date lastUpdate;

    @Override
    @JSONHint(ignore=true)
    public Class<? extends BaseResponse> getResponseClass() {
        return RemarksResponse.class;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

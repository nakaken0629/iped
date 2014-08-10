package com.iped_system.iped.common;

import net.arnx.jsonic.JSONHint;

/**
 * Created by kenji on 2014/08/09.
 */
public class GetRemarksRequest extends BaseRequest {
    private long remarkId;

    @Override
    @JSONHint(ignore=true)
    public Class<? extends BaseResponse> getResponseClass() {
        return GetRemarksResponse.class;
    }

    public long getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(long remarkId) {
        this.remarkId = remarkId;
    }
}

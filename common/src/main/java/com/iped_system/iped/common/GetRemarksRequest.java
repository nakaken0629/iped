package com.iped_system.iped.common;

/**
 * Created by kenji on 2014/08/09.
 */
public class GetRemarksRequest extends BaseRequest {
    private long remarkId;

    @Override
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

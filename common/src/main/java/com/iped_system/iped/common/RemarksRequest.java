package com.iped_system.iped.common;

import net.arnx.jsonic.JSONHint;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksRequest extends BaseRequest {
    @Override
    @JSONHint(ignore=true)
    public Class<? extends BaseResponse> getResponseClass() {
        return RemarksResponse.class;
    }
}

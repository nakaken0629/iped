package com.iped_system.iped.common.login;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;

import net.arnx.jsonic.JSONHint;

/**
 * Created by kenji on 2014/11/02.
 */
public class VersionRequest extends BaseRequest {
    @Override
    @JSONHint(ignore=true)
    public Class<? extends BaseResponse> getResponseClass() {
        return VersionResponse.class;
    }
}

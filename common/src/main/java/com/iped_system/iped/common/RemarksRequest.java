package com.iped_system.iped.common;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksRequest extends BaseRequest {
    @Override
    public Class<? extends BaseResponse> getResponseClass() {
        return RemarksResponse.class;
    }
}

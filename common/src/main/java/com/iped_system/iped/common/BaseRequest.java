package com.iped_system.iped.common;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONHint;

/**
 * Created by kenji on 2014/08/04.
 */
public abstract class BaseRequest {
    @JSONHint(ignore=true)
    public abstract Class<? extends BaseResponse> getResponseClass();

    public final String toJSON() {
        return JSON.encode(this);
    }
}

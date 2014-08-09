package com.iped_system.iped.common;

import net.arnx.jsonic.JSON;

/**
 * Created by kenji on 2014/08/04.
 */
public abstract class BaseRequest {
    public abstract Class<? extends BaseResponse> getResponseClass();

    public final String toJSON() {
        return JSON.encode(this);
    }
}

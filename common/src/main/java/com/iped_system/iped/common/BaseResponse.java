package com.iped_system.iped.common;

import net.arnx.jsonic.JSON;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by kenji on 2014/08/04.
 */
public abstract class BaseResponse {
    private ResponseStatus status = ResponseStatus.SUCCESS;

    public final ResponseStatus getStatus() {
        return status;
    }

    public final void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public final String toJSON() {
        return JSON.encode(this);
    }

    public static final BaseResponse fromJSON(Reader reader, Class<? extends BaseResponse> clazz) {
        try {
            return JSON.decode(reader, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

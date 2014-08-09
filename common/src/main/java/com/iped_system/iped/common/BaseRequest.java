package com.iped_system.iped.common;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONHint;

/**
 * Created by kenji on 2014/08/04.
 */
public abstract class BaseRequest {
    private long tokenId;

    @JSONHint(ignore=true)
    public long getTokenId() {
        return tokenId;
    }

    @JSONHint(ignore=true)
    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String toJSON() {
        return JSON.encode(this);
    }
}

package com.iped_system.iped.common.login;

import com.iped_system.iped.common.BaseResponse;

/**
 * Created by kenji on 2014/11/02.
 */
public class VersionResponse extends BaseResponse {
    private int versionCode;
    private String url;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

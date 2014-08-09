package com.iped_system.iped.common;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksRequest extends BaseRequest {
    private String text;

    @Override
    public Class<? extends BaseResponse> getResponseClass() {
        return RemarksResponse.class;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

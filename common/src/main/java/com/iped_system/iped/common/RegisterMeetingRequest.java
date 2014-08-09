package com.iped_system.iped.common;

/**
 * Created by kenji on 2014/08/09.
 */
public class RegisterMeetingRequest extends BaseRequest {
    private String text;

    @Override
    public Class<? extends BaseResponse> getResponseClass() {
        return RegisterMeetingResponse.class;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

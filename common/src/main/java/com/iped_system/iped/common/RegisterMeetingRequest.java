package com.iped_system.iped.common;

/**
 * Created by kenji on 2014/08/09.
 */
public class RegisterMeetingRequest extends BaseRequest {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

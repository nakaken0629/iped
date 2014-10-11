package com.iped_system.iped.common.main;

import com.iped_system.iped.common.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalksNewResponse extends BaseResponse {
    private List<TalkValue> talkValues = new ArrayList<TalkValue>();

    public List<TalkValue> getTalkValues() {
        return talkValues;
    }

    public void setTalkValues(List<TalkValue> talkValues) {
        this.talkValues = talkValues;
    }
}

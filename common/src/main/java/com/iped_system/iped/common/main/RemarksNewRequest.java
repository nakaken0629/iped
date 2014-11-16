package com.iped_system.iped.common.main;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;

import net.arnx.jsonic.JSONHint;

import java.util.List;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksNewRequest extends BaseRequest {
    private String text;
    private List<Long> pictureIdList;

    @Override
    @JSONHint(ignore=true)
    public Class<? extends BaseResponse> getResponseClass() {
        return RemarksNewResponse.class;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Long> getPictureIdList() {
        return pictureIdList;
    }

    public void setPictureIdList(List<Long> pictureIdList) {
        this.pictureIdList = pictureIdList;
    }
}

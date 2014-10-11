package com.iped_system.iped.common.main;

import com.iped_system.iped.common.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksResponse extends BaseResponse {
    private List<RemarkValue> remarkValues = new ArrayList<RemarkValue>();

    public List<RemarkValue> getRemarkValues() {
        return remarkValues;
    }

    public void setRemarkValues(List<RemarkValue> remarkValues) {
        this.remarkValues = remarkValues;
    }
}

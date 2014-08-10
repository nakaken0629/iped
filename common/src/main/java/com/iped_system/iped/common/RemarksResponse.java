package com.iped_system.iped.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksResponse extends BaseResponse {
    private List<Remark> remarks = new ArrayList<Remark>();

    public List<Remark> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<Remark> remarks) {
        this.remarks = remarks;
    }
}

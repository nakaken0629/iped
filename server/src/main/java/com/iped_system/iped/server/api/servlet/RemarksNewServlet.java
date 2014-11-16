package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.main.RemarksNewRequest;
import com.iped_system.iped.common.main.RemarksNewResponse;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.RemarkDomain;
import com.iped_system.iped.server.domain.model.Remark;

import java.util.Date;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksNewServlet extends BaseServlet {

    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return RemarksNewRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        AuthInfo userInfo = getAuthInfo();
        RemarksNewRequest request = (RemarksNewRequest) baseRequest;

        Remark remark = new Remark();
        remark.setUserId(userInfo.getUserId());
        remark.setPatientId(userInfo.getPatientId());
        remark.setText(request.getText());
        remark.setPictureIdList(request.getPictureIdList());
        remark.setCreatedAt(new Date());
        RemarkDomain domain = RemarkDomain.getInstance();
        domain.insert(remark);

        RemarksNewResponse response = new RemarksNewResponse();
        return response;
    }
}

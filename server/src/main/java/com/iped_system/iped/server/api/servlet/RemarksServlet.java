package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.main.RemarkValue;
import com.iped_system.iped.common.main.RemarksRequest;
import com.iped_system.iped.common.main.RemarksResponse;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.RemarkDomain;

import java.util.Date;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksServlet extends BaseServlet {
    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return RemarksRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        AuthInfo authInfo = getAuthInfo();
        String patientId = authInfo.getPatientId();
        RemarksRequest request = (RemarksRequest) baseRequest;
        Date lastUpdate = request.getLastUpdate();

        RemarksResponse response = new RemarksResponse();
        RemarkDomain remarkDomain = RemarkDomain.getInstance();
        for(RemarkValue remarkValue : remarkDomain.search(patientId, lastUpdate)) {
            response.getRemarkValues().add(remarkValue);
        }

        return response;
    }
}

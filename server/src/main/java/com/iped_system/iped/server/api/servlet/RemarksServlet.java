package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.Remark;
import com.iped_system.iped.common.RemarksRequest;
import com.iped_system.iped.common.RemarksResponse;
import com.iped_system.iped.server.api.domain.RemarkDomain;

import java.util.Date;
import java.util.Map;

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
        Map<String, Object> userValue = getCurrentUserValue();
        String patientId = (String) userValue.get("patientId");
        RemarksRequest request = (RemarksRequest) baseRequest;
        Date lastUpdate = request.getLastUpdate();

        RemarksResponse response = new RemarksResponse();
        RemarkDomain remarkDomain = RemarkDomain.getInstance();
        for(Remark remarkValue : remarkDomain.search(patientId, lastUpdate)) {
            response.getRemarks().add(remarkValue);
        }

        return response;
    }
}

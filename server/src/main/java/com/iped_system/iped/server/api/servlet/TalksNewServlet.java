package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.main.TalkValue;
import com.iped_system.iped.common.main.TalksNewRequest;
import com.iped_system.iped.common.main.TalksNewResponse;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.TalkDomain;

import java.util.List;
import java.util.Map;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalksNewServlet extends BaseServlet {
    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return TalksNewRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        TalksNewRequest request = (TalksNewRequest) baseRequest;
        AuthInfo authInfo = getAuthInfo();

        String userId = authInfo.getUserId();
        String patientId = authInfo.getPatientId();
        String text = request.getText();
        String pictogramKey = request.getPictogramKey();
        TalkDomain.getInstance().insert(userId, patientId, text, pictogramKey);

        TalksNewResponse response = new TalksNewResponse();
        return response;
    }
}

package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.main.TalkValue;
import com.iped_system.iped.common.main.TalksRequest;
import com.iped_system.iped.common.main.TalksResponse;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.TalkDomain;

import java.util.List;
import java.util.Map;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalksServlet extends BaseServlet {
    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return TalksRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        TalksRequest request = (TalksRequest) baseRequest;
        AuthInfo authInfo = getAuthInfo();
        String userId = authInfo.getUserId();
        String patientId = authInfo.getPatientId();
        TalksResponse response = new TalksResponse();
        List<TalkValue> talkValues = TalkDomain.getInstance().search(userId, patientId, request.getLastUpdate());
        response.setTalkValues(talkValues);
        return response;
    }
}

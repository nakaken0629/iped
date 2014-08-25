package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.Talk;
import com.iped_system.iped.common.TalksRequest;
import com.iped_system.iped.common.TalksResponse;
import com.iped_system.iped.server.api.domain.TalkDomain;

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
        Map<String, Object> userValue = getCurrentUserValue();
        String userId = (String) userValue.get("userId");
        String patientId = (String) userValue.get("patientId");
        TalksResponse response = new TalksResponse();
        List<Talk> talks = TalkDomain.getInstance().search(userId, patientId, request.getLastUpdate());
        response.setTalks(talks);
        return response;
    }
}

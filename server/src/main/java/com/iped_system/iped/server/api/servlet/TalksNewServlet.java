package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.Talk;
import com.iped_system.iped.common.TalksNewRequest;
import com.iped_system.iped.common.TalksNewResponse;
import com.iped_system.iped.server.api.domain.TalkDomain;

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
        Map<String, Object> userValue = getCurrentUserValue();

        String userId = (String) userValue.get("userId");
        String patientId = (String) userValue.get("patientId");
        String text = request.getText();
        TalkDomain.getInstance().insert(userId, patientId, text);

        TalksNewResponse response = new TalksNewResponse();
        List<Talk> talks = TalkDomain.getInstance().search(userId, patientId, request.getLastUpdate());
        response.setTalks(talks);
        return response;
    }
}

package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.main.TalksNewRequest;
import com.iped_system.iped.common.main.TalksNewResponse;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.TalkDomain;
import com.iped_system.iped.server.domain.model.Talk;

import java.util.Date;

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

        Talk talk = new Talk();
        talk.setUserId(authInfo.getUserId());
        talk.setPatientId(authInfo.getPatientId());
        talk.setText(request.getText());
        talk.setPictogramKey(request.getPictogramKey());
        talk.setPictureId(request.getPictureId());
        talk.setCreatedAt(new Date());
        TalkDomain.getInstance().insert(talk);

        TalksNewResponse response = new TalksNewResponse();
        return response;
    }
}

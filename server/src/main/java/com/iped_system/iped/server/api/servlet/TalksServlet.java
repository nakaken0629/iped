package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.main.TalkValue;
import com.iped_system.iped.common.main.TalksRequest;
import com.iped_system.iped.common.main.TalksResponse;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.TalkDomain;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.Talk;
import com.iped_system.iped.server.domain.model.User;

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
        TalkDomain talkDomain = TalkDomain.getInstance();
        UserDomain userDomain = UserDomain.getInstance();
        for(Talk talk : talkDomain.search(userId, patientId, request.getLastUpdate())) {
            TalkValue talkValue = new TalkValue();
            if (talk.getUserId().equals(userId)) {
                talkValue.setMeText(talk.getText());
            } else {
                User user = userDomain.getByUserId(talk.getUserId());
                talkValue.setFaceId(user.getFaceId());
                talkValue.setYouText(talk.getText());
                talkValue.setAuthorName(user.getName());
            }
            talkValue.setCreatedAt(talk.getCreatedAt());
            response.getTalkValues().add(talkValue);
        }
        return response;
    }
}

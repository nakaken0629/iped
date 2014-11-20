package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.main.RemarkValue;
import com.iped_system.iped.common.main.RemarksRequest;
import com.iped_system.iped.common.main.RemarksResponse;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.RemarkDomain;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.Remark;
import com.iped_system.iped.server.domain.model.User;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(RemarksServlet.class.getName());

    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return RemarksRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        AuthInfo authInfo = getAuthInfo();
        String patientId = authInfo.getPatientId();
        RemarksRequest request = (RemarksRequest) baseRequest;
        Date lastDate = request.getLastDate();

        RemarksResponse response = new RemarksResponse();
        RemarkDomain remarkDomain = RemarkDomain.getInstance();
        UserDomain userDomain = UserDomain.getInstance();
        for(Remark remark : remarkDomain.search(patientId, lastDate)) {
            User user = userDomain.getUser(remark.getUserId());
            RemarkValue value = new RemarkValue();
            value.setId(remark.getId());
            value.setFaceId(user.getFaceId());
            value.setAuthorName(user.getName());
            value.setCreatedAt(remark.getCreatedAt());
            value.setText(remark.getText());
            value.setPictureIdList(remark.getPictureIdList());
            response.getRemarkValues().add(value);
        }

        return response;
    }
}

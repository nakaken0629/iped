package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.login.LoginRequest;
import com.iped_system.iped.common.login.LoginResponse;
import com.iped_system.iped.common.ResponseStatus;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.User;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by kenji on 2014/08/04.
 */
public class LoginServlet extends BaseServlet {

    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return LoginRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        LoginRequest request = (LoginRequest) baseRequest;
        String userId = request.getUserId();
        String password = request.getPassword();
        UserDomain.LoginResult result = UserDomain.getInstance().login(userId, password);

        LoginResponse response = new LoginResponse();
        if (result != null) {
            response.setTokenId(result.getTokenId());
            User user = result.getUser();
            response.setUserId(userId);
            response.setLastName(user.getLastName());
            response.setFirstName(user.getFirstName());
            response.setRole(user.getRole());
            /* TODO: set selected patient id */
            response.setPatientId(user.getPatientIdList().get(0));
        } else {
            response.setStatus(ResponseStatus.FAIL);
        }
        return response;
    }
}

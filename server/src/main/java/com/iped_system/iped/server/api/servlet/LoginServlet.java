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
        User user = UserDomain.getInstance().getByUserId(userId);

        LoginResponse response = new LoginResponse();
        if (user != null && password.equals(user.getPassword())) {
            Entity token = new Entity("Token");
            token.setProperty("userId", userId);
            Calendar calendar = Calendar.getInstance();
            token.setProperty("refreshDate", calendar.getTime());
            DatastoreService service = DatastoreServiceFactory.getDatastoreService();
            service.put(token);

            response.setTokenId(token.getKey().getId());
            response.setUserId(userId);
            response.setLastName((String) user.getLastName());
            response.setFirstName((String) user.getFirstName());
            response.setRole((String) user.getRole());
            response.setPatientId((String) user.getPatientId());
        } else {
            response.setStatus(ResponseStatus.FAIL);
        }
        return response;
    }
}

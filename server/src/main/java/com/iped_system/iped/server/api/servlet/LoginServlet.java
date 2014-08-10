package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.LoginRequest;
import com.iped_system.iped.common.LoginResponse;
import com.iped_system.iped.common.ResponseStatus;
import com.iped_system.iped.server.api.domain.UserDomain;

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
        Map<String,Object> userValue = UserDomain.getInstance().getByUserId(userId);

        LoginResponse response = new LoginResponse();
        if (userValue != null && password.equals(userValue.get("password"))) {
            Entity token = new Entity("token");
            token.setProperty("userId", userId);
            Calendar calendar = Calendar.getInstance();
            token.setProperty("refreshDate", calendar.getTime());
            DatastoreService service = DatastoreServiceFactory.getDatastoreService();
            service.put(token);

            response.setTokenId(token.getKey().getId());
            response.setUserId(userId);
            response.setLastName((String) userValue.get("lastName"));
            response.setFirstName((String) userValue.get("firstName"));
            response.setRole((String) userValue.get("role"));
            response.setPatientId((String) userValue.get("patientId"));
        } else {
            response.setStatus(ResponseStatus.FAIL);
        }
        return response;
    }
}

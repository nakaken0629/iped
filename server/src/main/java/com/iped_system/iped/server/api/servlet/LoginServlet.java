package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.LoginRequest;
import com.iped_system.iped.common.LoginResponse;
import com.iped_system.iped.common.ResponseStatus;

import java.util.Calendar;

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

        Query.Filter filter = new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, userId);
        Query query = new Query("User").setFilter(filter);
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = service.prepare(query);
        Entity user = preparedQuery.asSingleEntity();

        LoginResponse response = new LoginResponse();
        if (user != null && password.equals(user.getProperty("password"))) {
            Entity token = new Entity("token");
            Calendar calendar = Calendar.getInstance();
            token.setProperty("refreshDate", calendar.getTime());
            service.put(token);

            response.setStatus(ResponseStatus.SUCCESS);
            response.setTokenId(token.getKey().getId());
            response.setUserId(userId);
            response.setLastName((String) user.getProperty("lastName"));
            response.setFirstName((String) user.getProperty("firstName"));
        } else {
            response.setStatus(ResponseStatus.FAIL);
        }
        return response;
    }
}

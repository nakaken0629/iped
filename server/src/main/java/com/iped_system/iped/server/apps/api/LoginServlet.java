package com.iped_system.iped.server.apps.api;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.LoginRequest;
import com.iped_system.iped.common.LoginResponse;

/**
 * Created by kenji on 2014/08/04.
 */
public class LoginServlet extends BaseServlet {

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        LoginRequest request = (LoginRequest) baseRequest;
        String username = request.getUsername();
        String password = request.getPassword();

        Query.Filter filter = new Query.FilterPredicate("username", Query.FilterOperator.EQUAL, username);
        Query query = new Query("User").setFilter(filter);
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = service.prepare(query);
        Entity user = preparedQuery.asSingleEntity();

        LoginResponse response = new LoginResponse();
        if (user != null && password.equals(user.getProperty("password"))) {
            response.setStatus("SUCCESS");
        } else {
            response.setStatus("FAIL");
        }
        return response;
    }
}

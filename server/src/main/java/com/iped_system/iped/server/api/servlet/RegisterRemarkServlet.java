package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RegisterRemarkRequest;
import com.iped_system.iped.common.RegisterRemarkResponse;
import com.iped_system.iped.common.ResponseStatus;

/**
 * Created by kenji on 2014/08/09.
 */
public class RegisterRemarkServlet extends BaseServlet {

    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return RegisterRemarkRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        RegisterRemarkRequest request = (RegisterRemarkRequest) baseRequest;

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity remark = new Entity("remark");
        remark.setProperty("text", request.getText());
        service.put(remark);

        RegisterRemarkResponse response = new RegisterRemarkResponse();
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
    }
}

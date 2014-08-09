package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RegisterRemarkRequest;
import com.iped_system.iped.common.RegisterRemarkResponse;
import com.iped_system.iped.common.ResponseStatus;

import java.util.Date;
import java.util.Map;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksNewServlet extends BaseServlet {

    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return RegisterRemarkRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        RegisterRemarkRequest request = (RegisterRemarkRequest) baseRequest;
        Map<String, Object> userValue = getCurrentUserValue();
        String patientId = (String) userValue.get("patientId");
        String authorName = (String) userValue.get("lastName") + " " + (String) userValue.get("firstName");

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity remark = new Entity("remark");
        remark.setProperty("patientId", patientId);
        remark.setProperty("authorName", authorName);
        remark.setProperty("text", request.getText());
        remark.setProperty("createdAt", new Date());
        service.put(remark);

        RegisterRemarkResponse response = new RegisterRemarkResponse();
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
    }
}

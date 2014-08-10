package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RemarksNewRequest;
import com.iped_system.iped.common.RemarksNewResponse;
import com.iped_system.iped.common.Remark;

import java.util.Date;
import java.util.Map;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksNewServlet extends BaseServlet {

    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return RemarksNewRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        RemarksNewRequest request = (RemarksNewRequest) baseRequest;
        Map<String, Object> userValue = getCurrentUserValue();
        String patientId = (String) userValue.get("patientId");
        String authorName = (String) userValue.get("lastName") + " " + (String) userValue.get("firstName");

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity remark = new Entity("Remark");
        remark.setProperty("patientId", patientId);
        remark.setProperty("authorName", authorName);
        remark.setProperty("text", request.getText());
        remark.setProperty("createdAt", new Date());
        service.put(remark);

        RemarksNewResponse response = new RemarksNewResponse();
        Remark remarkValue = new Remark();
        remarkValue.setAuthorName(authorName);
        remarkValue.setText(request.getText());
        response.setRemark(remarkValue);
        return response;
    }
}

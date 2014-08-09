package com.iped_system.iped.server.apps.api;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RegisterMeetingRequest;
import com.iped_system.iped.common.RegisterMeetingResponse;
import com.iped_system.iped.common.ResponseStatus;

/**
 * Created by kenji on 2014/08/09.
 */
public class RegisterMeetingServlet extends BaseServlet {

    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return RegisterMeetingRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        RegisterMeetingRequest request = (RegisterMeetingRequest) baseRequest;

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity meetingArticle = new Entity("meetingArticle");
        meetingArticle.setProperty("text", request.getText());
        service.put(meetingArticle);

        RegisterMeetingResponse response = new RegisterMeetingResponse();
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
    }
}

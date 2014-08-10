package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.Remark;
import com.iped_system.iped.common.RemarksRequest;
import com.iped_system.iped.common.RemarksResponse;

import java.util.Map;

/**
 * Created by kenji on 2014/08/09.
 */
public class RemarksServlet extends BaseServlet {
    @Override
    protected Class<? extends BaseRequest> getRequestClass() {
        return RemarksRequest.class;
    }

    @Override
    protected BaseResponse execute(BaseRequest baseRequest) {
        Map<String, Object> userValue = getCurrentUserValue();
        String patientId = (String) userValue.get("patientId");

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query.Filter filter = new Query.FilterPredicate("patientId", Query.FilterOperator.EQUAL, patientId);
        Query query = new Query("Remark").setFilter(filter);
        PreparedQuery pq = service.prepare(query);
        RemarksResponse response = new RemarksResponse();
        for(Entity remark : pq.asIterable()) {
            Remark remarkValue = new Remark();
            remarkValue.setAuthorName((String)remark.getProperty("authorName"));
            remarkValue.setText((String)remark.getProperty("text"));
            response.getRemarks().add(remarkValue);
        }

        return response;
    }
}

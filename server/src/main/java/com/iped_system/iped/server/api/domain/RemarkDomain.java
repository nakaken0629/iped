package com.iped_system.iped.server.api.domain;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.common.Remark;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kenji on 2014/08/10.
 */
public class RemarkDomain {
    public static RemarkDomain getInstance() {
        return new RemarkDomain();
    }

    private RemarkDomain() {
        /* nop */
    }

    public List<Remark> search(String patientId) {
        return search(patientId, null);
    }

    public List<Remark> search(String patientId, Date lastUpdate) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query.Filter filter = new Query.FilterPredicate("patientId", Query.FilterOperator.EQUAL, patientId);
        if (lastUpdate != null) {
            Query.Filter lastUpdateFilter = new Query.FilterPredicate("createdAt", Query.FilterOperator.GREATER_THAN, lastUpdate);
            filter = Query.CompositeFilterOperator.and(filter, lastUpdateFilter);
        }
        Query query = new Query("Remark").setFilter(filter);
        query.addSort("createdAt", Query.SortDirection.DESCENDING);
        PreparedQuery pq = service.prepare(query);
        UserDomain userDomain = UserDomain.getInstance();
        ArrayList<Remark> remarks = new ArrayList<Remark>();
        for(Entity remark : pq.asIterable()) {
            Remark remarkValue = new Remark();
            String authorId = ((String) remark.getProperty("authorId"));
            Map<String, Object> authorValue = userDomain.getByUserId(authorId);
            String authorName = authorValue.get("lastName") + " " + authorValue.get("firstName");
            remarkValue.setAuthorName(authorName);
            remarkValue.setCreatedAt((Date)remark.getProperty("createdAt"));
            remarkValue.setText((String) remark.getProperty("text"));
            remarks.add(0, remarkValue);
        }

        return remarks;
    }
}

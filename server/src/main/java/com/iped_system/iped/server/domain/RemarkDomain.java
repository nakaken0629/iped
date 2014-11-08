package com.iped_system.iped.server.domain;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.common.main.RemarkValue;
import com.iped_system.iped.server.domain.model.Remark;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kenji on 2014/08/10.
 */
public class RemarkDomain {
    private static final int FETCH_SIZE = 30;

    public static RemarkDomain getInstance() {
        return new RemarkDomain();
    }

    private RemarkDomain() {
        /* nop */
    }

    public void insert(Remark remark) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        remark.save(service);
    }

    public List<Remark> search(String patientId) {
        return search(patientId, null);
    }

    public List<Remark> search(String patientId, Date lastDate) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query.Filter filter = new Query.FilterPredicate("patientId", Query.FilterOperator.EQUAL, patientId);
        if (lastDate != null) {
            Query.Filter lastDateFilter = new Query.FilterPredicate("createdAt", Query.FilterOperator.GREATER_THAN, lastDate);
            filter = Query.CompositeFilterOperator.and(filter, lastDateFilter);
        }
        Query query = new Query("Remark")
                .setFilter(filter)
                .addSort("createdAt", Query.SortDirection.DESCENDING);
        PreparedQuery pq = service.prepare(query);
        UserDomain userDomain = UserDomain.getInstance();
        ArrayList<Remark> remarks = new ArrayList<Remark>();
        for(Entity entity : pq.asIterable(FetchOptions.Builder.withLimit(FETCH_SIZE))) {
            Remark remark = new Remark(entity);
            remarks.add(remark);
        }

        return remarks;
    }
}

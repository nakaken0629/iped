package com.iped_system.iped.server.domain;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.common.main.TalkValue;
import com.iped_system.iped.server.domain.model.Talk;
import com.iped_system.iped.server.domain.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kenji on 2014/08/25.
 */
public final class TalkDomain {
    public static TalkDomain getInstance() {
        return new TalkDomain();
    }

    private TalkDomain() {
        /* nop */
    }

    public Entity insert(String userId, String patientId, String text, String pictogramKey) {
        Date createdAt = new Date();

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("Talk");
        entity.setProperty("userId", userId);
        entity.setProperty("patientId", patientId);
        entity.setProperty("text", text);
        entity.setProperty("pictogramKey", pictogramKey);
        entity.setProperty("createdAt", createdAt);
        service.put(entity);
        return entity;
    }

    public List<Talk> search(String patientId) {
        return search(patientId, null);
    }

    public List<Talk> search(String patientId, Date lastUpdate) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query.Filter filter = new Query.FilterPredicate("patientId", Query.FilterOperator.EQUAL, patientId);
        if (lastUpdate != null) {
            Query.Filter lastUpdateFilter = new Query.FilterPredicate("createdAt", Query.FilterOperator.GREATER_THAN, lastUpdate);
            filter = Query.CompositeFilterOperator.and(filter, lastUpdateFilter);
        }
        Query query = new Query("Talk").setFilter(filter);
        query.addSort("createdAt");
        PreparedQuery pq = service.prepare(query);
        UserDomain userDomain = UserDomain.getInstance();
        ArrayList<Talk> talks = new ArrayList<Talk>();
        for(Entity entity : pq.asIterable()) {
            Talk talk = new Talk(entity);
            talks.add(talk);
        }

        return talks;
    }
}

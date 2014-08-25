package com.iped_system.iped.server.api.domain;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.common.Talk;

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

    public Entity insert(String authorId, String patientId, String text) {
        Date createdAt = new Date();

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("Talk");
        entity.setProperty("authorId", authorId);
        entity.setProperty("patientId", patientId);
        entity.setProperty("text", text);
        entity.setProperty("createdAt", createdAt);
        service.put(entity);
        return entity;
    }

    public List<Talk> search(String userId, String patientId) {
        return search(userId, patientId, null);
    }

    public List<Talk> search(String userId, String patientId, Date lastUpdate) {
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
        for(Entity talk : pq.asIterable()) {
            Talk talkValue = new Talk();
            String authorId = (String) talk.getProperty("authorId");
            String text = (String) talk.getProperty("text");
            if (userId.equals(authorId)) {
                /* do as me */
                talkValue.setMeText(text);
            } else {
                /* do as other people */
                Map<String, Object> authorValue = userDomain.getByUserId(authorId);
                String authorName = authorValue.get("lastName") + " " + authorValue.get("firstName");
                talkValue.setAuthorName(authorName);
                talkValue.setYouText(text);
            }
            talkValue.setCreatedAt((Date) talk.getProperty("createdAt"));
            talks.add(talkValue);
        }

        return talks;
    }
}

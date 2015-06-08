package com.iped_system.iped.server.domain;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.server.domain.model.Talk;
import com.iped_system.iped.server.domain.model.TalkSummary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by kenji on 2014/08/25.
 */
public final class TalkDomain {
    private static final int FETCH_SIZE = 30;

    public static TalkDomain getInstance() {
        return new TalkDomain();
    }

    private TalkDomain() {
        /* nop */
    }

    public void insert(Talk talk) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        talk.setRecorded(true);
        talk.save(service);
        updateTalkSummary(service, talk);
    }

    private void updateTalkSummary(DatastoreService service, Talk talk) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTime(talk.getCreatedAt());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date talkDate = calendar.getTime();
        Query.Filter filter = new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, talk.getUserId());
        Query.Filter talkDateFilter = new Query.FilterPredicate("talkDate", Query.FilterOperator.EQUAL, talkDate);
        filter = Query.CompositeFilterOperator.and(filter, talkDateFilter);
        Query query = new Query("TalkSummary")
                .setFilter(filter);
        PreparedQuery pq = service.prepare(query);
        Entity entity = pq.asSingleEntity();
        TalkSummary talkSummary;
        if (entity == null) {
            talkSummary = new TalkSummary();
            talkSummary.setUserId(talk.getUserId());
            talkSummary.setTalkDate(talkDate);
        } else {
            talkSummary = new TalkSummary(entity);
        }
        if (talk.getText() != null) {
            talkSummary.incrementTalkCount();
        } else if (talk.getPictogramKey() != null) {
            talkSummary.incrementPictogram();
        } else if (talk.getPictureId() != null) {
            talkSummary.incrementPicture();
        }
        talkSummary.save(service);
    }

    public List<Talk> search(String patientId) {
        return search(patientId, null, null);
    }

    public List<Talk> search(String patientId, Date firstUpdate, Date lastUpdate) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query.Filter filter = new Query.FilterPredicate("patientId", Query.FilterOperator.EQUAL, patientId);
        if (firstUpdate != null) {
            Query.Filter updateFilter = new Query.FilterPredicate("createdAt", Query.FilterOperator.LESS_THAN, firstUpdate);
            filter = Query.CompositeFilterOperator.and(filter, updateFilter);
        } else if (lastUpdate != null) {
            Query.Filter updateFilter = new Query.FilterPredicate("createdAt", Query.FilterOperator.GREATER_THAN, lastUpdate);
            filter = Query.CompositeFilterOperator.and(filter, updateFilter);
        }
        Query query = new Query("Talk")
                .setFilter(filter)
                .addSort("createdAt", Query.SortDirection.DESCENDING);
        PreparedQuery pq = service.prepare(query);
        ArrayList<Talk> talks = new ArrayList<Talk>();
        for (Entity entity : pq.asIterable(FetchOptions.Builder.withLimit(FETCH_SIZE))) {
            Talk talk = new Talk(entity);
            talks.add(talk);
        }
        Collections.reverse(talks);

        return talks;
    }

    public int refreshSummaries() {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Talk");
        PreparedQuery pq = service.prepare(query);
        int count = 0;
        for (Entity entity : pq.asIterable()) {
            Talk talk = new Talk(entity);
            if (talk.isRecorded()) {
                continue;
            }
            talk.setRecorded(true);
            talk.save(service);
            updateTalkSummary(service, talk);
            count++;
        }
        return count;
    }

    public List<TalkSummary> getSummaries() {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("TalkSummary")
            .addSort("talkDate")
            .addSort("userId");
        PreparedQuery pq = service.prepare(query);
        ArrayList<TalkSummary> summaries = new ArrayList<TalkSummary>();
        for (Entity entity : pq.asIterable()) {
            summaries.add(new TalkSummary(entity));
        }
        return summaries;
    }
}

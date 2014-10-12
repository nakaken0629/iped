package com.iped_system.iped.server.domain;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.server.domain.model.User;

import java.util.Map;

/**
 * Created by kenji on 2014/08/10.
 */
public class UserDomain {
    public static UserDomain getInstance() {
        return new UserDomain();
    }

    private UserDomain() {
        /* nop */
    }

    public User getByUserId(String userId) {
        try {
            return getByUserIdImpl(userId);
        } catch (Exception e) {
            /* TODO: 例外の種類は正しく考えること */
            return null;
        }
    }

    private User getByUserIdImpl(String userId) {
        /* TODO: キャッシュを使うようにすること */
        Query.Filter filter = new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, userId);
        Query query = new Query("User").setFilter(filter);
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = service.prepare(query);
        Entity entity = preparedQuery.asSingleEntity();

        User user = new User(entity);
        return user;
    }

    public void update(User user) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        user.save(service);
    }
}

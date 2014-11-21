package com.iped_system.iped.server.domain;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.iped_system.iped.server.domain.model.Picture;

/**
 * Created by kenji on 2014/11/16.
 */
public final class PictureDomain {
    public static PictureDomain getInstance() {
        return new PictureDomain();
    }

    private PictureDomain() {
        /* nop */
    }

    public void insert(Picture picture) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        picture.save(service);
    }

    public Picture get(long id) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey("Picture", id);
        Entity entity = null;
        try {
            entity = service.get(key);
            return new Picture(entity);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }
}

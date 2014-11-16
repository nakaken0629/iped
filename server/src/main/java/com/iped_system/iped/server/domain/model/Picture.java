package com.iped_system.iped.server.domain.model;

import com.google.appengine.api.datastore.Entity;

/**
 * Created by kenji on 2014/11/16.
 */
public class Picture extends EntityWrapper {
    @EntityProperty private String blobKey;
    @EntityProperty private String owner;

    public Picture() {
        super();
    }

    public Picture(Entity entity) {
        super(entity);
    }

    public String getBlobKey() {
        return blobKey;
    }

    public void setBlobKey(String blobKey) {
        this.blobKey = blobKey;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

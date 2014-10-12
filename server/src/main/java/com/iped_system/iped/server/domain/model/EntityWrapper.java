package com.iped_system.iped.server.domain.model;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by kenji on 2014/10/11.
 */
public abstract class EntityWrapper {
    private static final Logger logger = Logger.getLogger(EntityWrapper.class.getName());

    private Entity entity;

    public long getId() {
        return this.entity == null ? 0 : this.entity.getKey().getId();
    }

    public EntityWrapper() {
        /* nop */
    }

    public EntityWrapper(Entity entity) {
        this.entity = entity;

        Class<? extends EntityWrapper> clazz = this.getClass();
        for(Map.Entry<String, Object> entry : entity.getProperties().entrySet()) {
            try {
                Field field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(this, entry.getValue());
            } catch (NoSuchFieldException e) {
                /* ignore */
            } catch (IllegalAccessException e) {
                /* ignore */
            }
        }
    }

    public void save(DatastoreService service) {
        Class<? extends EntityWrapper> clazz = this.getClass();
        if (this.entity == null) {
            this.entity = new Entity(clazz.getSimpleName());
        }

        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            EntityProperty property = field.getAnnotation(EntityProperty.class);
            if(property == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                this.entity.setProperty(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                /* ignore */
            }
        }

        service.put(this.entity);
    }
}

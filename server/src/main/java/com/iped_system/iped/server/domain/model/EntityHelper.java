package com.iped_system.iped.server.domain.model;

import com.google.appengine.api.datastore.Entity;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by kenji on 2014/10/11.
 */
public abstract class EntityHelper {
    private static final Logger logger = Logger.getLogger(EntityHelper.class.getName());

    private long id;

    public long getId() {
        return this.id;
    }

    public Entity createEntity() {
        Class<? extends EntityHelper> clazz = this.getClass();
        Entity entity = new Entity(clazz.getSimpleName());

        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            EntityProperty property = field.getAnnotation(EntityProperty.class);
            if(property == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                entity.setProperty(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                logger.warning(e.toString());
            }
        }
        return entity;
    }

    public void fromEntity(Entity entity) {
        this.id = entity.getKey().getId();

        Class<? extends EntityHelper> clazz = this.getClass();
        for(Map.Entry<String, Object> entry : entity.getProperties().entrySet()) {
            try {
                Field field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(this, entry.getValue());
            } catch (NoSuchFieldException e) {
                logger.warning(e.toString());
            } catch (IllegalAccessException e) {
                logger.warning(e.toString());
            }
        }
    }
}

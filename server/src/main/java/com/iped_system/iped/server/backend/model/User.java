package com.iped_system.iped.server.backend.model;

/**
 * Created by kenji on 2014/08/09.
 */
public class User {
    private long userId;
    private String lastName;
    private String firstName;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}

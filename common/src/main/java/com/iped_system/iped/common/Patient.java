package com.iped_system.iped.common;

/**
 * Created by kenji on 2014/11/21.
 */
public class Patient {
    private String userId;
    private String firstName;
    private String lastName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return this.lastName + " " + this.firstName;
    }
}

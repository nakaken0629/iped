package com.iped_system.iped.server.backend.model;

/**
 * Created by kenji on 2014/08/09.
 */
public class User {
    private long id;
    private String userId;
    private String lastName;
    private String firstName;
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isUserIdValid() {
        return this.userId != null && this.userId.length() > 0;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isLastNameValid() {
        return this.lastName != null && this.lastName.length() > 0;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isFirstNameValid() {
        return this.firstName != null && this.firstName.length() > 0;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordValid() {
        return this.password != null && this.password.length() > 0;
    }

    public boolean isValid() {
        return isUserIdValid() && isLastNameValid() && isFirstNameValid() && isPasswordValid();
    }
}

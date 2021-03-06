package com.iped_system.iped.server.domain.model;

import com.google.appengine.api.datastore.Entity;
import com.iped_system.iped.common.RoleType;

import java.util.List;

/**
 * Created by kenji on 2014/08/09.
 */
public class User extends EntityWrapper {
    @EntityProperty private String userId;
    @EntityProperty private String lastName;
    @EntityProperty private String firstName;
    @EntityProperty private String password;
    @EntityProperty private RoleType role;
    @EntityProperty private List<String> patientIdList;
    @EntityProperty private long faceId;

    public User() {
        super();
    }

    public User(Entity entity) {
        super(entity);
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

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public boolean isRoleValid() {
        return this.role != null;
    }

    public List<String> getPatientIdList() {
        return patientIdList;
    }

    public void setPatientIdList(List<String> patientIdList) {
        this.patientIdList = patientIdList;
    }

    public long getFaceId() {
        return faceId;
    }

    public void setFaceId(long faceId) {
        this.faceId = faceId;
    }

    public String getName() {
        return this.lastName + " " + this.firstName;
    }

    public boolean isValid() {
        return isUserIdValid()
                && isLastNameValid()
                && isFirstNameValid()
                && isPasswordValid()
                && isRoleValid()
                ;
    }
}

package com.iped_system.iped.common;

/**
 * Created by kenji on 2014/08/24.
 */
public enum RoleType {
    SCHOLAR("研究者"), STUDENT("学生"), PATIENT("患者"), MEDICAL_STAFF("医療者"), ADMIN("管理者");

    private final String displayName;

    private RoleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}

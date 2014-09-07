package com.iped_system.iped.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenji on 2014/08/24.
 */
public abstract class RoleType {
    private static final ArrayList<String> roles;
    
    static {
        roles = new ArrayList<String>();
        roles.add("教員");
        roles.add("生徒");
        roles.add("患者");
    }

    public static List<String> getRoles() {
        return roles;
    }
}

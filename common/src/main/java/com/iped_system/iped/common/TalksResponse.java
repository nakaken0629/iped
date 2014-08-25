package com.iped_system.iped.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenji on 2014/08/25.
 */
public class TalksResponse extends BaseResponse {
    private List<Talk> talks = new ArrayList<Talk>();

    public List<Talk> getTalks() {
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }
}

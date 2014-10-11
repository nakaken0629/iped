package com.iped_system.iped.app.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FragmentTabHost host = (FragmentTabHostEx) findViewById(android.R.id.tabhost);
        host.setup(this, getSupportFragmentManager(), R.id.content);

        IpedApplication application = (IpedApplication) getApplication();
        String role = application.getRole();
        if ("患者".equals(role)) {
            createAsPatient(host);
        } else {
            createAsMember(host);
        }
    }

    private void createAsMember(FragmentTabHost host) {
        TabHost.TabSpec tabSpecMeeting = host.newTabSpec("meeting");
        tabSpecMeeting.setIndicator("ミーティング");
        host.addTab(tabSpecMeeting, MeetingFragment.class, null);

        TabHost.TabSpec tabSpecInterview = host.newTabSpec("interview");
        tabSpecInterview.setIndicator("インタビュー");
        host.addTab(tabSpecInterview, InterviewFragment.class, null);
    }

    private void createAsPatient(FragmentTabHost host) {
        TabHost.TabSpec tabSpecInterview = host.newTabSpec("interview");
        tabSpecInterview.setIndicator("インタビュー");
        host.addTab(tabSpecInterview, InterviewFragment.class, null);
    }
}

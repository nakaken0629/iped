package com.iped_system.iped;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost;

import com.iped_system.iped.R;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FragmentTabHost host = (FragmentTabHost) findViewById(android.R.id.tabhost);
        host.setup(this, getSupportFragmentManager(), R.id.content);

        TabHost.TabSpec tabSpecMeeting = host.newTabSpec("meeting");
        tabSpecMeeting.setIndicator("meeting");
        host.addTab(tabSpecMeeting, MeetingFragment.class, null);

        TabHost.TabSpec tabSpecInterview = host.newTabSpec("interview");
        tabSpecInterview.setIndicator("interview");
        host.addTab(tabSpecInterview, MeetingFragment.class, null);
    }
}

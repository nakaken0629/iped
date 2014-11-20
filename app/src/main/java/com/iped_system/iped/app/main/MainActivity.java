package com.iped_system.iped.app.main;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.TabHost;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.common.Patient;
import com.iped_system.iped.common.RoleType;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {
    private LruCache<String, Bitmap> imageCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemory / 8;
        this.imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        IpedApplication application = (IpedApplication) getApplication();

        ArrayAdapter<Patient> adapter = new ArrayAdapter<Patient>(this, android.R.layout.simple_spinner_dropdown_item);
        for(Patient patient : application.getPatients()) {
            adapter.add(patient);
        }
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(adapter, this);

        setContentView(R.layout.activity_main);
        FragmentTabHost host = (FragmentTabHostEx) findViewById(android.R.id.tabhost);
        host.setup(this, getSupportFragmentManager(), R.id.content);

        RoleType role = application.getRole();
        if (role != RoleType.PATIENT) {
            prepareMeetingTab(host);
        }
        prepareInterviewTab(host);
    }

    private void prepareMeetingTab(FragmentTabHost host) {
        TabHost.TabSpec tabSpecMeeting = host.newTabSpec("meeting");
        tabSpecMeeting.setIndicator("ミーティング");
        host.addTab(tabSpecMeeting, MeetingFragment.class, null);
    }

    private void prepareInterviewTab(FragmentTabHost host) {
        TabHost.TabSpec tabSpecInterview = host.newTabSpec("interview");
        tabSpecInterview.setIndicator("インタビュー");
        host.addTab(tabSpecInterview, InterviewFragment.class, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return true;
    }
}

package com.iped_system.iped.app.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.LruCache;
import android.widget.TabHost;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.common.RoleType;

public class MainActivity extends FragmentActivity {
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

        setContentView(R.layout.activity_main);
        FragmentTabHost host = (FragmentTabHostEx) findViewById(android.R.id.tabhost);
        host.setup(this, getSupportFragmentManager(), R.id.content);

        IpedApplication application = (IpedApplication) getApplication();
        RoleType role = application.getRole();
        if (role == RoleType.PATIENT) {
            createAsPatient(host);
        } else {
            createAsMember(host);
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            this.imageCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return this.imageCache.get(key);
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

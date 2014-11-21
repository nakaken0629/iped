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

import java.util.HashMap;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {
    private static final String TAG = MainActivity.class.getName();
    private final MainActivity parent = this;

    /* TODO: FragmentTabHostから現在のfragmentが取得できれば、このインターフェイスは不要 */
    public interface RefreshObserver {
        public void refresh();
    }

    private LruCache<String, Bitmap> imageCache;
    private FragmentTabHost host;
    private HashMap<String, RefreshObserver> observers = new HashMap<String, RefreshObserver>();
    private String currentTab;

    private IpedApplication getIpedApplication() {
        return (IpedApplication) getApplication();
    }

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

        ArrayAdapter<Patient> adapter = new ArrayAdapter<Patient>(this, android.R.layout.simple_spinner_dropdown_item);
        for (Patient patient : getIpedApplication().getPatients()) {
            adapter.add(patient);
        }
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(adapter, this);

        setContentView(R.layout.activity_main);
        this.host = (FragmentTabHostEx) findViewById(android.R.id.tabhost);
        this.host.setup(this, getSupportFragmentManager(), R.id.content);
        this.host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tab) {
                parent.currentTab = tab;
                /* TODO: ここで呼ぶのが妥当な気はしない */
                if (parent.observers.containsKey(tab)) {
                    RefreshObserver observer = parent.observers.get(tab);
                    observer.refresh();
                }
            }
        });

        RoleType role = getIpedApplication().getRole();
        if (role != RoleType.PATIENT) {
            prepareMeetingTab();
        }
        prepareInterviewTab();
    }

    private void prepareMeetingTab() {
        TabHost.TabSpec tabSpecMeeting = this.host.newTabSpec("meeting");
        tabSpecMeeting.setIndicator("ミーティング");
        this.host.addTab(tabSpecMeeting, MeetingFragment.class, null);
    }

    private void prepareInterviewTab() {
        TabHost.TabSpec tabSpecInterview = this.host.newTabSpec("interview");
        tabSpecInterview.setIndicator("インタビュー");
        this.host.addTab(tabSpecInterview, InterviewFragment.class, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        getIpedApplication().setPatientIndex(itemPosition);
        RefreshObserver observer = this.observers.get(this.currentTab);
        observer.refresh();
        return true;
    }

    public void addObserver(String key, RefreshObserver observer) {
        this.observers.put(key, observer);
    }
}

package com.iped_system.iped.app.main;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TabHost;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.app.common.os.VersionTask;
import com.iped_system.iped.common.Patient;
import com.iped_system.iped.common.RoleType;
import com.iped_system.iped.common.login.VersionRequest;
import com.iped_system.iped.common.login.VersionResponse;

import java.util.HashMap;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {
    private static final String TAG = MainActivity.class.getName();
    private final MainActivity parent = this;
    private MenuItem updateItem;

    /* TODO: FragmentTabHostから現在のfragmentが取得できれば、このインターフェイスは不要 */
    public interface RefreshObserver {
        public void refresh();
    }

    private FragmentTabHost host;
    private HashMap<String, RefreshObserver> observers = new HashMap<String, RefreshObserver>();
    private String currentTab;

    private IpedApplication getIpedApplication() {
        return (IpedApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preparePatientSpinner();

        RoleType role = getIpedApplication().getRole();
        if (role != RoleType.PATIENT) {
            prepareMeetingTab();
        }
        prepareInterviewTab();
    }

    private void preparePatientSpinner() {
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
        this.updateItem = menu.findItem(R.id.updateMenu);
        Log.d(TAG, "updateItem is " + (this.updateItem == null ? "null" : "not null"));

        MainVersionTask task = new MainVersionTask(this);
        VersionRequest request = new VersionRequest();
        task.execute(request);

        return true;
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

    private class MainVersionTask extends VersionTask {
        private MainVersionTask(Activity activity) {
            super(activity);
             parent.updateItem.setVisible(false);
        }

        @Override
        protected void onPostExecuteOnSuccess(VersionResponse versionResponse) {
            if (getVersionCode() < versionResponse.getVersionCode()) {
                parent.updateItem.setVisible(true);
            }
        }
    }

}

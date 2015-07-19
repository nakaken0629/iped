package com.iped_system.iped.app.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.app.common.service.QuickstartPreferences;
import com.iped_system.iped.app.common.service.RegistrationIntentService;
import com.iped_system.iped.app.main.MainActivity;

public class LoginActivity extends FragmentActivity implements LoginFragment.OnLoginListener {
    private static final String TAG = LoginActivity.class.getName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String FRAGMENT_LOGOUT = "LOGOUT";
    public static final String KEY_LOGOUT = "LOGOUT";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
//    private ProgressBar mRegistrationProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment(), FRAGMENT_LOGOUT)
                    .commit();
        }

//        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.d(TAG, getString(R.string.gcm_send_message));
//                    mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    Log.d(TAG, getString(R.string.token_error_message));
//                    mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Log.d(TAG, "start RegistrationIntentService");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivityForResult(intent, IpedApplication.REQUEST_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != IpedApplication.REQUEST_LOGIN) {
            return;
        }

        if (data != null && data.getBooleanExtra(LoginActivity.KEY_LOGOUT, false)) {
            LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_LOGOUT);
            fragment.resetTextView();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}

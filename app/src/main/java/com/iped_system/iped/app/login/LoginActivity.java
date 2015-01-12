package com.iped_system.iped.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.app.main.MainActivity;

public class LoginActivity extends FragmentActivity implements LoginFragment.OnLoginListener {
    public static final String FRAGMENT_LOGOUT = "LOGOUT";
    public static final String KEY_LOGOUT = "LOGOUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment(), FRAGMENT_LOGOUT)
                    .commit();
        }
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
}

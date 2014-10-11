package com.iped_system.iped.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.iped_system.iped.R;
import com.iped_system.iped.app.ui.MainActivity;

public class LoginActivity extends FragmentActivity implements LoginFragment.OnLoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }

    @Override
    public void onLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}

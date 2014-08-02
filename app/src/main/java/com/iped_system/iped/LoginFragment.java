package com.iped_system.iped;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = (Button) rootView.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginButtonListener());
        return rootView;
    }

    class LoginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Activity activity = LoginFragment.this.getActivity();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        }
    }
}

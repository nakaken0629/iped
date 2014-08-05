package com.iped_system.iped;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.LoginRequest;
import com.iped_system.iped.common.LoginResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = (Button) rootView.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginButtonListener());
        return rootView;
    }

    public void setEnabled(boolean enabled) {
        getView().findViewById(R.id.usernameEditText).setEnabled(enabled);
        getView().findViewById(R.id.passwordEditText).setEnabled(enabled);
        getView().findViewById(R.id.loginButton).setEnabled(enabled);
    }

    class LoginButtonListener implements View.OnClickListener {
        private String getEditTextValue(int id) {
            return ((EditText) getView().findViewById(id)).getText().toString();
        }

        @Override
        public void onClick(View view) {
            String username = getEditTextValue(R.id.usernameEditText);
            String password = getEditTextValue(R.id.passwordEditText);
            setEnabled(false);

            LoginAsyncTask task = new LoginAsyncTask(username, password);
            task.execute();
        }
    }

    class LoginAsyncTask extends AsyncTask<Void, Void, Void> {
        private LoginRequest loginRequest;

        public LoginAsyncTask(String username, String password) {
            this.loginRequest = new LoginRequest();
            this.loginRequest.setUsername(username);
            this.loginRequest.setPassword(password);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            android.util.Log.d(TAG, "call doInBackground");
            try {
                doNetworkAccess();
            } catch (IOException e) {
                android.util.Log.e(TAG, "error", e);
                cancel(true);
            }
            return null;
        }

        private void doNetworkAccess() throws IOException {
            URL url = new URL("http://10.0.2.2:8080/api/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            String parameter = "parameter=" + this.loginRequest.toJSON();
            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            writer.print(parameter);
            writer.close();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            LoginResponse response = (LoginResponse) BaseResponse.fromJSON(reader, LoginResponse.class);
        }

        @Override
        protected void onCancelled() {
            setEnabled(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setEnabled(true);

            /* TODO: 本当はActivityに通知する実装が良い */
            Activity activity = getActivity();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        }
    }
}

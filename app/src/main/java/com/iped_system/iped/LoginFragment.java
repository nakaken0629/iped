package com.iped_system.iped;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.iped_system.iped.common.ResponseStatus;

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
        getView().findViewById(R.id.userIdEditText).setEnabled(enabled);
        getView().findViewById(R.id.passwordEditText).setEnabled(enabled);
        getView().findViewById(R.id.loginButton).setEnabled(enabled);
    }

    class LoginButtonListener implements View.OnClickListener {
        private String getEditTextValue(int id) {
            return ((EditText) getView().findViewById(id)).getText().toString().trim();
        }

        @Override
        public void onClick(View view) {
            String userId = getEditTextValue(R.id.userIdEditText);
            String password = getEditTextValue(R.id.passwordEditText);
            setEnabled(false);

            LoginRequest request = new LoginRequest();
            request.setUserId(userId);
            request.setPassword(password);
            LoginAsyncTask task = new LoginAsyncTask();
            task.execute(request);
        }
    }

    class LoginAsyncTask extends AsyncTask<LoginRequest, Void, LoginResponse> {

        @Override
        protected LoginResponse doInBackground(LoginRequest... requests) {
            try {
                LoginRequest request = requests[0];
                return doNetworkAccess(request);
            } catch (IOException e) {
                android.util.Log.e(TAG, "error", e);
                cancel(true);
                return null;
            }
        }

        private LoginResponse doNetworkAccess(LoginRequest request) throws IOException {
            URL url = new URL("http://10.0.2.2:8080/api/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            String parameter = "parameter=" + request.toJSON();
            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            writer.print(parameter);
            writer.close();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            LoginResponse response = (LoginResponse) BaseResponse.fromJSON(reader, LoginResponse.class);
            return response;
        }

        @Override
        protected void onCancelled() {
            setEnabled(true);
        }

        @Override
        protected void onPostExecute(LoginResponse response) {
            setEnabled(true);

            if (response.getStatus() == ResponseStatus.SUCCESS) {
                /* TODO: 本当はActivityに通知する実装が良い */
                Activity activity = getActivity();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("ユーザ名かパスワードが正しくありません");
                builder.setPositiveButton("確認", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}

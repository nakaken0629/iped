package com.iped_system.iped.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.app.network.ApiAsyncTask;
import com.iped_system.iped.common.LoginRequest;
import com.iped_system.iped.common.LoginResponse;
import com.iped_system.iped.common.ResponseStatus;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();

    public interface OnLoginListener {
        public void onLogin();
    }

    private OnLoginListener onLoginListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = (Button) rootView.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginButtonListener());
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.onLoginListener = (OnLoginListener) activity;
    }

    class LoginButtonListener implements View.OnClickListener {
        private String getEditTextValue(int id) {
            return ((EditText) getView().findViewById(id)).getText().toString().trim();
        }

        @Override
        public void onClick(View view) {
            String userId = getEditTextValue(R.id.userIdEditText);
            String password = getEditTextValue(R.id.passwordEditText);
            LoginRequest request = new LoginRequest();
            request.setUserId(userId);
            request.setPassword(password);

            LoginAsyncTask task = new LoginAsyncTask(LoginFragment.this.getActivity());
            task.execute(request);
        }
    }

    class LoginAsyncTask extends ApiAsyncTask<LoginRequest, LoginResponse> {
        LoginAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected boolean isSecure() {
            return false;
        }

        @Override
        protected String getApiName() {
            return "login";
        }

        @Override
        protected void onPostExecute(LoginResponse loginResponse) {
            Activity activity = getActivity();
            if (activity == null) {
                return;
            }

            if (loginResponse.getStatus() == ResponseStatus.SUCCESS) {
                IpedApplication application = (IpedApplication) activity.getApplication();
                application.authenticate(loginResponse);
                onLoginListener.onLogin();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                AlertDialog dialog = builder.setTitle("メッセージ")
                        .setMessage("ユーザーIDかパスワードが正しくありません")
                        .setPositiveButton("確認", null)
                        .create();
                dialog.show();
            }
        }
    }
}

package com.iped_system.iped.app.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.app.common.os.ApiAsyncTask;
import com.iped_system.iped.app.common.widget.EditTextEx;
import com.iped_system.iped.common.login.LoginRequest;
import com.iped_system.iped.common.login.LoginResponse;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();

    private EditTextEx userIdEditText;
    private EditTextEx passwordEditText;
    private Button loginButton;

    public interface OnLoginListener {
        public void onLogin();
    }

    private OnLoginListener onLoginListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        this.userIdEditText = (EditTextEx) rootView.findViewById(R.id.userIdEditText);
        this.passwordEditText = (EditTextEx) rootView.findViewById(R.id.passwordEditText);
        this.loginButton = (Button) rootView.findViewById(R.id.loginButton);
        this.loginButton.setOnClickListener(new LoginButtonListener());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.onLoginListener = (OnLoginListener) activity;
    }

    class LoginButtonListener implements View.OnClickListener {
        private LoginFragment parent = LoginFragment.this;

        @Override
        public void onClick(View view) {
            String userId = parent.userIdEditText.getTrimmedValue();
            String password = parent.passwordEditText.getTrimmedValue();
            LoginRequest request = new LoginRequest();
            request.setUserId(userId);
            request.setPassword(password);

            LoginAsyncTask task = new LoginAsyncTask(parent.getActivity());
            task.execute(request);
        }
    }

    class LoginAsyncTask extends ApiAsyncTask<LoginRequest, LoginResponse> {
        private LoginFragment parent = LoginFragment.this;

        LoginAsyncTask(Activity activity) {
            super(activity);
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
        protected void onPostExecuteOnSuccess(LoginResponse resp) {
            long tokenId = resp.getTokenId();
            String userId = resp.getUserId();
            String lastName = resp.getLastName();
            String firstName = resp.getFirstName();
            String role = resp.getRole();
            String patientId = resp.getPatientId();
            IpedApplication application = (IpedApplication) this.getActivity().getApplication();
            application.authenticate(tokenId, userId, lastName, firstName, role, patientId);
            parent.onLoginListener.onLogin();
        }

        @Override
        protected void onPostExecuteOnFailure(LoginResponse resp) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog dialog = builder.setTitle("メッセージ")
                    .setMessage("ユーザーIDかパスワードが正しくありません")
                    .setPositiveButton("確認", null)
                    .create();
            dialog.show();
        }
    }
}

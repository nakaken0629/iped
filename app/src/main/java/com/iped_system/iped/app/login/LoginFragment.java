package com.iped_system.iped.app.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.app.common.os.ApiAsyncTask;
import com.iped_system.iped.app.common.os.UpdateAsyncTask;
import com.iped_system.iped.app.common.os.VersionTask;
import com.iped_system.iped.app.common.widget.EditTextEx;
import com.iped_system.iped.common.Patient;
import com.iped_system.iped.common.RoleType;
import com.iped_system.iped.common.login.LoginRequest;
import com.iped_system.iped.common.login.LoginResponse;
import com.iped_system.iped.common.login.VersionRequest;
import com.iped_system.iped.common.login.VersionResponse;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();
    private static final String PREFERENCE_USER_ID = "PREFERENCE_USER_ID";
    private static final String PREFERENCE_PASSWORD = "PREFERENCE_PASSWORD";
    private final LoginFragment parent = this;

    private TextView versionNameTextView;
    private TextView updateTextView;
    private Button updateButton;
    private EditTextEx userIdEditText;
    private EditTextEx passwordEditText;
    private Button loginButton;

    private String updateUrl;

    public interface OnLoginListener {
        public void onLogin();
    }

    private OnLoginListener onLoginListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        this.versionNameTextView = (TextView) rootView.findViewById(R.id.versionNameTextView);
        this.updateTextView = (TextView) rootView.findViewById(R.id.updateTextView);
        this.updateButton = (Button) rootView.findViewById(R.id.updateButton);
        this.userIdEditText = (EditTextEx) rootView.findViewById(R.id.userIdEditText);
        this.passwordEditText = (EditTextEx) rootView.findViewById(R.id.passwordEditText);

        this.versionNameTextView.setText("バージョン " + getVersionName());
        this.updateTextView.setText("");
        this.updateButton.setVisibility(View.INVISIBLE);
        this.updateButton.setOnClickListener(new UpdateButtonListener());
        this.loginButton = (Button) rootView.findViewById(R.id.loginButton);
        this.loginButton.setOnClickListener(new LoginButtonListener());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        this.userIdEditText.setText(preferences.getString(PREFERENCE_USER_ID, ""));
        this.passwordEditText.setText(preferences.getString(PREFERENCE_PASSWORD, ""));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.onLoginListener = (OnLoginListener) activity;

        LoginVersionTask task = new LoginVersionTask(activity);
        VersionRequest request = new VersionRequest();
        task.execute(request);
    }

    public void resetTextView() {
        this.userIdEditText.setText(null);
        this.passwordEditText.setText(null);

        this.userIdEditText.requestFocus();
    }

    protected String getVersionName() {
        PackageManager pm = getActivity().getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getActivity().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "バージョン名を取得できませんでした", e);
        }
        return versionName;
    }

    private class LoginVersionTask extends VersionTask {
        private LoginVersionTask(Activity activity) {
            super(activity);
        }

        @Override
        protected void onDisconnected() {
            parent.updateTextView.setText("最新版の有無を確認できません");
        }

        @Override
        protected void onPostExecuteOnSuccess(VersionResponse versionResponse) {
            if (getVersionCode() < versionResponse.getVersionCode()) {
                parent.updateUrl = versionResponse.getUrl();
                parent.updateTextView.setText("最新版にアップデートしてください");
                parent.updateButton.setVisibility(View.VISIBLE);
                parent.updateButton.setEnabled(true);
            } else {
                parent.updateTextView.setText("最新版を利用しています");
            }
        }

        @Override
        protected void onPostExecuteOnFailure(VersionResponse versionResponse) {
            parent.updateTextView.setText("最新版の有無の取得に失敗しました");
        }
    }

    class UpdateButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            UpdateAsyncTask task = new UpdateAsyncTask(parent.getActivity());
            task.execute(parent.updateUrl);
        }
    }

    class LoginButtonListener implements View.OnClickListener {
        private LoginFragment parent = LoginFragment.this;

        @Override
        public void onClick(View view) {
            String userId = parent.userIdEditText.getTrimmedValue();
            String password = parent.passwordEditText.getTrimmedValue();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginFragment.this.getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREFERENCE_USER_ID, userId);
            editor.putString(PREFERENCE_PASSWORD, password);
            editor.commit();
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
            RoleType role = resp.getRole();
            List<Patient> patients = resp.getPatients();
            IpedApplication application = (IpedApplication) this.getActivity().getApplication();
            application.authenticate(tokenId, userId, lastName, firstName, role, patients);
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

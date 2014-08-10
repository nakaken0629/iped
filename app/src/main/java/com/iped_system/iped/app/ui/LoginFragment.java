package com.iped_system.iped.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.app.network.ApiAsyncTaskLoader;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.LoginRequest;
import com.iped_system.iped.common.LoginResponse;
import com.iped_system.iped.common.ResponseStatus;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();

    private LoginCallbacks loginCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        this.loginCallbacks = new LoginCallbacks();

        Button loginButton = (Button) rootView.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginButtonListener());
        return rootView;
    }

    class LoginButtonListener implements View.OnClickListener {
        private String getEditTextValue(int id) {
            return ((EditText) getView().findViewById(id)).getText().toString().trim();
        }

        @Override
        public void onClick(View view) {
            String userId = getEditTextValue(R.id.userIdEditText);
            String password = getEditTextValue(R.id.passwordEditText);

            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            bundle.putString("password", "password");
            LoginFragment self = LoginFragment.this;
            LoaderManager manager = self.getLoaderManager();
            self.getLoaderManager().initLoader(0, bundle, self.loginCallbacks);
        }
    }

    class LoginCallbacks implements LoaderManager.LoaderCallbacks<BaseResponse> {

        @Override
        public Loader<BaseResponse> onCreateLoader(int i, Bundle bundle) {
            Context context = getActivity().getApplicationContext();
            LoginRequest request = new LoginRequest();
            request.setUserId(bundle.getString("userId"));
            request.setPassword(bundle.getString("password"));

            ApiAsyncTaskLoader loader = new ApiAsyncTaskLoader(context, request, "login", false);
            loader.forceLoad();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<BaseResponse> baseResponseLoader, BaseResponse baseResponse) {
        /* TODO: このキャストをなくせないか？ */
            LoginResponse response = (LoginResponse) baseResponse;
            if (response.getStatus() == ResponseStatus.SUCCESS) {
            /* TODO: 本当はActivityに通知する実装が良い */
                Activity activity = getActivity();
                IpedApplication application = (IpedApplication) activity.getApplication();
                application.authenticate(response);
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                AlertDialog dialog = builder.setTitle("メッセージ")
                        .setMessage("ユーザ名かパスワードが正しくありません")
                        .setPositiveButton("確認", null)
                        .create();
                dialog.show();
            }
        }

        @Override
        public void onLoaderReset(Loader<BaseResponse> baseResponseLoader) {
            /* nop */
        }
    }
}

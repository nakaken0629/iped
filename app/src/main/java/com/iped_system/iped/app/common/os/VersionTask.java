package com.iped_system.iped.app.common.os;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.iped_system.iped.common.login.VersionRequest;
import com.iped_system.iped.common.login.VersionResponse;

/**
 * Created by kenji on 2014/12/17.
 */
public abstract class VersionTask extends ApiAsyncTask<VersionRequest, VersionResponse> {
    private static final String TAG = VersionTask.class.getName();

    public VersionTask(Activity activity) {
        super(activity);
    }

    @Override
    protected boolean isSecure() {
        return false;
    }

    @Override
    protected String getApiName() {
        return "version";
    }

    protected int getVersionCode() {
        PackageManager pm = getActivity().getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getActivity().getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "バージョン番号を取得できませんでした", e);
        }
        return versionCode;
    }
}

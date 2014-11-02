package com.iped_system.iped.app.common.os;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.utils.IoUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by kenji on 2014/11/02.
 */
public class UpdateAsyncTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = UpdateAsyncTask.class.getName();
    private final String downloadPath = Environment.getExternalStorageDirectory() + "/download/iped/";
    private static final String APK_NAME = "iped.apk";
    private WeakReference<Activity> activityRef;

    public UpdateAsyncTask(Activity activity) {
        this.activityRef = new WeakReference<Activity>(activity);
    }

    @Override
    protected Void doInBackground(String[] urls) {
        try {
            return doInBackgroundInner(urls);
        } catch (IOException e) {
            Log.e(TAG, "update error", e);
            cancel(true);
            return null;
        }
    }

    private Void doInBackgroundInner(String[] urls) throws IOException {
        Log.d(TAG, "url = " + urls[0]);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(urls[0]);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();

        File download = new File(this.downloadPath);
        download.mkdir();
        File apk = new File(downloadPath, APK_NAME);
        FileOutputStream outputStream = new FileOutputStream(apk);
        IoUtils.copyStream(entity.getContent(), outputStream, null);
        outputStream.close();
        client.getConnectionManager().shutdown();

        return null;
    }

    @Override
    protected void onCancelled() {
        Activity activity = this.activityRef.get();
        if (activity == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog dialog = builder.setTitle("メッセージ")
                .setMessage("アプリの更新に失敗しました。")
                .setPositiveButton("確認", null)
                .create();
        dialog.show();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Activity activity = this.activityRef.get();
        if (activity == null) {
            return;
        }

        Log.d(TAG, "downloadPath = " + this.downloadPath + ", APK_NAME = " + APK_NAME);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(this.downloadPath, APK_NAME)), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }
}

package com.iped_system.iped.app.common.os;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.ResponseStatus;

import net.arnx.jsonic.JSON;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by kenji on 2014/09/19.
 */
public abstract class ApiAsyncTask<T1 extends BaseRequest, T2 extends BaseResponse> extends AsyncTask<T1, Void, T2> {
    private static final String TAG = ApiAsyncTask.class.getName();

    private long tokenId;
    private String baseUrl;
    private WeakReference<Activity> activityRef;
    private String patientId;
    private int httpStatus;

    private boolean isConnected = true;

    public ApiAsyncTask(Activity activity) {
        IpedApplication application = (IpedApplication) activity.getApplication();
        this.tokenId = application.getTokenId();
        this.baseUrl = activity.getString(R.string.server_baseurl);
        this.activityRef = new WeakReference<Activity>(activity);
        if (isSecure()) {
            this.patientId = application.getPatientId();
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && cm.getActiveNetworkInfo().isConnected();
    }

    protected boolean isSecure() {
        return true;
    }

    protected abstract String getApiName();

    protected Activity getActivity() {
        return this.activityRef.get();
    }

    private String getPath() {
        return "/api" + (this.isSecure() ? "/secure/" : "/") + this.getApiName();
    }

    @Override
    protected T2 doInBackground(T1... requests) {
        if (!this.isConnected(this.activityRef.get())) {
            this.isConnected = false;
            return null;
        }

        try {
            return doInBackgroundInner(requests);
        } catch (IOException e) {
            return null;
        }
    }

    private T2 doInBackgroundInner(T1... requests) throws IOException {
        T1 request = requests[0];
        HttpClient client = new DefaultHttpClient();
        String url = this.baseUrl + getPath();
        Log.d(TAG, "url: " + url);
        HttpPost post = new HttpPost(url);
        if (this.isSecure()) {
            post.setHeader("X-IPED-TOKEN-ID", Long.toString(this.tokenId));
            post.setHeader("X-IPED-PATIENT-ID", this.patientId);
        }

        /* prepare parameters */
        ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("parameter", request.toJSON()));
        post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));

        /* access server */
        HttpResponse response = client.execute(post);
        this.httpStatus = response.getStatusLine().getStatusCode();
        Log.d(TAG, "httpStatus : " + this.httpStatus);
        if (this.httpStatus == HttpStatus.SC_UNAUTHORIZED) {
            return null;
        }
        InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
        BaseResponse baseResponse = BaseResponse.fromJSON(reader, request.getResponseClass());
        Log.d(TAG, "response: " + JSON.encode(baseResponse));
        return (T2) baseResponse;
    }

    @Override
    protected final void onPostExecute(T2 t2) {
        Activity activity = this.activityRef.get();
        if (activity == null) {
            return;
        }
        if (!this.isConnected) {
            onDisconnected();
            return;
        } else if (this.httpStatus == HttpStatus.SC_UNAUTHORIZED) {
            onExpireToken();
            return;
        } else if (t2.getStatus() == ResponseStatus.SUCCESS) {
            onPostExecuteOnSuccess(t2);
            return;
        } else {
            onPostExecuteOnFailure(t2);
            return;
        }
    }

    protected void onDisconnected() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setTitle("メッセージ")
                .setMessage("インターネットにつながっていません。")
                .setPositiveButton("確認", null)
                .create();
        dialog.show();
    }

    protected abstract void onPostExecuteOnSuccess(T2 t2);

    protected void onPostExecuteOnFailure(T2 t2) {
        /* nop */
    }

    protected void onExpireToken() {
        Log.d(TAG, "call onExpireToken");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setTitle("メッセージ")
                .setMessage("時間が空いたので、ログイン画面からやり直してください")
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                }).create();
        dialog.show();
    }
}

package com.iped_system.iped.app.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;

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
import java.util.ArrayList;

/**
 * Created by kenji on 2014/09/19.
 */
public abstract class ApiAsyncTask<T1 extends BaseRequest, T2 extends BaseResponse> extends AsyncTask<T1, Void, T2> {
    private static final String TAG = ApiAsyncTask.class.getName();

    private Context context;

    public ApiAsyncTask(Context context) {
        this.context = context;
    }

    protected abstract boolean isSecure();

    protected abstract String getApiName();

    private String getPath() {
        return "/api" + (this.isSecure() ? "/secure/" : "/") + this.getApiName();
    }

    @Override
    protected T2 doInBackground(T1... requests) {
        try {
            return doInBackgroundInner(requests);
        } catch (IOException e) {
            cancel(true);
            return null;
        }
    }

    private T2 doInBackgroundInner(T1... requests) throws IOException {
        T1 request = requests[0];
        HttpClient client = new DefaultHttpClient();
        String url = this.context.getString(R.string.server_baseurl) + getPath();
        Log.d(TAG, "url: " + url);
        HttpPost post = new HttpPost(url);
        if (this.isSecure()) {
            IpedApplication application = (IpedApplication) this.context.getApplicationContext();
            post.setHeader("X-IPED-USER-ID", application.getUserId());
            post.setHeader("X-IPED-TOKEN-ID", Long.toString(application.getTokenId()));
        }

        /* prepare parameters */
        ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("parameter", request.toJSON()));
        post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));

        /* access server */
        HttpResponse response = client.execute(post);
        int status = response.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_OK) {
            throw new IOException("network error: " + response.getStatusLine().toString());
        }
        InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
        BaseResponse baseResponse = BaseResponse.fromJSON(reader, request.getResponseClass());
        Log.d(TAG, "response: " + JSON.encode(baseResponse));
        return (T2) baseResponse;
    }
}

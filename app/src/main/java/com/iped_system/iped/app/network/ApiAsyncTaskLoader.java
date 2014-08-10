package com.iped_system.iped.app.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;

import net.arnx.jsonic.JSON;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by kenji on 2014/08/09.
 */
public class ApiAsyncTaskLoader extends AsyncTaskLoader<BaseResponse> {
    private static final String TAG = ApiAsyncTaskLoader.class.getName();

    private BaseRequest request;
    private String apiName;
    private boolean isSecure;

    public ApiAsyncTaskLoader(Context context, BaseRequest request, String apiName, boolean isSecure) {
        super(context);
        this.request = request;
        this.apiName = apiName;
        this.isSecure = isSecure;
    }

    @Override
    public BaseResponse loadInBackground() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://10.0.2.2:8080/api/" + getPath());
            connection = (HttpURLConnection) url.openConnection();
            return doNetworkAccess(connection);
        } catch (IOException e) {
            Log.e(TAG, "network error", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String getPath() {
        if (this.isSecure) {
            return "secure/" + this.apiName;
        } else {
            return this.apiName;
        }
    }

    private BaseResponse doNetworkAccess(HttpURLConnection connection) throws IOException {
        /* prepare a connection */
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");

        /* prepare parameters */
        ArrayList<String> parameters = new ArrayList<String>();
        if (this.isSecure) {
            IpedApplication application = (IpedApplication) getContext().getApplicationContext();
            connection.setRequestProperty("X-IPED-USER-ID", application.getUserId());
            connection.setRequestProperty("X-IPED-TOKEN-ID", Long.toString(application.getTokenId()));
        }
        parameters.add("parameter=" + URLEncoder.encode(this.request.toJSON(), "utf-8"));
        PrintWriter writer = new PrintWriter(connection.getOutputStream());
        StringBuilder builder = new StringBuilder();
        for (String parameter : parameters) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(parameter);
        }
        writer.print(builder.toString());
        writer.close();
        Log.d(TAG, "request: " + builder.toString());

        /* access server */
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BaseResponse response = BaseResponse.fromJSON(reader, this.request.getResponseClass());
        Log.d(TAG, "response: " + JSON.encode(response));
        return response;
    }
}

//package com.iped_system.iped.app.network;
//
//import android.content.Context;
//import android.support.v4.content.AsyncTaskLoader;
//import android.util.Log;
//
//import com.iped_system.iped.R;
//import com.iped_system.iped.app.IpedApplication;
//import com.iped_system.iped.common.BaseRequest;
//import com.iped_system.iped.common.BaseResponse;
//
//import net.arnx.jsonic.JSON;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
///**
// * Created by kenji on 2014/08/09.
// */
//public class ApiAsyncTaskLoader extends AsyncTaskLoader<BaseResponse> {
//    private static final String TAG = ApiAsyncTaskLoader.class.getName();
//
//    private BaseRequest request;
//    private String apiName;
//    private boolean isSecure;
//
//    public ApiAsyncTaskLoader(Context context, BaseRequest request, String apiName, boolean isSecure) {
//        super(context);
//        this.request = request;
//        this.apiName = apiName;
//        this.isSecure = isSecure;
//    }
//
//    @Override
//    public BaseResponse loadInBackground() {
//        try {
//            return doNetworkAccess();
//        } catch (IOException e) {
//            Log.e(TAG, "network error: " + e.toString(), e);
//            throw new RuntimeException("通信エラーが発生しました", e);
//        }
//    }
//
//    private String getPath() {
//        return "/api" + (this.isSecure ? "/secure/" : "/") + this.apiName;
//    }
//
//    private BaseResponse doNetworkAccess() throws IOException {
//        HttpClient client = new DefaultHttpClient();
//        String url = getContext().getString(R.string.server_baseurl) + getPath();
//        Log.d(TAG, "url: " + url);
//        HttpPost post = new HttpPost(url);
//        if (this.isSecure) {
//            IpedApplication application = (IpedApplication) getContext().getApplicationContext();
//            post.setHeader("X-IPED-USER-ID", application.getUserId());
//            post.setHeader("X-IPED-TOKEN-ID", Long.toString(application.getTokenId()));
//        }
//
//        /* prepare parameters */
//        ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
//        parameters.add(new BasicNameValuePair("parameter", this.request.toJSON()));
//        post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
//
//        /* access server */
//        HttpResponse response = client.execute(post);
//        int status = response.getStatusLine().getStatusCode();
//        if (status != HttpStatus.SC_OK) {
//            throw new IOException("network error: " + response.getStatusLine().toString());
//        }
//        InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
//        BaseResponse baseResponse = BaseResponse.fromJSON(reader, this.request.getResponseClass());
//        Log.d(TAG, "response: " + JSON.encode(baseResponse));
//        return baseResponse;
//    }
//}

package com.iped_system.iped.app.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kenji on 2014/08/09.
 */
public class UploadAsyncTaskLoader extends AsyncTaskLoader<Void> {
    private static final String TAG = UploadAsyncTaskLoader.class.getName();

    private String pictureName;
    private byte[] pictureData;

    public UploadAsyncTaskLoader(Context context, String pictureName, byte[] pictureData) {
        super(context);
        this.pictureName = pictureName;
        this.pictureData = pictureData;
    }

    @Override
    public Void loadInBackground() {
        try {
            return doNetworkAccess();
        } catch (IOException e) {
            Log.e(TAG, "network error: " + e.toString(), e);
            throw new RuntimeException("通信エラーが発生しました", e);
        }
    }

    private String getPath() throws IOException {
        HttpClient client = new DefaultHttpClient();
//        String url = "http://10.0.2.2:8080/api/getPostUrl";
        String url = "http://192.168.11.103:8080/api/getPostUrl";
//        String url = "http://ipedsystem.appspot.com/api/getPostUrl";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder builder = new StringBuilder();
        while(true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            builder.append(line);
            builder.append("¥n");
        }
        return builder.toString();
    }

    private Void doNetworkAccess() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(getPath());

        /* add picture */
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody(this.pictureName, this.pictureData, ContentType.create("image/jpeg"), this.pictureName);
        post.setEntity(builder.build());

        /* access server */
        HttpResponse response = client.execute(post);
        int status = response.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_OK) {
            throw new IOException("network error: " + response.getStatusLine().toString());
        }
        return null;
    }
}

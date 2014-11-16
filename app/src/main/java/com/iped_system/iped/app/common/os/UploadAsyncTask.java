package com.iped_system.iped.app.common.os;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.app.main.Picture;
import com.iped_system.iped.common.ResponseStatus;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kenji on 2014/08/09.
 */
public abstract class UploadAsyncTask extends AsyncTask<Picture, Void, List<Long>> {
    private static final String TAG = UploadAsyncTask.class.getName();

    private long tokenId;
    private String url;
    private WeakReference<Activity> activityRef;

    public UploadAsyncTask(Activity activity) {
        IpedApplication application = (IpedApplication) activity.getApplication();
        this.tokenId = application.getTokenId();
        this.url = activity.getString(R.string.server_baseurl) + "/api/secure/getPostUrl";
        this.activityRef = new WeakReference<Activity>(activity);
    }

    private String getPath() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(this.url);
        get.setHeader("X-IPED-TOKEN-ID", Long.toString(this.tokenId));
        HttpResponse response = client.execute(get);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder builder = new StringBuilder();
        while(true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(line);
        }
        return builder.toString();
    }

    @Override
    protected List<Long> doInBackground(Picture... pictures) {
        try {
            return doInBackgroundInner(pictures);
        } catch (IOException e) {
            Log.e(TAG, "network error: " + e.toString(), e);
            throw new RuntimeException("通信エラーが発生しました", e);
        }
    }

    private List<Long> doInBackgroundInner(Picture[] pictures) throws IOException {
        HttpClient client = new DefaultHttpClient();
        ArrayList<Long> result = new ArrayList<Long>();

        for(Picture picture : pictures) {
            HttpPost post = new HttpPost(getPath());

            /* add picture */
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.getDisplayBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            String filename = "p" + Long.toString(new Date().getTime()) + ".jpg";
            builder.addBinaryBody("myFile", stream.toByteArray(), ContentType.create("image/jpeg"), filename);
            post.setEntity(builder.build());

            /* access server */
            HttpResponse response = client.execute(post);
            int status = response.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
                throw new IOException("network error: " + response.getStatusLine().toString());
            }

            HttpEntity entity = response.getEntity();
            String entityString = EntityUtils.toString(entity);
            result.add(Long.parseLong(entityString));
        }
        return result;
    }

    @Override
    protected final void onPostExecute(List<Long> pictureIdList) {
        Activity activity = this.activityRef.get();
        if (activity == null) {
            return;
        }
        onPostExecuteOnSuccess(pictureIdList);
    }

    protected abstract void onPostExecuteOnSuccess(List<Long> pictureIdList);
}

package com.iped_system.iped.app.common.os;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.app.RetainFragment;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kenji on 2014/10/13.
 */
public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = ImageAsyncTask.class.getName();

    private String baseUrl;
    private WeakReference<ImageView> imageViewRef;
    private String faceKey;
    private RetainFragment retainFragment;

    public ImageAsyncTask(Context context, ImageView imageView, RetainFragment retainFragment) {
        this.baseUrl = context.getString(R.string.server_baseurl);
        this.imageViewRef = new WeakReference<ImageView>(imageView);
        this.retainFragment = retainFragment;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            return doInBackgroundInner(strings);
        } catch (MalformedURLException e) {
            Log.e(TAG, "error", e);
        } catch (IOException e) {
            Log.e(TAG, "error", e);
        }
        cancel(true);
        return null;
    }

    private Bitmap doInBackgroundInner(String... strings) throws MalformedURLException, IOException {
        this.faceKey = strings[0];
        if (faceKey == null) {
            cancel(true);
            return null;
        }
        Bitmap bitmap = this.retainFragment.getBitmapFromMemCache(faceKey);
        if (bitmap != null) {
            return bitmap;
        }

        String faceUrl = this.baseUrl + "/api/face/" + this.faceKey;
        InputStream stream = null;
        try {
            stream = new URL(faceUrl).openStream();
            bitmap = BitmapFactory.decodeStream(stream);
            this.retainFragment.addBitmapToMemoryCache(faceKey, bitmap);
            return bitmap;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    /* ignore */
                }
            }
        }
    }

    private boolean isAlive(ImageView imageView) {
        if (imageView == null) {
            return false;
        }
        if (this.faceKey == null || !this.faceKey.equals(imageView.getTag())) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.d(TAG, "onPostExecute");
        ImageView imageView = this.imageViewRef.get();
        if (isAlive(imageView)) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCancelled() {
        Log.d(TAG, "onCancelled");
        ImageView imageView = this.imageViewRef.get();
        if (isAlive(imageView)) {
            imageView.setImageResource(R.drawable.anonymous);
        }
    }
}

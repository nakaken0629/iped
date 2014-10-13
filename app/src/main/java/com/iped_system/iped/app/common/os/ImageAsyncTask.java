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

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by kenji on 2014/10/13.
 */
public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = ImageAsyncTask.class.getName();
    private static LruCache<String, Bitmap> imageCache;

    static {
        final int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imageCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return imageCache.get(key);
    }

    private String baseUrl;
    private WeakReference<ImageView> imageViewRef;
    private String faceKey;

    public ImageAsyncTask(Context context, ImageView imageView) {
        this.baseUrl = context.getString(R.string.server_baseurl);
        this.imageViewRef = new WeakReference<ImageView>(imageView);
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
        Bitmap bitmap = getBitmapFromMemCache(faceKey);
        if (bitmap != null) {
            return bitmap;
        }

        String faceUrl = this.baseUrl + "/api/face/" + this.faceKey;
        InputStream stream = null;
        try {
            stream = new URL(faceUrl).openStream();
            bitmap = BitmapFactory.decodeStream(stream);
            addBitmapToMemoryCache(faceKey, bitmap);
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
        ImageView imageView = this.imageViewRef.get();
        if (isAlive(imageView)) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCancelled() {
        ImageView imageView = this.imageViewRef.get();
        if (isAlive(imageView)) {
            imageView.setImageResource(R.drawable.anonymous);
        }
    }
}

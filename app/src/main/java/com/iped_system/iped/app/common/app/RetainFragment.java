package com.iped_system.iped.app.common.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.LruCache;

/**
 * Created by kenji on 2014/10/15.
 */
public class RetainFragment extends Fragment {
    private static final String TAG = RetainFragment.class.getName();
    private LruCache cache;

    public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
        RetainFragment fragment = (RetainFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RetainFragment();
            fm.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        final int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemory / 8;
        this.cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            this.cache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return (Bitmap)this.cache.get(key);
    }
}

package com.iped_system.iped.app.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.Serializable;

/**
* Created by kenji on 2014/10/11.
*/
public class Picture implements Serializable {
    private byte[] original;
    private Bitmap displayBitmap;
    private Bitmap thumbnailBitmap;
    private boolean isRotate;

    public Picture(byte[] original) {
        this(original, true);
    }

    public Picture(byte[] original, boolean isRotate) {
        this.original = original;
        this.displayBitmap = convertTo(original, 800);
        this.thumbnailBitmap = convertTo(original, 128);
        this.isRotate = isRotate;
    }

    private Bitmap convertTo(byte[] original, int size) {
        /* check size */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(original, 0, original.length, options);
        int width = options.outWidth;
        int height = options.outHeight;
        int max = Math.max(width, height);
        if (size < max) {
            options.inSampleSize = max / size + 1;
        }

        /* create bitmap */
        options.inJustDecodeBounds = false;
        Bitmap workBitmap = BitmapFactory.decodeByteArray(original, 0, original.length, options);
        Matrix matrix = new Matrix();
        if (this.isRotate) {
            matrix.postRotate(180);
        }
        return Bitmap.createBitmap(workBitmap, 0, 0, workBitmap.getWidth(), workBitmap.getHeight(), matrix, false);
    }

    public Bitmap getDisplayBitmap() {
        return this.displayBitmap;
    }

    public Bitmap getThumbnailBitmap() {
        return this.thumbnailBitmap;
    }
}

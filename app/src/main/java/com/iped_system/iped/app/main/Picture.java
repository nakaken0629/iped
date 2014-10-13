package com.iped_system.iped.app.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
* Created by kenji on 2014/10/11.
*/
class Picture implements Serializable {
    private byte[] original;
    private Bitmap displayBitmap;
    private Bitmap thumbnailBitmap;

    Picture(byte[] original) {
        this.original = original;
        this.displayBitmap = convertToDisplay(original);
        this.thumbnailBitmap = convertToThumbnail(original);
    }

    Bitmap getDisplayBitmap() {
        return this.displayBitmap;
    }

    Bitmap getThumbnailBitmap() {
        return this.thumbnailBitmap;
    }

    private Bitmap convertToDisplay(byte[] original) {
        return convertTo(original, 800);
    }

    private Bitmap convertToThumbnail(byte[] original) {
        return convertTo(original, 200);
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
        return Bitmap.createBitmap(workBitmap, 0, 0, workBitmap.getWidth(), workBitmap.getHeight(), null, false);
    }

    private byte[] getDisplay() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            this.displayBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baos.flush();
        } catch (IOException e) {
            /* nop */
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                /* nop */
            }
        }
        return baos.toByteArray();
    }
}
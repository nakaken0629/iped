package com.iped_system.iped.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.network.ApiAsyncTaskLoader;
import com.iped_system.iped.app.network.UploadAsyncTaskLoader;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RemarksNewRequest;
import com.iped_system.iped.common.RemarksNewResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RemarkFragment extends DialogFragment {
    private static final String TAG = RemarkFragment.class.getName();
    private static final int LOADER_PICTURE = 0;
    private static final int LOADER_REMARK = 1;

    /* TODO: ライフサイクルによってなくなってしまわないか？ */
    private ArrayList<Picture> pictures = new ArrayList<Picture>();

    public interface OnRegisterListener {
        public void onRegister();
    }

    private PictureUploadCallbacks pictureUploadCallbacks;
    private RemarkNewCallbacks remarkNewCallbacks;

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

        byte[] getDisplay() {
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

    public static RemarkFragment newInstance(Fragment fragment) {
        RemarkFragment remarkFragment = new RemarkFragment();
        remarkFragment.setTargetFragment(fragment, 0);
        return remarkFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_remark, container, false);
        this.remarkNewCallbacks = new RemarkNewCallbacks();
        getLoaderManager().initLoader(LOADER_REMARK, null, this.remarkNewCallbacks);
        this.pictureUploadCallbacks = new PictureUploadCallbacks();
        getLoaderManager().initLoader(LOADER_PICTURE, null, this.pictureUploadCallbacks);

        Bundle args = getArguments();
        if (args.containsKey("pictures")) {
            Picture picture = new Picture(args.getByteArray("pictures"));
            ViewGroup thumbnailLayout = (ViewGroup) rootView.findViewById(R.id.thumbnailLayout);
            ImageView thumbnailImageView = (ImageView) inflater.inflate(R.layout.thumbnail, thumbnailLayout, false);
            thumbnailImageView.setImageBitmap(picture.thumbnailBitmap);
            thumbnailLayout.addView(thumbnailImageView);
            this.pictures.add(picture);
        }

        Button remarkButton = (Button) rootView.findViewById(R.id.remarkButton);
        remarkButton.setOnClickListener(new RemarkButtonListener());
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        return dialog;
    }

    class RemarkButtonListener implements View.OnClickListener {
        private String getEditTextValue(int id) {
            return ((EditText) getView().findViewById(id)).getText().toString().trim();
        }

        @Override
        public void onClick(View view) {
            String text = getEditTextValue(R.id.remarkEditText);
            if (text == null || text.length() == 0) {
                return;
            }

            RemarkFragment self = RemarkFragment.this;
            Bundle bundle = new Bundle();
            bundle.putString("text", text);
            if (self.pictures.size() == 0) {
                self.getLoaderManager().restartLoader(LOADER_REMARK, bundle, self.remarkNewCallbacks);
            } else {
                bundle.putInt("pictureCount", self.pictures.size());
                bundle.putString("picturePath", UUID.randomUUID().toString());
                bundle.putByteArray("picture", pictures.get(0).getDisplay());
                self.getLoaderManager().restartLoader(LOADER_PICTURE, bundle, self.pictureUploadCallbacks);
            }
        }
    }

    class PictureUploadCallbacks implements LoaderManager.LoaderCallbacks<List<String>> {
        private Bundle bundle;

        @Override
        public Loader<List<String>> onCreateLoader(int i, Bundle bundle) {
            this.bundle = bundle;
            Context context = getActivity().getApplicationContext();
            String picturePath = bundle.getString("picturePath");
            byte[] pictureData = bundle.getByteArray("pictures");
            Log.d(TAG, "picturePath: " + picturePath);
            UploadAsyncTaskLoader loader = new UploadAsyncTaskLoader(context, picturePath, pictureData);
            loader.forceLoad();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<List<String>> listLoader, List<String> strings) {
            bundle.putStringArrayList("pictures", new ArrayList<String>(strings));
            getLoaderManager().restartLoader(0, bundle, remarkNewCallbacks);
        }

        @Override
        public void onLoaderReset(Loader<List<String>> listLoader) {
            /* nop */
        }
    }

    class RemarkNewCallbacks implements LoaderManager.LoaderCallbacks<BaseResponse> {

        @Override
        public Loader<BaseResponse> onCreateLoader(int i, Bundle bundle) {
            Context context = getActivity().getApplicationContext();
            RemarksNewRequest request = new RemarksNewRequest();
            request.setAuthorName(bundle.getString("authorName"));
            request.setText(bundle.getString("text"));
            request.setPictures(bundle.getStringArrayList("pictures"));
            ApiAsyncTaskLoader loader = new ApiAsyncTaskLoader(context, request, "remarks/new", true);
            loader.forceLoad();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<BaseResponse> baseResponseLoader, BaseResponse baseResponse) {
            final RemarksNewResponse response = (RemarksNewResponse) baseResponse;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Fragment fragment = getTargetFragment();
                    if (fragment instanceof OnRegisterListener) {
                        ((OnRegisterListener) fragment).onRegister();
                    }
                    RemarkFragment.this.dismiss();
                }
            });
        }

        @Override
        public void onLoaderReset(Loader<BaseResponse> baseResponseLoader) {
            /* nop */
        }
    }
}

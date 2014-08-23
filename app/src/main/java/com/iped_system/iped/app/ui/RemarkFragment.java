package com.iped_system.iped.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RemarkFragment extends DialogFragment {
    private static final String TAG = RemarkFragment.class.getName();
    /* TODO: ライフサイクルによってなくなってしまわないか？ */
    private Date lastUpdate;
    private byte[] pictureData;

    public interface OnRegisterListener {
        public void onRegister(RemarksNewResponse response);
    }

    private RemarksNewCallbacks remarksNewCallbacks;
    private PictureUploadCallbacks pictureUploadCallbacks;

    public static RemarkFragment newInstance(Fragment fragment) {
        RemarkFragment remarkFragment = new RemarkFragment();
        remarkFragment.setTargetFragment(fragment, 0);
        return remarkFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_remark, container, false);
        this.remarksNewCallbacks = new RemarksNewCallbacks();
        this.pictureUploadCallbacks = new PictureUploadCallbacks();

        Bundle args = getArguments();
        this.lastUpdate = (Date) args.getSerializable("lastUpdate");
        if (args.containsKey("pictureData")) {
            this.pictureData = args.getByteArray("pictureData");
            /* check size */
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(this.pictureData, 0, this.pictureData.length, options);
            int width = options.outWidth;
            int height = options.outHeight;
            int max = Math.max(width, height);
            if (800 < max) {
                int scale = max / 800 + 1;
                options.inSampleSize = scale;
            }

            /* create bitmap */
            options.inJustDecodeBounds = false;
            Bitmap workBitmap = BitmapFactory.decodeByteArray(this.pictureData, 0, this.pictureData.length, options);
            Matrix matrix = new Matrix();
            matrix.setRotate(270);
            Bitmap picture = Bitmap.createBitmap(workBitmap, 0, 0, workBitmap.getWidth(), workBitmap.getHeight(), matrix, false);
            ViewGroup thumbnailLayout = (ViewGroup) rootView.findViewById(R.id.thumbnailLayout);
            ImageView thumbnailImageView = (ImageView) inflater.inflate(R.layout.thumbnail, thumbnailLayout, false);
            thumbnailImageView.setImageBitmap(picture);
            thumbnailLayout.addView(thumbnailImageView);
        }

        Button messageButton = (Button) rootView.findViewById(R.id.remarkButton);
        messageButton.setOnClickListener(new RemarkButtonListener());
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

    private byte[] getPictureData() {
        return RemarkFragment.this.pictureData;
    }

    class RemarkButtonListener implements View.OnClickListener {
        private String getEditTextValue(int id) {
            return ((EditText) getView().findViewById(id)).getText().toString().trim();
        }

        @Override
        public void onClick(View view) {
            String text = getEditTextValue(R.id.remarkEditText);
            Log.d(TAG, "text: " + text);
            if (text == null || text.length() == 0) {
                return;
            }

            RemarkFragment self = RemarkFragment.this;
            Bundle bundle = new Bundle();
            bundle.putString("text", text);
            byte[] pictureData = self.getPictureData();
            if (pictureData == null) {
                self.getLoaderManager().restartLoader(0, bundle, self.remarksNewCallbacks);
            } else {
                bundle.putString("picturePath", UUID.randomUUID().toString());
                bundle.putByteArray("pictureData", pictureData);
                self.getLoaderManager().restartLoader(0, bundle, self.pictureUploadCallbacks);
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
            byte[] pictureData = bundle.getByteArray("pictureData");
            Log.d(TAG, "picturePath: " + picturePath);
            UploadAsyncTaskLoader loader = new UploadAsyncTaskLoader(context, picturePath, pictureData);
            loader.forceLoad();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<List<String>> listLoader, List<String> strings) {
            bundle.putStringArrayList("pictures", new ArrayList<String>(strings));
            getLoaderManager().restartLoader(0, bundle, remarksNewCallbacks);
        }

        @Override
        public void onLoaderReset(Loader<List<String>> listLoader) {
            /* nop */
        }
    }

    class RemarksNewCallbacks implements LoaderManager.LoaderCallbacks<BaseResponse> {

        @Override
        public Loader<BaseResponse> onCreateLoader(int i, Bundle bundle) {
            Context context = getActivity().getApplicationContext();
            RemarksNewRequest request = new RemarksNewRequest();
            request.setLastUpdate(lastUpdate);
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
                        ((OnRegisterListener) fragment).onRegister(response);
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

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
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RemarksNewRequest;
import com.iped_system.iped.common.RemarksNewResponse;

import java.util.Date;

public class RemarkFragment extends DialogFragment {
    private static final String TAG = RemarkFragment.class.getName();
    /* TODO: ライフサイクルによってなくなってしまわないか？ */
    private Date lastUpdate;

    public interface OnRegisterListener {
        public void onRegister(RemarksNewResponse response);
    }

    private RemarksNewCallbacks remarksNewCallbacks;

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
        Bundle args = getArguments();
        this.lastUpdate = (Date) args.getSerializable("lastUpdate");
        if (args.containsKey("picture")) {
            byte[] bytes = args.getByteArray("picture");
            Bitmap workBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Matrix matrix = new Matrix();
            matrix.setRotate(270);
            Bitmap bitmap = Bitmap.createBitmap(workBitmap, 0, 0, workBitmap.getWidth(), workBitmap.getHeight(), matrix, false);
            workBitmap.recycle();
            workBitmap = null;
            ViewGroup thumbnailLayout = (ViewGroup) rootView.findViewById(R.id.thumbnailLayout);
            ImageView thumbnailImageView = (ImageView) inflater.inflate(R.layout.thumbnail, thumbnailLayout, false);
            thumbnailImageView.setImageBitmap(bitmap);
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

            Bundle bundle = new Bundle();
            bundle.putString("text", text);
            RemarkFragment self = RemarkFragment.this;
            self.getLoaderManager().restartLoader(0, bundle, self.remarksNewCallbacks);
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

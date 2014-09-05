package com.iped_system.iped.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.iped_system.iped.R;

import java.io.IOException;
import java.util.List;

/**
 * This source has been created with reference to the below link.
 * http://kurotofu.sytes.net/kanji/fool/?p=694
 */
public class CameraFragment extends DialogFragment {
    private static final String TAG = CameraFragment.class.getName();

    private Camera camera;

    public interface OnTakePictureListener {
        public void onTakePicture(byte[] bitmapBytes);
    }

    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    camera = Camera.open(i);
                    break;
                }
            }
            if (camera == null) {
                camera = Camera.open(0);
            }
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                Log.e(TAG, "camera error", e);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            camera.stopPreview();
            Camera.Parameters parameters = camera.getParameters();

            /* set orientation */
            boolean portrait = isPortrait();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                /* 2.1 and before */
                parameters.set("orientation", portrait ? "portrait" : "landscape");
            } else {
                /* 2.2 and later */
                camera.setDisplayOrientation(portrait ? 90 : 0);
            }

            /* Set width & height */
            int previewWidth = (portrait ? height : width);
            int previewHeight = (portrait ? width : height);

            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            int tmpHeight = 0;
            int tmpWidth = 0;
            for (Camera.Size size : sizes) {
                if ((size.width > previewWidth) || (size.height > previewHeight)) {
                    continue;
                }
                if (tmpHeight < size.height) {
                    tmpWidth = size.width;
                    tmpHeight = size.height;
                }
            }
            previewWidth = tmpWidth;
            previewHeight = tmpHeight;

            parameters.setPreviewSize(previewWidth, previewHeight);

            /* Adjust SurfaceView size */
            ViewGroup.LayoutParams layoutParams = getView().getLayoutParams();
            float layoutWidth = (portrait ? previewHeight : previewWidth);
            float layoutHeight = (portrait ? previewWidth : previewHeight);
            float factW = width / layoutWidth;
            float factH = height / layoutHeight;
            /* Select smaller factor, because the surface cannot be set to the size larger than display metrics. */
            float fact = (factH < factW ? factH : factW);
            layoutParams.width = (int) (layoutWidth * fact);
            layoutParams.height = (int) (layoutHeight * fact);
            getView().setLayoutParams(layoutParams);

            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            camera.release();
            camera = null;
        }
    };

    public static CameraFragment newInstance(Fragment fragment) {
        if (!(fragment instanceof OnTakePictureListener)) {
            throw new ClassCastException();
        }
        CameraFragment cameraFragment = new CameraFragment();
        cameraFragment.setTargetFragment(fragment, 0);
        return cameraFragment;
    }

    protected boolean isPortrait() {
        Configuration configuration = this.getResources().getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        /* camera */
        SurfaceView cameraSurfaceView = (SurfaceView) rootView.findViewById(R.id.cameraSurfaceView);
        SurfaceHolder holder = cameraSurfaceView.getHolder();
        holder.addCallback(surfaceListener);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        /* shutter */
        Button shutterButton = (Button) rootView.findViewById(R.id.shutterButton);
        shutterButton.setOnClickListener(new TakePictureListener());

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        );
        return dialog;
    }

    class TakePictureListener implements View.OnClickListener {
        private MyShutterCallback shutterCallback;
        private JpegPictureCallback jpegPictureCallback;

        TakePictureListener() {
            this.shutterCallback = new MyShutterCallback();
            this.jpegPictureCallback = new JpegPictureCallback();
        }

        @Override
        public void onClick(View view) {
            if (camera == null) {
                return;
            }
            camera.takePicture(shutterCallback, null, jpegPictureCallback);
        }
    }

    class MyShutterCallback implements Camera.ShutterCallback {
        @Override
        public void onShutter() {
            /* Use handler cause continuing play unless CameraFragment is dismissed */
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    AudioManager mgr = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                    mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
                }
            });
        }
    }

    class JpegPictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            final byte[] bitmapBytes = bytes;

            /* TODO: CameraFragmentを閉じるタイミングとRemarkFragmentを閉じるタイミングに問題が無いか？ */
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Fragment fragment = getTargetFragment();
                    if (fragment instanceof OnTakePictureListener) {
                        ((OnTakePictureListener) fragment).onTakePicture(bitmapBytes);
                    }
                }
            });
            CameraFragment.this.dismiss();
        }
    }
}

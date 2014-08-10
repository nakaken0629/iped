package com.iped_system.iped.app.ui;

import android.app.Dialog;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
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

import com.iped_system.iped.R;

import java.io.IOException;
import java.util.List;

public class CameraFragment extends DialogFragment {
    private static final String TAG = CameraFragment.class.getName();

    private Camera camera;

    public static CameraFragment newInstance(Fragment fragment) {
        CameraFragment cameraFragment = new CameraFragment();
        cameraFragment.setTargetFragment(fragment, 0);
        return cameraFragment;
    }

    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            camera = Camera.open(0);
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
            boolean portrait = isPortrait();

            /* change camera's orientation */
            camera.setDisplayOrientation(portrait ? 90 : 0);

            /* change size */
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            Camera.Size size = sizes.get(0);
            parameters.setPreviewSize(size.width, size.height);

            /* config layouts */
            SurfaceView surfaceView = (SurfaceView) getView().findViewById(R.id.cameraSurfaceView);
            ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
            layoutParams.width = (portrait ? size.height : size.width);
            layoutParams.height = (portrait ? size.width : size.height);
            surfaceView.setLayoutParams(layoutParams);

            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            camera.release();
            camera = null;
        }
    };

    protected boolean isPortrait() {
        return (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
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
}

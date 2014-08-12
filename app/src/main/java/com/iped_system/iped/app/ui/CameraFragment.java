package com.iped_system.iped.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.AudioManager;
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

public class CameraFragment extends DialogFragment {
    private static final String TAG = CameraFragment.class.getName();

    private Camera camera;

    public interface OnTakePictureListener {
        public void onTakePicture(byte[] bitmapBytes);
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
            /* TODO: 表示されるプレビューの向きとアスペクト比 */
            /* TODO: 出来上がる写真のサイズ */
            /* TODO: レンズが複数ある時の対応 */
            camera.stopPreview();
            Camera.Parameters parameters = camera.getParameters();
            boolean portrait = isPortrait();
            camera.setDisplayOrientation(portrait ? 90 : 0);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            camera.release();
            camera = null;
        }
    };
    private int currentOrientation;

    public static CameraFragment newInstance(Fragment fragment) {
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

package com.iped_system.iped.app.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.os.ApiAsyncTask;
import com.iped_system.iped.app.common.widget.EditTextEx;
import com.iped_system.iped.common.main.RemarksNewRequest;
import com.iped_system.iped.common.main.RemarksNewResponse;

import java.util.List;

public class RemarkFragment extends DialogFragment {
    private static final String TAG = RemarkFragment.class.getName();
    private RemarkFragment parent = RemarkFragment.this;

    private EditTextEx remarkEditText;
    private LinearLayout pictureLayout;
    private TextView newPictureTextView;

    public interface RemarkListener {
        public String getText();
        public List<Picture> getPictures();
        public void onNewPicture(String text);
        public void onRegister(String text);
        public void onCancel();
    }

    public static RemarkFragment newInstance(Fragment fragment) {
        if (!(fragment instanceof RemarkListener)) {
            throw new ClassCastException("fragment need to implements " + RemarkListener.class.getName());
        }
        RemarkFragment remarkFragment = new RemarkFragment();
        remarkFragment.setTargetFragment(fragment, 0);
        return remarkFragment;
    }

    private RemarkListener getRemarkListener() {
        return (RemarkListener) getTargetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "call onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_remark, container, false);
        this.remarkEditText = (EditTextEx) rootView.findViewById(R.id.remarkEditText);
        this.pictureLayout = (LinearLayout) rootView.findViewById(R.id.thumbnailLayout);
        this.newPictureTextView = (TextView) rootView.findViewById(R.id.newPictureTextView);

        this.remarkEditText.setText(getRemarkListener().getText());
        for(Picture picture : getRemarkListener().getPictures()) {
            ImageView imageView = new ImageView(this.getActivity());
            imageView.setImageBitmap(picture.getThumbnailBitmap());
            this.pictureLayout.addView(imageView);
        }
        this.newPictureTextView.setOnClickListener(new NewPictureListener());

        rootView.findViewById(R.id.remarkButton).setOnClickListener(new RemarkButtonListener());

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

    private class RemarkButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String text = parent.remarkEditText.getTrimmedValue();
            if (text == null || text.length() == 0) {
                return;
            }

            getRemarkListener().onRegister(text);
            parent.dismiss();
        }
    }

    private class NewPictureListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String text = parent.remarkEditText.getTrimmedValue();
            getRemarkListener().onNewPicture(text);
            parent.dismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getRemarkListener().onCancel();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(TAG, "call onDismiss");
    }
}

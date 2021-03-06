package com.iped_system.iped.app.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.iped_system.iped.app.common.net.StreamUtility;
import com.iped_system.iped.app.common.widget.EditTextEx;

import java.io.InputStream;
import java.util.List;

public class RemarkFragment extends DialogFragment {
    private static final String TAG = RemarkFragment.class.getName();
    private RemarkFragment parent = RemarkFragment.this;

    private EditTextEx remarkEditText;
    private LinearLayout pictureLayout;
    private TextView newPictureTextView;
    private TextView galleryPictureTextView;

    public interface RemarkListener {
        public String getText();

        public List<Picture> getPictures();

        public void addPicture(Picture picture);

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
        this.galleryPictureTextView = (TextView) rootView.findViewById(R.id.galleryPictureTextView);

        this.remarkEditText.setText(getRemarkListener().getText());
        for (Picture picture : getRemarkListener().getPictures()) {
            ImageView imageView = new ImageView(this.getActivity());
            imageView.setImageBitmap(picture.getThumbnailBitmap());
            this.pictureLayout.addView(imageView);
        }
        this.newPictureTextView.setOnClickListener(new NewPictureListener());
        this.galleryPictureTextView.setOnClickListener(new GalleryPictureListener());

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MainActivity.REQUEST_CODE_GALLERY_FROM_REMARK:
                addPictureFromGallery(resultCode, data);
                break;
            default:
                /* nop */
        }
    }

    private class RemarkButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String text = parent.remarkEditText.getTrimmedValue();
            if (text == null || text.length() == 0) {
                return;
            }
            int maxLength = 500;
            if (text.length() >= maxLength) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                AlertDialog dialog = builder.setTitle("メッセージ")
                        .setMessage("投稿できるのは" + maxLength + "文字未満まで（現在" + text.length() + "文字)")
                        .setPositiveButton("確認", null)
                        .create();
                dialog.show();
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

    private class GalleryPictureListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent();
            i.setType("image/*"); // 画像のみが表示されるようにフィルターをかける
            i.setAction(Intent.ACTION_GET_CONTENT); // ギャラリーを取得するアプリをすべて開く
            startActivityForResult(i, MainActivity.REQUEST_CODE_GALLERY_FROM_REMARK);
        }
    }

    private void addPictureFromGallery(int resultCode, Intent data) {
        if (resultCode == 0) {
            return;
        }
        try {
            ContentResolver cr = getActivity().getContentResolver();
            String[] columns = {MediaStore.Images.Media.DATA};
            Cursor c = cr.query(data.getData(), columns, null, null, null);
            c.moveToFirst();
            InputStream is = cr.openInputStream(data.getData());
            byte[] bitmapBytes = StreamUtility.readAll(is);
            is.close();
            Picture picture = new Picture(bitmapBytes, false);
            this.getRemarkListener().addPicture(picture);
            ImageView imageView = new ImageView(this.getActivity());
            imageView.setImageBitmap(picture.getThumbnailBitmap());
            this.pictureLayout.addView(imageView);
        } catch (Exception e) {
            Log.e(TAG, "error", e);
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

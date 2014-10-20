package com.iped_system.iped.app.main;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.os.ApiAsyncTask;
import com.iped_system.iped.app.common.widget.EditTextEx;
import com.iped_system.iped.common.main.RemarksNewRequest;
import com.iped_system.iped.common.main.RemarksNewResponse;

import java.util.ArrayList;

public class RemarkFragment extends DialogFragment {
    private static final String TAG = RemarkFragment.class.getName();

    private EditTextEx remarkEditText;
    private ArrayList<Picture> pictures = new ArrayList<Picture>();

    public interface RemarkListener {
        public void onRegister();
    }

    public static RemarkFragment newInstance(Fragment fragment) {
        if (!(fragment instanceof RemarkListener)) {
            throw new ClassCastException("fragment need to implements " + RemarkListener.class.getName());
        }
        RemarkFragment remarkFragment = new RemarkFragment();
        remarkFragment.setTargetFragment(fragment, 0);
        return remarkFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_remark, container, false);
        this.remarkEditText = (EditTextEx) rootView.findViewById(R.id.remarkEditText);
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
        private RemarkFragment parent = RemarkFragment.this;

        @Override
        public void onClick(View view) {
            String text = parent.remarkEditText.getTrimmedValue();
            if (text == null || text.length() == 0) {
                return;
            }

            RemarksNewRequest request = new RemarksNewRequest();
            request.setText(text);
            RemarksNewTask task = new RemarksNewTask(getActivity());
            task.execute(request);
        }
    }

    private class RemarksNewTask extends ApiAsyncTask<RemarksNewRequest, RemarksNewResponse> {
        private RemarkFragment parent = RemarkFragment.this;

        private RemarksNewTask(Activity activity) {
            super(activity);
        }

        @Override
        protected boolean isSecure() {
            return true;
        }

        @Override
        protected String getApiName() {
            return "remarks/new";
        }

        @Override
        protected void onPostExecuteOnSuccess(RemarksNewResponse remarksNewResponse) {
            RemarkListener listener = (RemarkListener) getTargetFragment();
            listener.onRegister();
            parent.dismiss();
        }
    }
}

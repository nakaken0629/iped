package com.iped_system.iped.app.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.app.RetainFragment;
import com.iped_system.iped.app.common.os.ApiAsyncTask;
import com.iped_system.iped.app.common.os.UploadAsyncTask;
import com.iped_system.iped.common.main.RemarkValue;
import com.iped_system.iped.common.main.RemarksNewRequest;
import com.iped_system.iped.common.main.RemarksNewResponse;
import com.iped_system.iped.common.main.RemarksRequest;
import com.iped_system.iped.common.main.RemarksResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeetingFragment extends Fragment implements RemarkFragment.RemarkListener, CameraFragment.CameraListener {
    private static final String TAG = MeetingFragment.class.getName();
    private final MeetingFragment parent = this;

    private Date lastDate;
    private Date firstDate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView meetingListView;
    private String text;
    private ArrayList<Picture> pictures = new ArrayList<Picture>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meeting, container, false);

        /* 変数初期化 */
        this.lastDate = null;
        this.firstDate = null;

        /* コマンド */
        RemarkListener remarkListener = new RemarkListener();
        rootView.findViewById(R.id.remarkImageView).setOnClickListener(remarkListener);
        rootView.findViewById(R.id.RemarkTextView).setOnClickListener(remarkListener);

        PhotoListener photoListener = new PhotoListener();
        rootView.findViewById(R.id.pictureImageView).setOnClickListener(photoListener);
        rootView.findViewById(R.id.pictureTextView).setOnClickListener(photoListener);

        /* リストビュー */
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.meetingRefresh);
        swipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        this.meetingListView = (ListView) rootView.findViewById(R.id.meetingListView);
        RetainFragment retainFragment = RetainFragment.findOrCreateRetainFragment(getFragmentManager());
        MeetingAdapter adapter = new MeetingAdapter(getActivity(), 0, retainFragment);
        this.meetingListView.setAdapter(adapter);

        reloadRemarks();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void reloadRemarks() {
        RemarksAsyncTask task = new RemarksAsyncTask(getActivity(), 0);
        RemarksRequest request = new RemarksRequest();
        request.setLastDate(this.lastDate);
        request.setFirstDate(this.firstDate);
        task.execute(request);
    }

    private void showCamera(boolean fromRemarkDialog) {
        CameraFragment fragment = CameraFragment.newInstance(parent);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CameraFragment.FROM_REMARK_DIALOG, fromRemarkDialog);
        fragment.setArguments(bundle);
        fragment.show(transaction, null);
    }

    private class RemarksAsyncTask extends ApiAsyncTask<RemarksRequest, RemarksResponse> {
        private MeetingFragment parent = MeetingFragment.this;
        private int index;

        private RemarksAsyncTask(Activity activity, int index) {
            super(activity);
            this.index = index;
        }

        @Override
        protected String getApiName() {
            return "remarks";
        }

        @Override
        protected void onPostExecuteOnSuccess(RemarksResponse remarksResponse) {
            MeetingAdapter adapter = (MeetingAdapter) parent.meetingListView.getAdapter();
            for (RemarkValue value : remarksResponse.getRemarkValues()) {
                MeetingItem item = new MeetingItem();
                item.setFaceKey(value.getFaceKey());
                item.setAuthorName(value.getAuthorName());
                item.setCreatedAt(value.getCreatedAt());
                item.setText(value.getText());
                item.setPictureKeys(value.getPictures());
                adapter.insert(item, index++);
                if (lastDate == null || lastDate.before(value.getCreatedAt())) {
                    lastDate = value.getCreatedAt();
                }
            }
            adapter.notifyDataSetChanged();
            parent.meetingListView.setSelection(0);
            MeetingFragment.this.swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onPostExecuteOnFailure(RemarksResponse remarksResponse) {
            MeetingFragment.this.swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onDisconnected() {
            MeetingFragment.this.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        private MeetingFragment parent = MeetingFragment.this;

        @Override
        public void onRefresh() {
            parent.reloadRemarks();
        }
    }

    private class RemarkListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RemarkFragment fragment = RemarkFragment.newInstance(parent);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            fragment.show(transaction, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "RequestCode = " + requestCode + ", resultCode = " + resultCode);
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public List<Picture> getPictures() {
        return this.pictures;
    }

    @Override
    public void onNewPicture(String text) {
        this.text = text;
        showCamera(true);
    }

    @Override
    public void onRegister(String text) {
        PhotoUploadTask task = new PhotoUploadTask(getActivity(), text);
        task.execute((Picture[]) this.pictures.toArray(new Picture[0]));
        this.text = null;
        this.pictures.clear();
    }

    private class PhotoUploadTask extends UploadAsyncTask {
        private String text;

        private PhotoUploadTask(Activity activity, String text) {
            super(activity);
            this.text = text;
        }

        @Override
        protected void onPostExecuteOnSuccess(List<String> pictures) {
            RemarksNewRequest request = new RemarksNewRequest();
            request.setText(text);
            request.setPictures(pictures);
            RemarksNewTask task = new RemarksNewTask(getActivity());
            task.execute(request);
        }
    }

    private class RemarksNewTask extends ApiAsyncTask<RemarksNewRequest, RemarksNewResponse> {
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
            reloadRemarks();
        }
    }

    @Override
    public void onCancel() {
        this.text = null;
        this.pictures.clear();
    }

    private class PhotoListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showCamera(false);
        }
    }

    @Override
    public void onTakePicture(byte[] bitmapBytes) {
        this.pictures.add(new Picture(bitmapBytes));

        RemarkFragment fragment = RemarkFragment.newInstance(parent);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment.show(transaction, null);
    }

    @Override
    public void backToRemark() {
        RemarkFragment fragment = RemarkFragment.newInstance(parent);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment.show(transaction, null);
    }
}

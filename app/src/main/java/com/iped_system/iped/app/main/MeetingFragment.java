package com.iped_system.iped.app.main;

import android.app.Activity;
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
import com.iped_system.iped.app.IpedApplication;
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

public class MeetingFragment extends Fragment implements MainActivity.RefreshObserver, RemarkFragment.RemarkListener, CameraFragment.CameraListener {
    private static final String TAG = MeetingFragment.class.getName();
    private final MeetingFragment parent = this;

    private Date firstDate;
    private Date lastDate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView meetingListView;
    private String text;
    private ArrayList<Picture> pictures = new ArrayList<Picture>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meeting, container, false);

        /* 変数初期化 */
        this.firstDate = null;
        this.lastDate = null;

        /* コマンド */
        RemarkListener remarkListener = new RemarkListener();
        rootView.findViewById(R.id.remarkImageView).setOnClickListener(remarkListener);
        rootView.findViewById(R.id.remarkTextView).setOnClickListener(remarkListener);

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

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).addObserver("meeting", this);
    }

    private void reloadRemarks() {
        RemarksAsyncTask task = new RemarksAsyncTask(getActivity());
        RemarksRequest request = new RemarksRequest();
        request.setLastDate(this.lastDate);
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

        private RemarksAsyncTask(Activity activity) {
            super(activity);
        }

        @Override
        protected String getApiName() {
            return "remarks";
        }

        @Override
        protected void onPostExecuteOnSuccess(RemarksResponse remarksResponse) {
            Date firstDate = null;
            Date lastDate = null;

            MeetingAdapter adapter = (MeetingAdapter) parent.meetingListView.getAdapter();
            for (RemarkValue value : remarksResponse.getRemarkValues()) {
                MeetingItem item = new MeetingItem();
                item.setFaceId(value.getFaceId());
                item.setAuthorName(value.getAuthorName());
                item.setCreatedAt(value.getCreatedAt());
                item.setText(value.getText());
                item.setPictureIdList(value.getPictureIdList());
                for (int index = 0; index < adapter.getCount() + 1; index++) {
                    if (index == adapter.getCount()) {
                        adapter.add(item);
                        break;
                    }
                    MeetingItem currentItem = adapter.getItem(index);
                    if (currentItem.getCreatedAt().before(item.getCreatedAt())) {
                        adapter.insert(item, index);
                        break;
                    }
                }
                if (firstDate == null || firstDate.after(value.getCreatedAt())) {
                    firstDate = value.getCreatedAt();
                }
                if (lastDate == null || lastDate.before(value.getCreatedAt())) {
                    lastDate = value.getCreatedAt();
                }

                if (parent.firstDate == null || (firstDate != null && firstDate.before(parent.firstDate))) {
                    parent.firstDate = firstDate;
                }
                if (parent.lastDate == null || (lastDate != null && lastDate.after(parent.lastDate))) {
                    parent.lastDate = lastDate;
                }
            }
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
        IpedApplication application = (IpedApplication) getActivity().getApplicationContext();
        PhotoUploadTask task = new PhotoUploadTask(getActivity(), application.getPatientId(), text);
        task.execute((Picture[]) this.pictures.toArray(new Picture[0]));
        this.text = null;
        this.pictures.clear();
    }

    private class PhotoUploadTask extends UploadAsyncTask {
        private String text;

        private PhotoUploadTask(Activity activity, String patientId, String text) {
            super(activity, patientId);
            this.text = text;
        }

        @Override
        protected void onPostExecuteOnSuccess(List<Long> pictureIdList) {
            RemarksNewRequest request = new RemarksNewRequest();
            request.setText(text);
            request.setPictureIdList(pictureIdList);
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

    @Override
    public void refresh() {
        this.firstDate = null;
        this.lastDate = null;
        ((MeetingAdapter) this.meetingListView.getAdapter()).clear();
        reloadRemarks();
    }
}

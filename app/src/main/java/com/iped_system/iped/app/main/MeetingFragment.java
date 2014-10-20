package com.iped_system.iped.app.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.app.RetainFragment;
import com.iped_system.iped.app.common.os.ApiAsyncTask;
import com.iped_system.iped.common.main.RemarkValue;
import com.iped_system.iped.common.main.RemarksRequest;
import com.iped_system.iped.common.main.RemarksResponse;

import java.util.Date;

public class MeetingFragment extends Fragment implements RemarkFragment.RemarkListener {
    private static final String TAG = MeetingFragment.class.getName();

    private Date lastDate;
    private Date firstDate;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView meetingListView;

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

//        PhotoListener photoListener = new PhotoListener();
//        rootView.findViewById(R.id.photoImageView).setOnClickListener(photoListener);
//        rootView.findViewById(R.id.photoTextView).setOnClickListener(photoListener);

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
            for(RemarkValue value : remarksResponse.getRemarkValues()) {
                MeetingItem item = new MeetingItem();
                item.setFaceKey(value.getFaceKey());
                item.setAuthorName(value.getAuthorName());
                item.setCreatedAt(value.getCreatedAt());
                item.setText(value.getText());
                adapter.insert(item, index++);
                if (lastDate == null || lastDate.before(value.getCreatedAt())) {
                    lastDate = value.getCreatedAt();
                }
            }
            adapter.notifyDataSetChanged();
            parent.meetingListView.setSelection(0);
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
            RemarkFragment fragment = RemarkFragment.newInstance(MeetingFragment.this);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            fragment.show(transaction, null);
        }
    }

    @Override
    public void onRegister() {
        reloadRemarks();
    }

//    private class PhotoListener implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            CameraFragment fragment = CameraFragment.newInstance(MeetingFragment.this);
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            fragment.show(transaction, null);
//        }
//    }
//
//    @Override
//    public void onTakePicture(byte[] bitmapBytes) {
//        RemarkFragment fragment = RemarkFragment.newInstance(MeetingFragment.this);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        fragment.show(transaction, null);
//        Bundle args = new Bundle();
//        args.putByteArray("pictureData", bitmapBytes);
//    }
}

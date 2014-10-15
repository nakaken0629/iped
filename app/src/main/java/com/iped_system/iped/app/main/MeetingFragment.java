package com.iped_system.iped.app.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.iped_system.iped.common.main.RemarkValue;
import com.iped_system.iped.common.main.RemarksRequest;
import com.iped_system.iped.common.main.RemarksResponse;

import java.util.Date;
import java.util.List;

public class MeetingFragment extends Fragment implements RemarkFragment.OnRegisterListener, CameraFragment.OnTakePictureListener {
    private static final String TAG = MeetingFragment.class.getName();

    private Date lastDate;
    private Date firstDate;
    private SwipeRefreshLayout swipeLayout;
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

        PhotoListener photoListener = new PhotoListener();
        rootView.findViewById(R.id.photoImageView).setOnClickListener(photoListener);
        rootView.findViewById(R.id.photoTextView).setOnClickListener(photoListener);

        /* リストビュー */
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.meetingRefresh);
        swipeLayout.setOnRefreshListener(new RefreshListener());
        this.meetingListView = (ListView) rootView.findViewById(R.id.meetingListView);
        RetainFragment retainFragment = RetainFragment.findOrCreateRetainFragment(getFragmentManager());
        MeetingAdapter adapter = new MeetingAdapter(getActivity(), 0, retainFragment);
        this.meetingListView.setAdapter(adapter);

        reloadRemarks();
        return rootView;
    }

    private void reloadRemarks() {
        RemarksAsyncTask task = new RemarksAsyncTask(getActivity(), 0);
        RemarksRequest request = new RemarksRequest();
        request.setLastDate(this.lastDate);
        request.setFirstDate(this.firstDate);
        task.execute(request);
    }

    class RemarksAsyncTask extends ApiAsyncTask<RemarksRequest, RemarksResponse> {
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
            MeetingFragment.this.swipeLayout.setRefreshing(false);
        }
    }

    private void insertRemarks(List<RemarkValue> remarkValues) {
        ListView meetingListView = (ListView) getView().findViewById(R.id.meetingListView);
        MeetingAdapter adapter = (MeetingAdapter) meetingListView.getAdapter();
        for (RemarkValue remarkValue : remarkValues) {
            MeetingItem item = new MeetingItem();
            item.setAuthorName(remarkValue.getAuthorName());
            item.setCreatedAt(remarkValue.getCreatedAt());
            item.setText(remarkValue.getText());
            if (remarkValue.getPictures() != null) {
                for (String blobKey : remarkValue.getPictures()) {
                    Log.d(TAG, "blobKey: " + blobKey);
                }
            } else {
                Log.d(TAG, "blobKey: null");
            }
            adapter.insert(item, 0);

            if (lastDate == null || lastDate.before(remarkValue.getCreatedAt())) {
                lastDate = remarkValue.getCreatedAt();
            }
        }
        adapter.notifyDataSetChanged();
    }

    class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        private MeetingFragment parent = MeetingFragment.this;

        @Override
        public void onRefresh() {
            parent.reloadRemarks();
        }
    }

    class RemarkListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RemarkFragment fragment = RemarkFragment.newInstance(MeetingFragment.this);
            Bundle args = new Bundle();
            args.putSerializable("lastDate", lastDate);
            showDialog(fragment, args);
        }
    }

    @Override
    public void onRegister() {
        reloadRemarks();
    }

    class PhotoListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            CameraFragment fragment = CameraFragment.newInstance(MeetingFragment.this);
            showDialog(fragment, null);
        }
    }

    @Override
    public void onTakePicture(byte[] bitmapBytes) {
        RemarkFragment fragment = RemarkFragment.newInstance(MeetingFragment.this);
        Bundle args = new Bundle();
        args.putSerializable("lastDate", lastDate);
        args.putByteArray("pictureData", bitmapBytes);
        showDialog(fragment, args);
    }

    private void showDialog(DialogFragment fragment, Bundle args) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        if (args != null) {
            fragment.setArguments(args);
        }
        fragment.show(transaction, "dialog");
    }
}

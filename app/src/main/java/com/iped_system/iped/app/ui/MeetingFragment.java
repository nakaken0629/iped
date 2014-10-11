package com.iped_system.iped.app.ui;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.os.ApiAsyncTask;
import com.iped_system.iped.common.Remark;
import com.iped_system.iped.common.RemarksRequest;
import com.iped_system.iped.common.RemarksResponse;

import java.util.Date;
import java.util.List;

public class MeetingFragment extends Fragment implements RemarkFragment.OnRegisterListener, CameraFragment.OnTakePictureListener {
    private static final String TAG = MeetingFragment.class.getName();

    private Date lastUpdate;
    private SwipeRefreshLayout swipe;
    private ListView meetingListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meeting, container, false);

        /* 変数初期化 */
        this.lastUpdate = null;

        /* コマンド */
        RemarkListener remarkListener = new RemarkListener();
        ImageView remarkImageView = (ImageView) rootView.findViewById(R.id.remarkImageView);
        remarkImageView.setOnClickListener(remarkListener);
        TextView remarkTextView = (TextView) rootView.findViewById(R.id.RemarkTextView);
        remarkTextView.setOnClickListener(remarkListener);

        PhotoListener photoListener = new PhotoListener();
        ImageView photoImageView = (ImageView) rootView.findViewById(R.id.photoImageView);
        photoImageView.setOnClickListener(photoListener);
        TextView photoTextView = (TextView) rootView.findViewById(R.id.photoTextView);
        photoTextView.setOnClickListener(photoListener);

        /* リストビュー */
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.meetingRefresh);
        swipe.setOnRefreshListener(new RefreshListener());
        this.meetingListView = (ListView) rootView.findViewById(R.id.meetingListView);
        MeetingAdapter adapter = new MeetingAdapter(getActivity(), 0);
        this.meetingListView.setAdapter(adapter);

        reloadRemarks();

        return rootView;
    }

    private void reloadRemarks() {
        ReloadRemarksAsyncTask task = new ReloadRemarksAsyncTask(getActivity());
        RemarksRequest request = new RemarksRequest();
        request.setLastUpdate(this.lastUpdate);
        task.execute(request);
    }

    class ReloadRemarksAsyncTask extends ApiAsyncTask<RemarksRequest, RemarksResponse> {
        ReloadRemarksAsyncTask(Activity activity) {
            super(activity);
        }

        @Override
        protected boolean isSecure() {
            return true;
        }

        @Override
        protected String getApiName() {
            return "remarks";
        }

        @Override
        protected void onPostExecuteOnSuccess(RemarksResponse remarksResponse) {
            MeetingAdapter adapter = (MeetingAdapter) MeetingFragment.this.meetingListView.getAdapter();
            for(Remark remark : remarksResponse.getRemarks()) {
                MeetingItem item = new MeetingItem();
                item.setAuthorName(remark.getAuthorName());
                item.setCreatedAt(remark.getCreatedAt());
                item.setText(remark.getText());
                adapter.insert(item, 0);
                if (lastUpdate == null || lastUpdate.before(remark.getCreatedAt())) {
                    lastUpdate = remark.getCreatedAt();
                }
            }
            adapter.notifyDataSetChanged();
            MeetingFragment.this.swipe.setRefreshing(false);
        }
    }

    private void insertRemarks(List<Remark> remarks) {
        ListView meetingListView = (ListView) getView().findViewById(R.id.meetingListView);
        MeetingAdapter adapter = (MeetingAdapter) meetingListView.getAdapter();
        for (Remark remark : remarks) {
            MeetingItem item = new MeetingItem();
            item.setAuthorName(remark.getAuthorName());
            item.setCreatedAt(remark.getCreatedAt());
            item.setText(remark.getText());
            if (remark.getPictures() != null) {
                for (String blobKey : remark.getPictures()) {
                    Log.d(TAG, "blobKey: " + blobKey);
                }
            } else {
                Log.d(TAG, "blobKey: null");
            }
            adapter.insert(item, 0);

            if (lastUpdate == null || lastUpdate.before(remark.getCreatedAt())) {
                lastUpdate = remark.getCreatedAt();
            }
        }
        adapter.notifyDataSetChanged();
    }

    class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            MeetingFragment.this.reloadRemarks();
        }
    }

    class RemarkListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            RemarkFragment fragment = RemarkFragment.newInstance(MeetingFragment.this);
            Bundle args = new Bundle();
            args.putSerializable("lastUpdate", lastUpdate);
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
        args.putSerializable("lastUpdate", lastUpdate);
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

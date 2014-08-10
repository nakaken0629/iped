package com.iped_system.iped.app.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.network.ApiAsyncTaskLoader;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.Remark;
import com.iped_system.iped.common.RemarksNewResponse;
import com.iped_system.iped.common.RemarksRequest;
import com.iped_system.iped.common.RemarksResponse;

import java.util.Date;
import java.util.List;

public class MeetingFragment extends Fragment implements RemarkFragment.OnRegisterListener {
    private static final String TAG = MeetingFragment.class.getName();

    private RemarksCallbacks remarksCallbacks;
    private Date lastUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meeting, container, false);
        this.remarksCallbacks = new RemarksCallbacks();

        /* コマンド */
        TextView remarkTextView = (TextView) rootView.findViewById(R.id.RemarkTextView);
        remarkTextView.setOnClickListener(new RemarkTextViewListener());
        TextView photoTextView = (TextView) rootView.findViewById(R.id.photoTextView);
        photoTextView.setOnClickListener(new PhotoTextViewListener());

        /* リストビュー */
        Button refreshButton = (Button) rootView.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new RefreshButtonListener());
        ListView meetingListView = (ListView) rootView.findViewById(R.id.meetingListView);
        MeetingAdapter adapter = new MeetingAdapter(getActivity().getApplicationContext(), 0);
        meetingListView.setAdapter(adapter);
        Bundle bundle = new Bundle();
        bundle.putSerializable("lastUpdate", lastUpdate);
        getLoaderManager().initLoader(0, bundle, remarksCallbacks);

        return rootView;
    }

    private void insertRemarks(List<Remark> remarks) {
        ListView meetingListView = (ListView) getView().findViewById(R.id.meetingListView);
        MeetingAdapter adapter = (MeetingAdapter) meetingListView.getAdapter();
        for(Remark remark : remarks) {
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
    }

    class RefreshButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("lastUpdate", lastUpdate);
            getLoaderManager().restartLoader(0, bundle, remarksCallbacks);
        }
    }

    class RemarksCallbacks implements LoaderManager.LoaderCallbacks<BaseResponse> {
        @Override
        public Loader<BaseResponse> onCreateLoader(int i, Bundle bundle) {
            Context context = getActivity().getApplicationContext();
            RemarksRequest request = new RemarksRequest();
            Date lastUpdate = (Date) bundle.getSerializable("lastUpdate");
            request.setLastUpdate(lastUpdate);

            ApiAsyncTaskLoader loader = new ApiAsyncTaskLoader(context, request, "remarks", true);
            loader.forceLoad();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<BaseResponse> baseResponseLoader, BaseResponse baseResponse) {
            RemarksResponse response = (RemarksResponse) baseResponse;
            insertRemarks(response.getRemarks());
        }

        @Override
        public void onLoaderReset(Loader<BaseResponse> baseResponseLoader) {
            /* nop */
        }
    }

    class RemarkTextViewListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            RemarkFragment fragment = RemarkFragment.newInstance(MeetingFragment.this);
            Bundle args = new Bundle();
            args.putSerializable("lastUpdate", lastUpdate);
            fragment.setArguments(args);
            fragment.show(transaction, "dialog");
        }
    }

    @Override
    public void onRegister(RemarksNewResponse response) {
        insertRemarks(response.getRemarks());
    }

    class PhotoTextViewListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog dialog = builder.setTitle("メッセージ")
                    .setMessage("現在実装されていません")
                    .setPositiveButton("確認", null)
                    .create();
            dialog.show();
        }
    }
}
package com.iped_system.iped.app.ui;

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
import android.widget.ListView;
import android.widget.TextView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.network.ApiAsyncTaskLoader;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.Remark;
import com.iped_system.iped.common.RemarksNewResponse;
import com.iped_system.iped.common.RemarksRequest;
import com.iped_system.iped.common.RemarksResponse;

public class MeetingFragment extends Fragment implements RemarkFragment.OnRegisterListener {
    private static final String TAG = MeetingFragment.class.getName();

    private RemarksCallbacks remarksCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meeting, container, false);
        this.remarksCallbacks = new RemarksCallbacks();

        /* コマンド */
        TextView remarkTextView = (TextView) rootView.findViewById(R.id.RemarkTextView);
        remarkTextView.setOnClickListener(new RemarkTextViewListener());
        /* リストビュー */
        ListView meetingListView = (ListView) rootView.findViewById(R.id.meetingListView);
        MeetingAdapter adapter = new MeetingAdapter(getActivity().getApplicationContext(), 0);
        meetingListView.setAdapter(adapter);
        Bundle bundle = new Bundle();
        getLoaderManager().initLoader(0, bundle, this.remarksCallbacks);

        return rootView;
    }

    class RemarksCallbacks implements LoaderManager.LoaderCallbacks<BaseResponse> {
        @Override
        public Loader<BaseResponse> onCreateLoader(int i, Bundle bundle) {
            Context context = getActivity().getApplicationContext();
            RemarksRequest request = new RemarksRequest();

            ApiAsyncTaskLoader loader = new ApiAsyncTaskLoader(context, request, "remarks", true);
            loader.forceLoad();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<BaseResponse> baseResponseLoader, BaseResponse baseResponse) {
            RemarksResponse response = (RemarksResponse) baseResponse;

            ListView meetingListView = (ListView) getView().findViewById(R.id.meetingListView);
            MeetingAdapter adapter = (MeetingAdapter) meetingListView.getAdapter();
            for(Remark remark : response.getRemarks()) {
                MeetingItem item = new MeetingItem();
                item.setAuthorName(remark.getAuthorName());
                item.setText(remark.getText());
                adapter.add(item);
            }
            adapter.notifyDataSetChanged();
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
            fragment.show(transaction, "dialog");
        }
    }

    @Override
    public void onRegister(RemarksNewResponse response) {
        Remark remark = response.getRemark();

        ListView meetingListView = (ListView) getView().findViewById(R.id.meetingListView);
        MeetingAdapter adapter = (MeetingAdapter) meetingListView.getAdapter();
        MeetingItem item = new MeetingItem();
        item.setAuthorName(remark.getAuthorName());
        item.setCreatedAt(remark.getCreatedAt());
        item.setText(remark.getText());
        adapter.insert(item, 0);
        adapter.notifyDataSetChanged();
    }
}

package com.iped_system.iped.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.network.ApiAsyncTaskLoader;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RemarksNewRequest;
import com.iped_system.iped.common.Remark;
import com.iped_system.iped.common.RemarksRequest;
import com.iped_system.iped.common.RemarksResponse;

public class MeetingFragment extends Fragment {
    private static final String TAG = MeetingFragment.class.getName();

    private RemarksCallbacks remarksCallbacks;
    private RemarksNewCallbacks remarksNewCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meeting, container, false);
        this.remarksCallbacks = new RemarksCallbacks();
        this.remarksNewCallbacks = new RemarksNewCallbacks();

        /* 送信ボタン */
        Button messageButton = (Button) rootView.findViewById(R.id.messageButton);
        messageButton.setOnClickListener(new MessageButtonListener());
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

    class MessageButtonListener implements View.OnClickListener {
        private String getEditTextValue(int id) {
            return ((EditText) getView().findViewById(id)).getText().toString().trim();
        }

        @Override
        public void onClick(View view) {
            String text = getEditTextValue(R.id.messageEditText);
            Log.d(TAG, "text: " + text);
            if (text == null || text.length() == 0) {
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("text", text);
            MeetingFragment self = MeetingFragment.this;
            self.getLoaderManager().restartLoader(0, bundle, self.remarksNewCallbacks);
        }
    }

    class RemarksNewCallbacks implements LoaderManager.LoaderCallbacks<BaseResponse> {

        @Override
        public Loader<BaseResponse> onCreateLoader(int i, Bundle bundle) {
            Context context = getActivity().getApplicationContext();
            RemarksNewRequest request = new RemarksNewRequest();
            request.setAuthorName(bundle.getString("authorName"));
            request.setText(bundle.getString("text"));

            ApiAsyncTaskLoader loader = new ApiAsyncTaskLoader(context, request, "remarks/new", true);
            loader.forceLoad();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<BaseResponse> baseResponseLoader, BaseResponse baseResponse) {
            /* nop */
        }

        @Override
        public void onLoaderReset(Loader<BaseResponse> baseResponseLoader) {
            /* nop */
        }
    }
}

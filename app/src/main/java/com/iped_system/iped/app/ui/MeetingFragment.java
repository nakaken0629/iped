package com.iped_system.iped.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.network.ApiAsyncTaskLoader;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RegisterRemarkRequest;

public class MeetingFragment extends Fragment implements LoaderManager.LoaderCallbacks<BaseResponse> {
    private static final String TAG = MeetingFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meeting, container, false);
        /* 送信ボタン */
        Button messageButton = (Button) rootView.findViewById(R.id.messageButton);
        messageButton.setOnClickListener(new MessageButtonListener());
        /* リストビュー */
        ListView meetingListView = (ListView) rootView.findViewById(R.id.meetingListView);
        MeetingAdapter adapter = new MeetingAdapter(getActivity().getApplicationContext(), 0);
        meetingListView.setAdapter(adapter);

        Remark item = new Remark();
        item.setText("これはテストです");
        adapter.add(item);
        adapter.add(item);
        adapter.add(item);

        return rootView;
    }

    public void setEnabled(boolean enabled) {
        getView().findViewById(R.id.messageEditText).setEnabled(enabled);
        getView().findViewById(R.id.messageButton).setEnabled(enabled);
    }


    class MessageButtonListener implements View.OnClickListener {
        private String getEditTextValue(int id) {
            return ((EditText) getView().findViewById(id)).getText().toString().trim();
        }

        @Override
        public void onClick(View view) {
            String text = getEditTextValue(R.id.messageEditText);
            if (text == null || text.length() == 0) {
                return;
            }
            setEnabled(false);

            Bundle bundle = new Bundle();
            bundle.putString("text", text);
            MeetingFragment self = MeetingFragment.this;
            self.getLoaderManager().initLoader(0, bundle, self);
        }
    }

    @Override
    public Loader<BaseResponse> onCreateLoader(int i, Bundle bundle) {
        Context context = getActivity().getApplicationContext();
        RegisterRemarkRequest request = new RegisterRemarkRequest();
        request.setText(bundle.getString("text"));

        ApiAsyncTaskLoader loader = new ApiAsyncTaskLoader(context, request, "register-meeting", true);
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<BaseResponse> baseResponseLoader, BaseResponse baseResponse) {
        setEnabled(true);
    }

    @Override
    public void onLoaderReset(Loader<BaseResponse> baseResponseLoader) {
        setEnabled(true);
    }
}

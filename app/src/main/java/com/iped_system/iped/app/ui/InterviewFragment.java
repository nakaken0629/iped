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
import com.iped_system.iped.common.Talk;
import com.iped_system.iped.common.TalksNewRequest;
import com.iped_system.iped.common.TalksNewResponse;

import java.util.Date;
import java.util.List;

public class InterviewFragment extends Fragment {
    private Date lastUpdate;
    private InterviewsNewCallbacks interviewsNewCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_interview, container, false);
        this.interviewsNewCallbacks = new InterviewsNewCallbacks();

        /* コマンド */
        Button postButton = (Button) rootView.findViewById(R.id.postButton);
        postButton.setOnClickListener(new PostListener());

        /* リストビュー */
        ListView interviewListView = (ListView) rootView.findViewById(R.id.interviewListView);
        InterviewAdapter adapter = new InterviewAdapter(getActivity().getApplicationContext(), 0);
        interviewListView.setAdapter(adapter);
        return rootView;
    }

    private void insertTalks(List<Talk> talks) {
        /* TODO: ListViewに追加する処理 */
    }

    class PostListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            EditText postEditText = (EditText) getView().findViewById(R.id.postEditText);
            String text = postEditText.getText().toString().trim();
            if (text == null || text.length() == 0) {
                return;
            }

            postEditText.setText("");
            InterviewFragment self = InterviewFragment.this;
            Bundle bundle = new Bundle();
            bundle.putString("text", text);
            self.getLoaderManager().restartLoader(0, bundle, self.interviewsNewCallbacks);
        }
    }

    class InterviewsNewCallbacks implements LoaderManager.LoaderCallbacks<BaseResponse> {

        @Override
        public Loader<BaseResponse> onCreateLoader(int i, Bundle bundle) {
            Context context = getActivity().getApplicationContext();
            TalksNewRequest request = new TalksNewRequest();
            request.setLastUpdate(lastUpdate);
            request.setText(bundle.getString("text"));
            ApiAsyncTaskLoader loader = new ApiAsyncTaskLoader(context, request, "talks/new", true);
            loader.forceLoad();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<BaseResponse> baseResponseLoader, BaseResponse baseResponse) {
            TalksNewResponse response = (TalksNewResponse) baseResponse;
        }

        @Override
        public void onLoaderReset(Loader<BaseResponse> baseResponseLoader) {
            /* nop */
        }
    }
}

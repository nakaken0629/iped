package com.iped_system.iped.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.network.ApiAsyncTask;
import com.iped_system.iped.common.Talk;
import com.iped_system.iped.common.TalksNewRequest;
import com.iped_system.iped.common.TalksNewResponse;

import java.util.Date;
import java.util.List;

public class InterviewFragment extends Fragment {
    private static final String TAG = InterviewFragment.class.getName();

    private Date lastUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_interview, container, false);

        /* 変数初期化 */
        this.lastUpdate = null;

        /* コマンド */
        Button postButton = (Button) rootView.findViewById(R.id.postButton);
        postButton.setOnClickListener(new PostListener());

        /* リストビュー */
        ListView interviewListView = (ListView) rootView.findViewById(R.id.interviewListView);
        InterviewAdapter adapter = new InterviewAdapter(getActivity(), 0);
        interviewListView.setAdapter(adapter);
        return rootView;
    }

    private void insertTalks(List<Talk> talks) {
        ListView interviewListView = (ListView) getView().findViewById(R.id.interviewListView);
        InterviewAdapter adapter = (InterviewAdapter) interviewListView.getAdapter();
        for (Talk talk : talks) {
            Log.d(TAG, "createdAt: " + talk.getCreatedAt());
            TalkItem item = new TalkItem();
            item.setYouText(talk.getYouText());
            item.setAuthorName(talk.getAuthorName());
            item.setMeText(talk.getMeText());
            item.setCreatedAt(talk.getCreatedAt());
            adapter.add(item);

            if (lastUpdate == null || lastUpdate.before(talk.getCreatedAt())) {
                lastUpdate = talk.getCreatedAt();
            }
        }
        adapter.notifyDataSetChanged();
        interviewListView.setSelection(interviewListView.getCount() - 1);
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
            ApiAsyncTask<TalksNewRequest, TalksNewResponse> task = new ApiAsyncTask<TalksNewRequest, TalksNewResponse>(InterviewFragment.this.getActivity()) {
                @Override
                protected boolean isSecure() {
                    return true;
                }

                @Override
                protected String getApiName() {
                    return "talks/new";
                }

                @Override
                protected void onPostExecute(TalksNewResponse talksNewResponse) {
                    insertTalks(talksNewResponse.getTalks());
                }
            };
            TalksNewRequest request = new TalksNewRequest();
            request.setText(text);
            request.setLastUpdate(lastUpdate);
            task.execute(request);
        }
    }
}

package com.iped_system.iped.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.os.ApiAsyncTask;
import com.iped_system.iped.common.Talk;
import com.iped_system.iped.common.TalksNewRequest;
import com.iped_system.iped.common.TalksNewResponse;
import com.iped_system.iped.common.TalksRequest;
import com.iped_system.iped.common.TalksResponse;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class InterviewFragment extends Fragment {
    private static final String TAG = InterviewFragment.class.getName();

    private Date lastUpdate;
    private ListView interviewListView;
    private Timer reloadTimer;
    private Handler handler = new Handler();

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
        this.interviewListView = (ListView) rootView.findViewById(R.id.interviewListView);
        InterviewAdapter adapter = new InterviewAdapter(getActivity(), 0);
        this.interviewListView.setAdapter(adapter);

        this.reloadTalks();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.reloadTimer = new Timer(true);
        this.reloadTimer.schedule(new ReloadTask(), 5000, 5000);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.reloadTimer.cancel();
        this.reloadTimer = null;
    }

    class ReloadTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "execute ReloadTask.run()");
                    reloadTalks();
                }
            });
        }
    }

    private void reloadTalks() {
        ReloadTalksAsyncTask task = new ReloadTalksAsyncTask(getActivity());
        TalksRequest request = new TalksRequest();
        request.setLastUpdate(this.lastUpdate);
        task.execute(request);
    }

    class ReloadTalksAsyncTask extends ApiAsyncTask<TalksRequest, TalksResponse> {
        ReloadTalksAsyncTask(Activity context) {
            super(context);
        }

        @Override
        protected boolean isSecure() {
            return true;
        }

        @Override
        protected String getApiName() {
            return "talks";
        }

        @Override
        protected void onPostExecuteOnSuccess(TalksResponse talksResponse) {
            InterviewAdapter adapter = (InterviewAdapter) InterviewFragment.this.interviewListView.getAdapter();
            for(Talk talk : talksResponse.getTalks()) {
                TalkItem item = new TalkItem();
                item.setAuthorName(talk.getAuthorName());
                item.setCreatedAt(talk.getCreatedAt());
                item.setMeText(talk.getMeText());
                item.setYouText(talk.getYouText());
                adapter.add(item);
                if (lastUpdate == null || lastUpdate.before(talk.getCreatedAt())) {
                    lastUpdate = talk.getCreatedAt();
                }
            }
            adapter.notifyDataSetChanged();
            interviewListView.setSelection(interviewListView.getCount() - 1);
        }
    }

    private void insertTalks(List<Talk> talks) {
        ListView interviewListView = (ListView) getView().findViewById(R.id.interviewListView);
        InterviewAdapter adapter = (InterviewAdapter) interviewListView.getAdapter();
        for (Talk talk : talks) {
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
        private InterviewFragment self = InterviewFragment.this;

        @Override
        public void onClick(View view) {
            EditText postEditText = (EditText) getView().findViewById(R.id.postEditText);
            String text = postEditText.getText().toString().trim();
            if (text == null || text.length() == 0) {
                return;
            }

            postEditText.setText("");
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
                protected void onPostExecuteOnSuccess(TalksNewResponse talksNewResponse) {
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

package com.iped_system.iped.app.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.IpedApplication;
import com.iped_system.iped.app.common.app.RetainFragment;
import com.iped_system.iped.app.common.os.ApiAsyncTask;
import com.iped_system.iped.app.common.os.UploadAsyncTask;
import com.iped_system.iped.common.main.RemarksNewRequest;
import com.iped_system.iped.common.main.TalkValue;
import com.iped_system.iped.common.main.TalksNewRequest;
import com.iped_system.iped.common.main.TalksNewResponse;
import com.iped_system.iped.common.main.TalksRequest;
import com.iped_system.iped.common.main.TalksResponse;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class InterviewFragment extends Fragment implements MainActivity.RefreshObserver, PictogramFragment.OnFragmentInteractionListener, CameraFragment.CameraListener {
    private static final String TAG = InterviewFragment.class.getName();
    private final InterviewFragment self = this;

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
        Button cameraButton = (Button) rootView.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new CameraListener());
        Button postButton = (Button) rootView.findViewById(R.id.postButton);
        postButton.setOnClickListener(new PostListener());
        Button pictogramButton = (Button) rootView.findViewById(R.id.pictogramButton);
        pictogramButton.setOnClickListener(new PictogramListener());

        /* リストビュー */
        this.interviewListView = (ListView) rootView.findViewById(R.id.interviewListView);
        RetainFragment retainFragment = RetainFragment.findOrCreateRetainFragment(getFragmentManager());
        InterviewAdapter adapter = new InterviewAdapter(getActivity(), 0, retainFragment);
        this.interviewListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).addObserver("interview", this);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.reloadTalks();
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
            for(TalkValue talkValue : talksResponse.getTalkValues()) {
                TalkItem item = new TalkItem();
                item.setFaceId(talkValue.getFaceId());
                item.setAuthorName(talkValue.getAuthorName());
                item.setCreatedAt(talkValue.getCreatedAt());
                item.setMeText(talkValue.getMeText());
                item.setMePictogramKey(talkValue.getMePictogramKey());
                item.setMePictureId(talkValue.getMePictureId());
                item.setYouText(talkValue.getYouText());
                item.setYouPictogramKey(talkValue.getYouPictogramKey());
                item.setYouPictureId(talkValue.getYouPictureId());
                adapter.add(item);
                if (lastUpdate == null || lastUpdate.before(talkValue.getCreatedAt())) {
                    lastUpdate = talkValue.getCreatedAt();
                }
            }
            interviewListView.setSelection(interviewListView.getCount() - 1);
        }

        @Override
        protected void onDisconnected() {
            /* don't show dialog */
        }
    }

    class TalkTask extends ApiAsyncTask<TalksNewRequest, TalksNewResponse> {
        TalkTask(Activity activity) {
            super(activity);
        }

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
            self.reloadTalks();
        }
    }

    class CameraListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            CameraFragment fragment = CameraFragment.newInstance(self);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putBoolean(CameraFragment.FROM_REMARK_DIALOG, false);
            fragment.setArguments(bundle);
            fragment.show(transaction, null);
        }
    }

    @Override
    public void onTakePicture(byte[] bitmapBytes) {
        Picture picture = new Picture(bitmapBytes);
        IpedApplication application = (IpedApplication) getActivity().getApplicationContext();
        PhotoUploadTask task = new PhotoUploadTask(getActivity(), application.getPatientId());
        task.execute(picture);
    }

    private class PhotoUploadTask extends UploadAsyncTask {
        private PhotoUploadTask(Activity activity, String patientId) {
            super(activity, patientId);
        }

        @Override
        protected void onPostExecuteOnSuccess(List<Long> pictureIdList) {
            TalksNewRequest request = new TalksNewRequest();
            request.setPictureId(pictureIdList.get(0));
            TalkTask task = new TalkTask(getActivity());
            task.execute(request);
        }
    }

    @Override
    public void backToRemark() {
        /* nop */
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
            TalkTask task = new TalkTask(self.getActivity());
            TalksNewRequest request = new TalksNewRequest();
            request.setText(text);
            request.setLastUpdate(lastUpdate);
            task.execute(request);
        }
    }

    class PictogramListener implements View.OnClickListener {
        private InterviewFragment parent = InterviewFragment.this;

        @Override
        public void onClick(View view) {
            PictogramFragment fragment = PictogramFragment.newInstance(parent);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            fragment.show(transaction, null);
        }
    }

    @Override
    public void onFragmentInteraction(String pictogramKey) {
        TalkTask task = new TalkTask(self.getActivity());
        TalksNewRequest request = new TalksNewRequest();
        request.setPictogramKey(pictogramKey);
        request.setLastUpdate(lastUpdate);
        task.execute(request);
    }

    @Override
    public void refresh() {
        this.lastUpdate = null;
        ((InterviewAdapter) this.interviewListView.getAdapter()).clear();
        reloadTalks();
    }
}

package com.iped_system.iped;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RegisterMeetingRequest;
import com.iped_system.iped.common.RegisterMeetingResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MeetingFragment extends Fragment {
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

            RegisterMeetingRequest request = new RegisterMeetingRequest();
            request.setTokenId(((IpedApplication) getActivity().getApplication()).getTokenId());
            request.setText(text);
            RegisterMessageTask task = new RegisterMessageTask();
            task.execute(request);
        }
    }

    class RegisterMessageTask extends AsyncTask<RegisterMeetingRequest, Void, RegisterMeetingResponse> {
        @Override
        protected RegisterMeetingResponse doInBackground(RegisterMeetingRequest... requests) {
            try {
                RegisterMeetingRequest request = requests[0];
                return doNetworkAccess(request);
            } catch (IOException e) {
                android.util.Log.e(TAG, "error", e);
                cancel(true);
                return null;
            }
        }

        private RegisterMeetingResponse doNetworkAccess(RegisterMeetingRequest request) throws IOException {
            URL url = new URL("http://10.0.2.2:8080/api/secure/register-meeting");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            String parameter = "parameter=" + request.toJSON();
            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            writer.print(parameter);
            writer.close();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            RegisterMeetingResponse response = (RegisterMeetingResponse) BaseResponse.fromJSON(reader, RegisterMeetingResponse.class);
            return response;
        }

        @Override
        protected void onCancelled() {
            setEnabled(true);
        }

        @Override
        protected void onPostExecute(RegisterMeetingResponse response) {
            setEnabled(true);
        }
    }
}

package com.iped_system.iped;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class MeetingFragment extends Fragment {

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

        MeetingItem item = new MeetingItem();
        item.setText("テスト");
        adapter.add(item);

        return rootView;
    }

    class MessageButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }
}

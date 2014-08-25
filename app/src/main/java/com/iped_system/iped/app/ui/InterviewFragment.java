package com.iped_system.iped.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iped_system.iped.R;

public class InterviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_interview, container, false);

         /* リストビュー */
        ListView interviewListView = (ListView) rootView.findViewById(R.id.interviewListView);
        InterviewAdapter adapter = new InterviewAdapter(getActivity().getApplicationContext(), 0);
        interviewListView.setAdapter(adapter);

        InterviewItem item1 = new InterviewItem();
        item1.setMeText("私の発言です");
        adapter.add(item1);

        InterviewItem item2 = new InterviewItem();
        item2.setYouText("あなたの発言です");
        item2.setAuthorName("あなたの名前");
        adapter.add(item2);

        return rootView;
    }
}

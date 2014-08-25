package com.iped_system.iped.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iped_system.iped.R;

/**
 * Created by kenji on 2014/08/25.
 */
public class InterviewAdapter extends ArrayAdapter<InterviewItem> {
    private LayoutInflater inflater;

    public InterviewAdapter(Context context, int resource) {
        super(context, resource);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InterviewItem item = getItem(position);

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_interview, null);
        }

        RelativeLayout youLayout = (RelativeLayout) convertView.findViewById(R.id.youLayout);
        TextView youTextView = (TextView) convertView.findViewById(R.id.youTextTextView);
        TextView authorNameView = (TextView) convertView.findViewById(R.id.authorNameTextView);
        RelativeLayout meLayout = (RelativeLayout) convertView.findViewById(R.id.meLayout);
        TextView meTextView = (TextView) convertView.findViewById(R.id.meTextTextView);

        youLayout.setVisibility(item.getYouText() == null ? View.GONE : View.VISIBLE);
        youTextView.setText(item.getYouText());
        authorNameView.setText(item.getAuthorName());
        meLayout.setVisibility(item.getMeText() == null ? View.GONE : View.VISIBLE);
        meTextView.setText(item.getMeText());

        return convertView;
    }
}

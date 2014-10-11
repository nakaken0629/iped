package com.iped_system.iped.app.main;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iped_system.iped.R;

import java.util.Calendar;

/**
 * Created by kenji on 2014/08/09.
 */
public class MeetingAdapter extends ArrayAdapter<MeetingItem> {
    private static final String TAG = MeetingAdapter.class.getName();
    private LayoutInflater inflater;

    public MeetingAdapter(Context context, int resource) {
        super(context, resource);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MeetingItem item = getItem(position);

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_meeting, null);
        }

        TextView authorNameTextView = (TextView) convertView.findViewById(R.id.authorNameTextView);
        TextView createdAtTextView = (TextView) convertView.findViewById(R.id.createdAtTextView);
        TextView textTextView = (TextView) convertView.findViewById(R.id.textTextView);

        Log.d(TAG, item.getAuthorName());
        authorNameTextView.setText(item.getAuthorName());
        Log.d(TAG, item.getCreatedAt().toString());
        Calendar createdAt = Calendar.getInstance();
        createdAt.setTime(item.getCreatedAt());
        createdAtTextView.setText(DateFormat.format("yyyy/MM/dd kk:mm", createdAt));
        textTextView.setText(item.getText());

        return convertView;
    }
}

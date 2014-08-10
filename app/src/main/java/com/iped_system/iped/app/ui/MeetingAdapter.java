package com.iped_system.iped.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iped_system.iped.R;

/**
 * Created by kenji on 2014/08/09.
 */
public class MeetingAdapter extends ArrayAdapter<MeetingItem> {
    private LayoutInflater inflater;

    public MeetingAdapter(Context context, int resource) {
        super(context, resource);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MeetingItem item = (MeetingItem) getItem(position);

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_meeting, null);
        }

        TextView textTextView = (TextView) convertView.findViewById(R.id.textTextView);
        textTextView.setText(item.getText());

        return convertView;
    }
}

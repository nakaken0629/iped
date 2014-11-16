package com.iped_system.iped.app.main;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.app.RetainFragment;
import com.iped_system.iped.app.common.os.ImageAsyncTask;

import java.util.Calendar;

/**
 * Created by kenji on 2014/08/25.
 */
public class InterviewAdapter extends ArrayAdapter<TalkItem> {
    private static class ViewHolder {
        private RelativeLayout youLayout;
        private ImageView profileImage;
        private TextView youTextTextView;
        private RelativeLayout meLayout;
        private TextView authorNameTextView;
        private TextView meTextTextView;
        private TextView createdAtTextView;

        private ViewHolder(View view) {
            this.youLayout = (RelativeLayout) view.findViewById(R.id.youLayout);
            this.profileImage = (ImageView) view.findViewById(R.id.profileImageView);
            this.youTextTextView = (TextView) view.findViewById(R.id.youTextTextView);
            this.meLayout = (RelativeLayout) view.findViewById(R.id.meLayout);
            this.authorNameTextView = (TextView) view.findViewById(R.id.authorNameTextView);
            this.meTextTextView = (TextView) view.findViewById(R.id.meTextTextView);
            this.createdAtTextView = (TextView) view.findViewById(R.id.createdAtTextView);
        }
    }

    private static final String TAG = InterviewAdapter.class.getName();
    private LayoutInflater inflater;
    private RetainFragment retainFragment;

    public InterviewAdapter(Context context, int resource, RetainFragment retainFragment) {
        super(context, resource);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.retainFragment = retainFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        TalkItem item = getItem(position);

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_talk, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String youText = item.getYouText();
        if (youText != null && youText.length() > 0) {
            holder.youLayout.setVisibility(View.VISIBLE);
            holder.meLayout.setVisibility(View.GONE);
            holder.youLayout.setVisibility(youText == null || youText.length() == 0 ? View.GONE : View.VISIBLE);
            holder.youTextTextView.setText(youText);
            holder.authorNameTextView.setText(item.getAuthorName());
            holder.profileImage.setImageResource(R.drawable.anonymous);
            holder.profileImage.setTag(item.getFaceId());
            ImageAsyncTask task = new ImageAsyncTask(getContext(), holder.profileImage, this.retainFragment);
            task.execute(item.getFaceId());
        } else {
            holder.youLayout.setVisibility(View.GONE);
            holder.meLayout.setVisibility(View.VISIBLE);
            String meText = item.getMeText();
            holder.meLayout.setVisibility(meText == null || meText.length() == 0 ? View.GONE : View.VISIBLE);
            holder.meTextTextView.setText(meText);
        }
        Calendar createdAt = Calendar.getInstance();
        createdAt.setTime(item.getCreatedAt());
        holder.createdAtTextView.setText(DateFormat.format("yyyy/MM/dd kk:mm", createdAt));

        return convertView;
    }
}

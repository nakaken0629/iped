package com.iped_system.iped.app.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.app.RetainFragment;
import com.iped_system.iped.app.common.os.ImageAsyncTask;

import java.util.Calendar;

/**
 * Created by kenji on 2014/08/09.
 */
public class MeetingAdapter extends ArrayAdapter<MeetingItem> {
    private static class ViewHolder {
        private ImageView profileImage;
        private TextView authorNameTextView;
        private TextView createdAtTextView;
        private TextView textTextView;
        private LinearLayout picturesLayout;

        private ViewHolder(View view) {
            this.profileImage = (ImageView) view.findViewById(R.id.profileImageView);
            this.authorNameTextView = (TextView) view.findViewById(R.id.authorNameTextView);
            this.createdAtTextView = (TextView) view.findViewById(R.id.createdAtTextView);
            this.textTextView = (TextView) view.findViewById(R.id.textTextView);
            this.picturesLayout = (LinearLayout) view.findViewById(R.id.picturesLayout);
        }
    }

    private static final String TAG = MeetingAdapter.class.getName();
    private LayoutInflater inflater;
    private RetainFragment retainFragment;

    public MeetingAdapter(Context context, int resource, RetainFragment retainFragment) {
        super(context, resource);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.retainFragment = retainFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        MeetingItem item = getItem(position);

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_meeting, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.authorNameTextView.setText(item.getAuthorName());
        Calendar createdAt = Calendar.getInstance();
        createdAt.setTime(item.getCreatedAt());
        holder.createdAtTextView.setText(DateFormat.format("yyyy/MM/dd kk:mm", createdAt));
        holder.textTextView.setText(item.getText());
        holder.profileImage.setImageResource(R.drawable.anonymous);
        holder.profileImage.setTag(item.getFaceKey());
        ImageAsyncTask task = new ImageAsyncTask(getContext(), holder.profileImage, this.retainFragment);
        task.execute(item.getFaceKey());
        if (item.getPictureKeys() == null || item.getPictureKeys().size() == 0) {
            holder.picturesLayout.setVisibility(View.GONE);
        } else {
            holder.picturesLayout.setVisibility(View.VISIBLE);
            int size = item.getPictureKeys().size();
            for (int i = holder.picturesLayout.getChildCount(); i < size; i++) {
                holder.picturesLayout.addView(new ImageView(getContext()));
            }
            if (holder.picturesLayout.getChildCount() > size) {
                holder.picturesLayout.removeViews(size, holder.picturesLayout.getChildCount() - size);
            }
            for (int i = 0; i < size; i++) {
                String key2 = item.getPictureKeys().get(i);
                ImageView pictureView = (ImageView) holder.picturesLayout.getChildAt(i);
                pictureView.setTag(key2);
                ImageAsyncTask task2 = new ImageAsyncTask(getContext(), pictureView, this.retainFragment);
                task2.execute(key2);
            }
        }
        return convertView;
    }
}

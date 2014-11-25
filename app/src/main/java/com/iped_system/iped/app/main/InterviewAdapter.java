package com.iped_system.iped.app.main;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iped_system.iped.R;
import com.iped_system.iped.app.common.app.RetainFragment;
import com.iped_system.iped.app.common.os.ImageAsyncTask;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by kenji on 2014/08/25.
 */
public class InterviewAdapter extends ArrayAdapter<TalkItem> {
    private static class ViewHolder {
        private RelativeLayout youLayout;
        private ImageView profileImage;
        private TextView youTextTextView;
        private ImageView youPictogramImageView;
        private ImageView youPictureImageView;
        private RelativeLayout meLayout;
        private TextView authorNameTextView;
        private TextView meTextTextView;
        private ImageView mePictogramImageView;
        private ImageView mePictureImageView;
        private TextView createdAtTextView;

        private ViewHolder(View view) {
            this.youLayout = (RelativeLayout) view.findViewById(R.id.youLayout);
            this.profileImage = (ImageView) view.findViewById(R.id.profileImageView);
            this.youTextTextView = (TextView) view.findViewById(R.id.youTextTextView);
            this.youPictogramImageView = (ImageView) view.findViewById(R.id.youPictogramImageView);
            this.youPictureImageView = (ImageView) view.findViewById(R.id.youPictureImageView);
            this.meLayout = (RelativeLayout) view.findViewById(R.id.meLayout);
            this.authorNameTextView = (TextView) view.findViewById(R.id.authorNameTextView);
            this.meTextTextView = (TextView) view.findViewById(R.id.meTextTextView);
            this.mePictogramImageView = (ImageView) view.findViewById(R.id.mePictogramImageView);
            this.mePictureImageView = (ImageView) view.findViewById(R.id.mePictureImageView);
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
        String youPictogramKey = item.getYouPictogramKey();
        Long youPictureId = item.getYouPictureId();
        String meText = item.getMeText();
        String mePictogramKey = item.getMePictogramKey();
        Long mePictureId = item.getMePictureId();
        if ((youText != null && youText.length() > 0) || (youPictogramKey != null && youPictogramKey.length() > 0) || youPictureId != null) {
            holder.meLayout.setVisibility(View.GONE);
            holder.youLayout.setVisibility(View.VISIBLE);
            holder.youLayout.setVisibility(View.VISIBLE);
            holder.youTextTextView.setVisibility(youText != null && youText.length() > 0 ? View.VISIBLE : View.GONE);
            holder.youTextTextView.setText(youText);
            holder.youPictogramImageView.setVisibility(youPictogramKey != null && youPictogramKey.length() > 0 ? View.VISIBLE : View.GONE);
            holder.youPictogramImageView.setImageResource(getPictogramResourceId(youPictogramKey));
            holder.youPictureImageView.setVisibility(youPictureId != null ? View.VISIBLE : View.GONE);
            if (youPictureId != null) {
                holder.youPictureImageView.setTag(youPictureId);
                ImageAsyncTask youPictureTask = new ImageAsyncTask(getContext(), holder.youPictureImageView, this.retainFragment);
                youPictureTask.execute(youPictureId);
            }
            holder.authorNameTextView.setText(item.getAuthorName());
            holder.profileImage.setImageResource(R.drawable.anonymous);
            if (item.getFaceId() > 0) {
                holder.profileImage.setTag(item.getFaceId());
                ImageAsyncTask faceTask = new ImageAsyncTask(getContext(), holder.profileImage, this.retainFragment);
                faceTask.execute(item.getFaceId());
            }
        } else {
            holder.youLayout.setVisibility(View.GONE);
            holder.meLayout.setVisibility(View.VISIBLE);
            holder.meTextTextView.setVisibility(meText != null && meText.length() > 0 ? View.VISIBLE : View.GONE);
            holder.meTextTextView.setText(meText);
            holder.mePictogramImageView.setVisibility(mePictogramKey != null && mePictogramKey.length() > 0 ? View.VISIBLE : View.GONE);
            holder.mePictogramImageView.setImageResource(getPictogramResourceId(mePictogramKey));
            holder.mePictureImageView.setVisibility(mePictureId != null ? View.VISIBLE : View.GONE);
            if (mePictureId != null) {
                holder.mePictureImageView.setTag(mePictureId);
                ImageAsyncTask mePictureTask = new ImageAsyncTask(getContext(), holder.mePictureImageView, this.retainFragment);
                mePictureTask.execute(mePictureId);
            }
        }
        Calendar createdAt = Calendar.getInstance();
        createdAt.setTime(item.getCreatedAt());
        holder.createdAtTextView.setText(DateFormat.format("yyyy/MM/dd kk:mm", createdAt));

        return convertView;
    }

    private int getPictogramResourceId(String pictogramKey) {
        Field[] fields = R.drawable.class.getFields();
        for(Field field : fields) {
            if (field.getName().equals(pictogramKey)) {
                try {
                    return field.getInt(null);
                } catch (IllegalAccessException e) {
                    return 0;
                }
            }
        }
        return 0;
    }
}

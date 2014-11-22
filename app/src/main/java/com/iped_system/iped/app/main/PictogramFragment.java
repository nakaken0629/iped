package com.iped_system.iped.app.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.iped_system.iped.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PictogramFragment extends DialogFragment {
    private static final String TAG = PictogramFragment.class.getName();

    private final PictogramFragment self = this;
    private OnFragmentInteractionListener mListener;

    public static PictogramFragment newInstance(Fragment fragment) {
        if (!(fragment instanceof OnFragmentInteractionListener)) {
            throw new ClassCastException("fragment need to implements " + OnFragmentInteractionListener.class.getName());
        }
        PictogramFragment pictogramFragment = new PictogramFragment();
        pictogramFragment.setTargetFragment(fragment, 0);
        return pictogramFragment;
    }

    public PictogramFragment() {
        // Required empty public constructor
    }

    private OnFragmentInteractionListener getListener() {
        return (OnFragmentInteractionListener) getTargetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pictogram, container, false);
        rootView.findViewById(R.id.imageView1).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView2).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView3).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView4).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView5).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView6).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView7).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView8).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView9).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView10).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView11).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView12).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView101).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView102).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView102b).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView103).setOnClickListener(new PictogramListener());
        rootView.findViewById(R.id.imageView104).setOnClickListener(new PictogramListener());
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private class PictogramListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String pictogramKey = (String) view.getTag();
            getListener().onFragmentInteraction(pictogramKey);
            self.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String pictogramKey);
    }
}

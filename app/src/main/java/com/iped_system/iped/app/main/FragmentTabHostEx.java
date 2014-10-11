package com.iped_system.iped.app.main;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

/**
 * Created by kenji on 2014/08/09.
 */
public class FragmentTabHostEx extends FragmentTabHost {

    public FragmentTabHostEx(Context context) {
        super(context);
    }

    public FragmentTabHostEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        /* nop for fix a bug. */
    }
}

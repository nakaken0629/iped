package com.iped_system.iped.app.common.widget;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by kenji on 2014/10/11.
 */
public class EditTextEx extends EditText {
    public EditTextEx(Context context) {
        super(context);
    }

    public EditTextEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getTrimmedValue() {
        Editable text = getText();
        return text == null ? null : text.toString();
    }
}

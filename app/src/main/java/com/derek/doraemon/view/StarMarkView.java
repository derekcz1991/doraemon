package com.derek.doraemon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.derek.doraemon.R;

/**
 * Created by derek on 16/7/20.
 */
public class StarMarkView extends FrameLayout {
    public StarMarkView(Context context) {
        this(context, null);
    }

    public StarMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_star_mark, null);
        addView(view);
    }
}

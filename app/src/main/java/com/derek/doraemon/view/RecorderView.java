package com.derek.doraemon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.derek.doraemon.R;

/**
 * Created by derek on 16/10/18.
 */
public class RecorderView extends FrameLayout {

    private TextView timeText;

    public RecorderView(Context context) {
        this(context, null);
    }

    public RecorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_recorder, null);
        timeText = (TextView) view.findViewById(R.id.timeText);
        addView(view);
    }

    public void updateTime(int i) {
        timeText.setText("语音倒计时:" + i + "秒");
    }
}

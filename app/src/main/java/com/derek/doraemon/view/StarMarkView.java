package com.derek.doraemon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.derek.doraemon.R;

/**
 * Created by derek on 16/7/20.
 */
public class StarMarkView extends FrameLayout {
    private TextView[] starText;

    public StarMarkView(Context context) {
        this(context, null);
    }

    public StarMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        starText = new TextView[5];
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_star_mark, null);
        starText[0] = (TextView) view.findViewById(R.id.star1);
        starText[1] = (TextView) view.findViewById(R.id.star2);
        starText[2] = (TextView) view.findViewById(R.id.star3);
        starText[3] = (TextView) view.findViewById(R.id.star4);
        starText[4] = (TextView) view.findViewById(R.id.star5);
        addView(view);
    }

    public void setStar(int starNum) {
        //starNum = starNum % 6;
        for (int i = 0; i < 5; i++) {
            if (i < starNum) {
                starText[i].setBackgroundResource(R.drawable.icon_star_full);
            } else {
                starText[i].setBackgroundResource(R.drawable.icon_star_empty);
            }
        }
    }
}

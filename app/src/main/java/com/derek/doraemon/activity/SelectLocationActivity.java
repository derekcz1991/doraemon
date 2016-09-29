package com.derek.doraemon.activity;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.derek.doraemon.R;

/**
 * Created by derek on 16/7/17.
 */
public class SelectLocationActivity extends BaseTitleActivity {
    @BindView(R.id.gpsLocationText) TextView gpsLocationText;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        View view = View.inflate(this, R.layout.activity_select_location, null);
        getToolbar().setNavigationIcon(R.drawable.icon_nav_blue);
        ButterKnife.bind(this, view);
        return view;
    }
}

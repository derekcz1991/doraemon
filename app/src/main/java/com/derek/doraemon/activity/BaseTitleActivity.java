package com.derek.doraemon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.derek.doraemon.R;

/**
 * Created by derek on 16/7/13.
 */
public abstract class BaseTitleActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView titleText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_title);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (showNavIcon()) {
                toolbar.setNavigationIcon(R.drawable.icon_nav);
            }
            toolbar.setTitle("");
        }
        setSupportActionBar(toolbar);

        titleText = (TextView) findViewById(R.id.titleText);

        FrameLayout containerLayout = (FrameLayout) findViewById(R.id.containerLayout);
        containerLayout.addView(onCreateContentView());
    }

    protected abstract boolean showNavIcon();

    protected abstract View onCreateContentView();

    protected void setTitleText(String title) {
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}

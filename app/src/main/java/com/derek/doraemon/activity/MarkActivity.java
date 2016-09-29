package com.derek.doraemon.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.derek.doraemon.R;
import com.derek.doraemon.view.StarMarkView;

/**
 * Created by derek on 16/8/12.
 */
public class MarkActivity extends BaseTitleActivity {
    @BindView(R.id.totalStarMark) StarMarkView totalStarMark;
    @BindView(R.id.hostStarMark) StarMarkView hostStarMark;
    @BindView(R.id.envStarMark) StarMarkView envStarMark;
    @BindView(R.id.positionStarMark) StarMarkView positionStarMark;
    @BindView(R.id.suggestStarMark) StarMarkView suggestStarMark;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    protected boolean showNavIcon() {
        return false;
    }

    @Override
    protected View onCreateContentView() {
        View view = View.inflate(this, R.layout.activity_mark, null);
        getToolbar().setNavigationIcon(R.drawable.icon_nav_blue);
        ButterKnife.bind(this, view);
        return view;
    }
}

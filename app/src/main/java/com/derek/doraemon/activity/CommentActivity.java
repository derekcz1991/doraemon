package com.derek.doraemon.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.derek.doraemon.R;

/**
 * Created by derek on 16/8/28.
 */
public class CommentActivity extends BaseTitleActivity {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        View view = View.inflate(this, R.layout.activity_comment, null);
        getToolbar().setNavigationIcon(R.drawable.icon_nav_blue);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.cmtBtn)
    public void cmtBtn() {

    }

    @OnClick(R.id.likeBtn)
    public void likeBtn() {

    }
}

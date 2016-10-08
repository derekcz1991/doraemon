package com.derek.doraemon.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.derek.doraemon.R;
import com.derek.doraemon.model.UserDetail;
import com.derek.doraemon.view.StarMarkView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by derek on 16/8/12.
 */
public class MarkActivity extends BaseTitleActivity {
    public static final String EXTRA_USER_DETAIL = "userDetail";

    @BindView(R.id.totalStarMark)
    StarMarkView totalStarMark;
    @BindView(R.id.hostStarMark)
    StarMarkView hostStarMark;
    @BindView(R.id.envStarMark)
    StarMarkView envStarMark;
    @BindView(R.id.positionStarMark)
    StarMarkView positionStarMark;
    @BindView(R.id.suggestStarMark)
    StarMarkView suggestStarMark;
    @BindView(R.id.healthStarMark)
    StarMarkView healthStarMark;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private UserDetail userDetail;

    @Override
    protected boolean showNavIcon() {
        return false;
    }

    @Override
    protected View onCreateContentView() {
        userDetail = (UserDetail) getIntent().getSerializableExtra(EXTRA_USER_DETAIL);

        View view = View.inflate(this, R.layout.activity_mark, null);
        getToolbar().setNavigationIcon(R.drawable.icon_nav_blue);
        setTitleText(userDetail.getRecommend() + "个评分");
        ButterKnife.bind(this, view);

        totalStarMark.setStar(userDetail.getGradeNum());
        hostStarMark.setStar(userDetail.getLoveHeart());
        envStarMark.setStar(userDetail.getEnvironment());
        positionStarMark.setStar(userDetail.getLocation());
        healthStarMark.setStar(userDetail.getHygiene());
        suggestStarMark.setStar(userDetail.getRecommend());
        return view;
    }
}

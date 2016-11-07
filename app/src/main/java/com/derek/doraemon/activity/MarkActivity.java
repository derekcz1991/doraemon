package com.derek.doraemon.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.CommentListAdapter;
import com.derek.doraemon.model.Comment;
import com.derek.doraemon.model.UserDetail;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.view.StarMarkView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by derek on 16/8/12.
 */
public class MarkActivity extends BaseTitleActivity {
    public static final String EXTRA_USER_DETAIL = "userDetail";
    public static final String EXTRA_UID = "uid";

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

        ButterKnife.bind(this, view);

        if (userDetail == null) {
            NetManager.getInstance().getUserDetail(getIntent().getLongExtra(EXTRA_UID, 0))
                .enqueue(new RequestCallback(new RequestCallback.Callback() {
                    @Override
                    public void success(Resp resp) {
                        Gson gson = new Gson();
                        userDetail = gson.fromJson(gson.toJsonTree(resp.getData()), UserDetail.class);
                        setUpView(userDetail);
                    }

                    @Override
                    public boolean fail(Resp resp) {
                        return false;
                    }
                }));
        } else {
            setUpView(userDetail);
        }
        return view;
    }

    private void setUpView(UserDetail userDetail) {
        setBlackTitleText(userDetail.getRecommend() + "个评分");
        totalStarMark.setStar(userDetail.getGradeNum());
        hostStarMark.setStar(userDetail.getLoveHeart());
        envStarMark.setStar(userDetail.getEnvironment());
        positionStarMark.setStar(userDetail.getLocation());
        healthStarMark.setStar(userDetail.getHygiene());
        suggestStarMark.setStar(userDetail.getRecommend());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Comment> comments = userDetail.getComments();
        if (comments != null) {
            CommentListAdapter commentListAdapter = new CommentListAdapter(comments);
            recyclerView.setAdapter(commentListAdapter);
        }

    }
}

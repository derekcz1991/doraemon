package com.derek.doraemon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.CommentListAdapter;
import com.derek.doraemon.model.BaseItem;
import com.derek.doraemon.model.Comment;
import com.derek.doraemon.model.ItemDetail;
import com.derek.doraemon.model.Message;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.derek.doraemon.view.CircleImageView;
import com.derek.doraemon.view.RecorderView;
import com.derek.doraemon.view.StarMarkView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 16/8/12.
 */
public class ItemDetailActivity extends BaseActivity {
    public static final String EXTRA_ITEM = "item";
    public static final String EXTRA_TYPE = "type";

    @BindView(R.id.wallPaper)
    ImageView wallPaper;
    @BindView(R.id.collectBtn)
    FloatingActionButton collectBtn;
    @BindView(R.id.contentText)
    TextView contentText;
    @BindView(R.id.userImageView)
    CircleImageView userImageView;
    @BindView(R.id.hostNameText)
    TextView hostNameText;
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.starMarkView)
    StarMarkView starMarkView;
    @BindView(R.id.starMarkText)
    TextView starMarkText;

    private int type;
    private BaseItem baseItem;
    private List<Comment> comments;
    private CommentListAdapter commentListAdapter;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_detail);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra(EXTRA_TYPE, 1);

        baseItem = (BaseItem) getIntent().getSerializableExtra(EXTRA_ITEM);
        contentText.setText(baseItem.getContent());
        locationText.setText(baseItem.getDistrict());
        Picasso.with(this)
            .load(NetManager.getInstance().getHost() + baseItem.getPhotoUrl())
            .into(wallPaper);
        Picasso.with(this)
            .load(NetManager.getInstance().getHost() + baseItem.getAvatarUrl())
            .placeholder(R.drawable.app_logo)
            .into(userImageView);
        hostNameText.setText(baseItem.getUserName());

        comments = new ArrayList<>();
        commentListAdapter = new CommentListAdapter(comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentListAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        NetManager.getInstance().getCommentList(baseItem.getId(), type).enqueue(
            new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    if (resp.getData() != null) {
                        comments.addAll(
                            (Collection<? extends Comment>) gson.fromJson(gson.toJsonTree(resp.getData()),
                                new TypeToken<List<Comment>>() {
                                }.getType()));
                        commentListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            })
        );

        NetManager.getInstance().getItemDetail(type, baseItem.getId()).enqueue(
            new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    if (resp.getData() != null) {
                        ItemDetail itemDetail = gson.fromJson(gson.toJsonTree(resp.getData()), ItemDetail.class);
                        starMarkText.setVisibility(View.VISIBLE);
                        starMarkText.setText(itemDetail.getUserInfo().getGradeNum() + "个评分");
                        starMarkView.setVisibility(View.VISIBLE);
                        starMarkView.setStar(itemDetail.getUserInfo().getRecommend());
                    }

                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));

    }

    @OnClick(R.id.commentBtn)
    public void onCommentClick() {
        Intent intent = getIntent();
        intent.setClass(this, PubMarkActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.msgBtn)
    public void sendMsg() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHATTERID, baseItem.getUid());
        startActivity(intent);
    }

    @OnClick(R.id.shareBtn)
    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, NetManager.getInstance().getHost() + baseItem.getPhotoUrl());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "分享到"));
    }

    @OnClick(R.id.collectBtn)
    public void collect() {
        NetManager.getInstance()
            .collect(type, baseItem.getId())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    CommonUtils.toast(resp.getMessage());
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }

    @OnClick(R.id.userImageView)
    public void onUserClick() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_UID, baseItem.getUid());
        startActivity(intent);
    }

    @OnClick(R.id.btnBack)
    public void onBackClick() {
        finish();
    }

    @OnClick(R.id.starMarkView)
    public void onStarMarkClick() {
        Intent intent = new Intent(this, MarkActivity.class);
        intent.putExtra(MarkActivity.EXTRA_UID, baseItem.getUid());
        startActivity(intent);
    }
}

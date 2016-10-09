package com.derek.doraemon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.derek.doraemon.view.CircleImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 16/8/12.
 */
public class ItemDetailActivity extends BaseActivity {
    public static final String EXTRA_ITEM = "item";
    public static final String EXTRA_TYPE = "type";

    @BindView(R.id.wallPaper) ImageView wallPaper;
    @BindView(R.id.collectBtn) FloatingActionButton collectBtn;
    @BindView(R.id.contentText) TextView contentText;
    @BindView(R.id.userImageView) CircleImageView userImageView;
    @BindView(R.id.hostNameText) TextView hostNameText;
    @BindView(R.id.locationText) TextView locationText;

    private BaseItem baseItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_detail);
        ButterKnife.bind(this);

        baseItem = (BaseItem) getIntent().getSerializableExtra(EXTRA_ITEM);
        contentText.setText(baseItem.getContent());
        locationText.setText(baseItem.getDistrict());
        Picasso.with(this)
            .load(NetManager.getInstance().getHost() + baseItem.getPhotoUrl())
            .into(wallPaper);
        Picasso.with(this)
            .load(NetManager.getInstance().getHost() + baseItem.getAvatarUrl())
            .into(userImageView);
        hostNameText.setText(baseItem.getUserName());
    }

    @OnClick(R.id.applyBtn)
    public void apply() {

    }

    @OnClick(R.id.msgBtn)
    public void sendMsg() {

    }

    @OnClick(R.id.collectBtn)
    public void collect() {
        NetManager.getInstance()
            .collect(getIntent().getStringExtra(EXTRA_TYPE), baseItem.getId())
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
}

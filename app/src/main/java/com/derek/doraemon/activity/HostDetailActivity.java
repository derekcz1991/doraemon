package com.derek.doraemon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.derek.doraemon.R;
import com.derek.doraemon.model.HostItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.derek.doraemon.view.CircleImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by derek on 16/8/12.
 */
public class HostDetailActivity extends BaseActivity {
    public static final String EXTRA_HOST = "hostItem";

    @BindView(R.id.wallPaper) ImageView wallPaper;
    @BindView(R.id.collectBtn) FloatingActionButton collectBtn;
    @BindView(R.id.contentText) TextView contentText;
    @BindView(R.id.userImageView) CircleImageView userImageView;
    @BindView(R.id.hostNameText) TextView hostNameText;
    @BindView(R.id.locationText) TextView locationText;

    private HostItem hostItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_detail);
        ButterKnife.bind(this);

        hostItem = (HostItem) getIntent().getSerializableExtra(EXTRA_HOST);
        contentText.setText(hostItem.getContent());
        Picasso.with(this)
            .load(NetManager.getInstance().getHost() + hostItem.getPhotoUrl())
            .into(wallPaper);
        Picasso.with(this)
            .load(NetManager.getInstance().getHost() + hostItem.getAvatarUrl())
            .into(userImageView);
        hostNameText.setText(hostItem.getUserName());
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
            .collect("1", hostItem.getId())
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

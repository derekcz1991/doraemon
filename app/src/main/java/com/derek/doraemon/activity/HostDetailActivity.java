package com.derek.doraemon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.derek.doraemon.R;
import com.derek.doraemon.view.CircleImageView;

/**
 * Created by derek on 16/8/12.
 */
public class HostDetailActivity extends BaseActivity {
    @BindView(R.id.favFab) FloatingActionButton favFab;
    @BindView(R.id.contentText) TextView contentText;
    @BindView(R.id.userImageView) CircleImageView userImageView;
    @BindView(R.id.hostNameText) TextView hostNameText;
    @BindView(R.id.locationText) TextView locationText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_detail);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.applyBtn)
    public void apply() {

    }

    @OnClick(R.id.msgBtn)
    public void sendMsg() {

    }
}

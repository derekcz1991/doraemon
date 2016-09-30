package com.derek.doraemon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.derek.doraemon.R;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;

/**
 * Created by derek on 16/8/18.
 */
public class PublishActivity extends BaseActivity {
    @BindView(R.id.postImg) TextView postImg;
    @BindView(R.id.contentText) TextView contentText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.pubBtn)
    public void publish() {
        if (TextUtils.isEmpty(contentText.getText().toString())) {
            CommonUtils.toast("写点什么...");
            return;
        }

    }

    @OnClick(R.id.cancelBtn)
    public void cancelBtn() {
        finish();
    }

    @OnClick(R.id.cameraBtn)
    public void takeCamera() {

    }

    @OnClick(R.id.photoBtn)
    public void selectPhoto() {

    }

    @OnClick(R.id.adoptBtn)
    public void checkAdopt() {

    }

    @OnClick(R.id.breedBtn)
    public void checkBreed() {

    }

    @OnClick(R.id.findBtn)
    public void checkFind() {

    }

    @OnClick(R.id.qaBtn)
    public void checkQa() {

    }
}
package com.derek.doraemon.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.derek.doraemon.R;

/**
 * Created by derek on 16/8/18.
 */
public class PublishActivity extends BaseActivity {
    /*@BindView(R.id.adoptBtn) ImageView adoptBtn;
    @BindView(R.id.breedBtn) ImageView breedBtn;
    @BindView(R.id.findBtn) ImageView findBtn;
    @BindView(R.id.qaBtn) ImageView qaBtn;*/
    @BindView(R.id.contentText) TextView contentText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
    }

    @OnClick(R.id.pubBtn)
    public void publish() {

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

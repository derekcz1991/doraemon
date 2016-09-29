package com.derek.doraemon.activity;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.derek.doraemon.R;
import com.derek.doraemon.view.CircleImageView;
import com.derek.doraemon.view.StarMarkView;

/**
 * Created by derek on 16/8/12.
 */
public class ProfileActivity extends BaseTitleActivity {
    @BindView(R.id.userImageView) CircleImageView userImageView;
    @BindView(R.id.hostNameText) TextView hostNameText;
    @BindView(R.id.starMarkView) StarMarkView starMarkView;
    @BindView(R.id.starText) TextView starText;
    @BindView(R.id.petNameText) TextView petNameText;
    @BindView(R.id.petAgeText) TextView petAgeText;
    @BindView(R.id.petTypeText) TextView petTypeText;
    @BindView(R.id.petCategoryText) TextView petCategoryText;
    @BindView(R.id.jobText) TextView jobText;
    @BindView(R.id.constellationText) TextView constellationText;
    @BindView(R.id.emailText) TextView emailText;
    @BindView(R.id.introText) TextView introText;

    @Override
    protected boolean showNavIcon() {
        return false;
    }

    @Override
    protected View onCreateContentView() {
        View view = View.inflate(this, R.layout.activity_profile, null);
        getToolbar().setNavigationIcon(R.drawable.icon_nav_blue);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.msgBtn)
    public void sendMsg() {

    }

    @OnClick(R.id.helloBtn)
    public void sayHi() {

    }
}

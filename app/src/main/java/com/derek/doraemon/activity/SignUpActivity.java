package com.derek.doraemon.activity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.derek.doraemon.R;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;

/**
 * Created by derek on 16/7/14.
 */
public class SignUpActivity extends BaseTitleActivity {

    @BindView(R.id.petNameText) TextView petNameText;
    @BindView(R.id.petAgeText) TextView petAgeText;
    @BindView(R.id.petTypeText) TextView petTypeText;
    @BindView(R.id.petCategoryText) TextView petCategoryText;
    @BindView(R.id.nickNameText) TextView nickNameText;
    @BindView(R.id.jobText) TextView jobText;
    @BindView(R.id.constellationText) TextView constellationText;
    @BindView(R.id.emailText) TextView emailText;
    @BindView(R.id.introText) TextView introText;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        setTitleText("完善个人信息");
        View view = View.inflate(this, R.layout.activity_sign_up, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.startBtn)
    public void submit() {
        if (petNameText.getText().toString().isEmpty()
            || petAgeText.getText().toString().isEmpty()
            || petTypeText.getText().toString().isEmpty()
            || nickNameText.getText().toString().isEmpty()
            || introText.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
            return;
        }

        NetManager.getInstance()
            .completeUserInfo("1", petTypeText.getText().toString(),
                petCategoryText.getText().toString(), petNameText.getText().toString(),
                petAgeText.getText().toString(), nickNameText.getText().toString(),
                jobText.getText().toString(), constellationText.getText().toString(),
                introText.getText().toString())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {

                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }
}

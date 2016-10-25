package com.derek.doraemon.activity;

import android.view.View;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 16/10/22.
 */
public class FeedbackActivity extends BaseTitleActivity {

    @BindView(R.id.contentText)
    TextView contentText;
    @BindView(R.id.phoneText)
    TextView phoneText;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        setTitleText("用户反馈");
        View view = View.inflate(this, R.layout.activity_feedback, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.submitBtn)
    public void submit() {
        if (contentText.getText().toString().isEmpty()) {
            CommonUtils.toast("请填写意见");
            return;
        }
        if (phoneText.getText().toString().isEmpty()) {
            CommonUtils.toast("请填写联系方式");
            return;
        }

        NetManager.getInstance().feedback(contentText.getText().toString()).enqueue(
            new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    CommonUtils.toast(resp.getMessage());
                    finish();
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            })
        );
    }

}

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
public class WriteCommentActivity extends BaseActivity {
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_POST_ID = "postId";

    @BindView(R.id.contentText) TextView contentText;

    private String type;
    private long postId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        ButterKnife.bind(this);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        postId = getIntent().getLongExtra(EXTRA_POST_ID, -1);
    }

    @OnClick(R.id.pubBtn)
    public void publish() {
        if (TextUtils.isEmpty(contentText.getText().toString())) {
            CommonUtils.toast("写点什么...");
            return;
        }

        NetManager.getInstance()
            .comment(type, postId, contentText.getText().toString())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    CommonUtils.toast(resp.getMessage());
                    finish();
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }

    @OnClick(R.id.cancelBtn)
    public void cancelBtn() {
        finish();
    }
}

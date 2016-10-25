package com.derek.doraemon.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by derek on 16/10/22.
 */
public class PubMarkActivity extends BaseTitleActivity implements View.OnClickListener {

    @BindView(R.id.contentText)
    TextView contentText;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.star1)
    CheckedTextView start1;
    @BindView(R.id.star2)
    CheckedTextView start2;
    @BindView(R.id.star3)
    CheckedTextView start3;
    @BindView(R.id.star4)
    CheckedTextView start4;
    @BindView(R.id.star5)
    CheckedTextView start5;

    private BaseItem baseItem;
    private int grade = 0;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        setTitleText("发表评价");
        View view = View.inflate(this, R.layout.activity_pub_mark, null);
        ButterKnife.bind(this, view);

        baseItem = (BaseItem) getIntent().getSerializableExtra(ItemDetailActivity.EXTRA_ITEM);

        Picasso.with(this)
            .load(NetManager.getInstance().getHost() + baseItem.getPhotoUrl())
            .into(imageView);

        start1.setOnClickListener(this);
        start2.setOnClickListener(this);
        start3.setOnClickListener(this);
        start4.setOnClickListener(this);
        start5.setOnClickListener(this);
        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (grade == 0) {
            CommonUtils.toast("请评分");
            return false;
        }
        NetManager.getInstance().evaluate(baseItem.getUid(), grade, contentText.getText().toString()).enqueue(
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
            }));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star1:
                grade = 1;
                start1.setChecked(true);
                start2.setChecked(false);
                start3.setChecked(false);
                start4.setChecked(false);
                start5.setChecked(false);
                break;
            case R.id.star2:
                grade = 2;
                start1.setChecked(true);
                start2.setChecked(true);
                start3.setChecked(false);
                start4.setChecked(false);
                start5.setChecked(false);
                break;
            case R.id.star3:
                grade = 3;
                start1.setChecked(true);
                start2.setChecked(true);
                start3.setChecked(true);
                start4.setChecked(false);
                start5.setChecked(false);
                break;
            case R.id.star4:
                grade = 4;
                start1.setChecked(true);
                start2.setChecked(true);
                start3.setChecked(true);
                start4.setChecked(true);
                start5.setChecked(false);
                break;
            case R.id.star5:
                grade = 5;
                start1.setChecked(true);
                start2.setChecked(true);
                start3.setChecked(true);
                start4.setChecked(true);
                start5.setChecked(true);
                break;
        }
    }
}

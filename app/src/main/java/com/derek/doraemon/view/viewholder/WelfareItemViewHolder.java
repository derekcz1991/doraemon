package com.derek.doraemon.view.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.activity.ItemDetailActivity;
import com.derek.doraemon.activity.WriteCommentActivity;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.model.WelfareItem;
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
public class WelfareItemViewHolder extends BaseViewHolder {
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.wallPaper)
    ImageView wallPaper;
    @BindView(R.id.userImageView)
    CircleImageView userImageView;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.typeText)
    TextView typeText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.contentText)
    TextView contentText;
    @BindView(R.id.likeBtn)
    TextView likeBtn;
    @BindView(R.id.commentBtn)
    TextView commentBtn;
    @BindView(R.id.msgNumText)
    TextView msgNumText;
    @BindView(R.id.favNumText)
    TextView favNumText;

    private WelfareItem welfareItem;

    public WelfareItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {
        welfareItem = (WelfareItem) data;
        locationText.setText(welfareItem.getDistrict());
        Picasso.with(context)
            .load(NetManager.getInstance().getHost() + welfareItem.getPhotoUrl())
            .into(wallPaper);
        Picasso.with(context)
            .load(NetManager.getInstance().getHost() + welfareItem.getAvatarUrl())
            .into(userImageView);
        nameText.setText(welfareItem.getUserName());
        if (welfareItem.getKind() == 1) {
            typeText.setText("随手拍流浪狗");
        } else if (welfareItem.getKind() == 2) {
            typeText.setText("寻宠");
        } else if (welfareItem.getKind() == 3) {
            typeText.setText("领养");
        }
        timeText.setText(welfareItem.getCreatedAt());
        contentText.setText(welfareItem.getContent());
        msgNumText.setText(String.valueOf(welfareItem.getTotalComment()));
        favNumText.setText(String.valueOf(welfareItem.getTotalLike()));
    }

    @OnClick(R.id.likeBtn)
    public void star() {
        NetManager.getInstance()
            .star("1", welfareItem.getId())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    CommonUtils.toast(resp.getMessage());
                    welfareItem.setTotalLike(welfareItem.getTotalLike() + 1);
                    favNumText.setText(String.valueOf(welfareItem.getTotalLike()));
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }

    @OnClick(R.id.commentBtn)
    public void comment() {
        Intent intent = new Intent(context, WriteCommentActivity.class);
        intent.putExtra(WriteCommentActivity.EXTRA_POST_ID, welfareItem.getId());
        intent.putExtra(WriteCommentActivity.EXTRA_TYPE, "1");
        context.startActivity(intent);
    }

    @OnClick(R.id.nextPageBtn)
    public void detail() {
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.EXTRA_ITEM, welfareItem);
        intent.putExtra(ItemDetailActivity.EXTRA_TYPE, "3");
        context.startActivity(intent);
    }
}

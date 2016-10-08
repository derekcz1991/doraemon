package com.derek.doraemon.view.viewholder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.activity.WriteCommentActivity;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.model.MomentItem;
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
 * Created by derek on 08/10/2016.
 */
public class MomentViewHolder extends BaseViewHolder {
    @BindView(R.id.userImageView)
    CircleImageView userImageView;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.petImageView)
    ImageView petImageView;
    @BindView(R.id.contentText)
    TextView contentText;
    @BindView(R.id.msgNumText)
    TextView msgNumText;
    @BindView(R.id.favNumText)
    TextView favNumText;

    private MomentItem momentItem;

    public MomentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {
        momentItem = (MomentItem) data;
        if (TextUtils.isEmpty(momentItem.getPhotoUrl())) {
            petImageView.setVisibility(View.GONE);
        } else {
            petImageView.setVisibility(View.VISIBLE);
            Picasso.with(context)
                .load(NetManager.getInstance().getHost() + momentItem.getPhotoUrl())
                .into(petImageView);
        }
        Picasso.with(context)
            .load(NetManager.getInstance().getHost() + momentItem.getAvatarUrl())
            .into(userImageView);
        nameText.setText(momentItem.getUserName());
        timeText.setText(momentItem.getCreatedAt());
        contentText.setText(momentItem.getContent());
        msgNumText.setText(String.valueOf(momentItem.getTotalComment()));
        favNumText.setText(String.valueOf(momentItem.getTotalLike()));
    }

    @OnClick(R.id.likeBtn)
    public void star() {
        NetManager.getInstance()
            .star("1", momentItem.getId())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    CommonUtils.toast(resp.getMessage());
                    momentItem.setTotalLike(momentItem.getTotalLike() + 1);
                    favNumText.setText(String.valueOf(momentItem.getTotalLike()));
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
        intent.putExtra(WriteCommentActivity.EXTRA_POST_ID, momentItem.getId());
        intent.putExtra(WriteCommentActivity.EXTRA_TYPE, "1");
        context.startActivity(intent);
    }

}

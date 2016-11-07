package com.derek.doraemon.view.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.derek.doraemon.MyApplication;
import com.derek.doraemon.R;
import com.derek.doraemon.activity.ChatActivity;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.model.Comment;
import com.derek.doraemon.model.Message;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.view.CircleImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by derek on 08/10/2016.
 */
public class CommentViewHolder extends BaseViewHolder {
    @BindView(R.id.userImageView)
    CircleImageView userImageView;
    @BindView(R.id.userNameText)
    TextView userNameText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.contentText)
    TextView contentText;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {
        final Comment comment = (Comment) data;
        Picasso.with(context)
            .load(NetManager.getInstance().getHost() + comment.getAvatar())
            .placeholder(R.drawable.app_logo)
            .into(userImageView);
        userNameText.setText(comment.getUserName());
        timeText.setText(comment.getUpdatedAt());
        contentText.setText(comment.getComment());
    }
}

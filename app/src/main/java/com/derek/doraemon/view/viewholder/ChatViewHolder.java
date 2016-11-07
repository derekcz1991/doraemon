package com.derek.doraemon.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.model.Chat;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.view.CircleImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by derek on 10/6/16.
 */
public class ChatViewHolder extends BaseViewHolder {
    private CircleImageView userImageView;
    private TextView userNameText;
    private TextView msgText;

    public ChatViewHolder(View itemView) {
        super(itemView);
        userImageView = (CircleImageView) itemView.findViewById(R.id.userImageView);
        userNameText = (TextView) itemView.findViewById(R.id.userNameText);
        msgText = (TextView) itemView.findViewById(R.id.msgText);
    }

    @Override
    public void update(BaseModel data) {
        Chat.ChatInfo chatInfo = (Chat.ChatInfo) data;
        Picasso.with(context)
            .load(NetManager.getInstance().getHost() + chatInfo.getChatUser().getAvatarUrl())
            .placeholder(R.drawable.app_logo)
            .into(userImageView);
        userNameText.setText(chatInfo.getChatUser().getUserName());
        msgText.setText(chatInfo.getContent());
    }
}

package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.model.Chat;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.ChatViewHolder;

/**
 * Created by derek on 10/6/16.
 */
public class ChatListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Chat chat;

    public ChatListAdapter(Chat chat) {
        this.chat = chat;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ChatViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_left_msg, null));
        } else {
            return new ChatViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_right_msg, null));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            chat.getChatInfoList().get(position).setChatUser(chat.getTo());
        } else {
            chat.getChatInfoList().get(position).setChatUser(chat.getFrom());
        }
        holder.update(chat.getChatInfoList().get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (chat.getChatInfoList().get(position).getMsgFrom() == NetManager.getInstance().getUid()) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return chat == null || chat.getChatInfoList() == null ? 0 : chat.getChatInfoList().size();
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        notifyDataSetChanged();
    }
}

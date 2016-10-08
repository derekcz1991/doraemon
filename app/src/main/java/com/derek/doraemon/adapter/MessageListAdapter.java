package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.model.Message;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.MessageViewHolder;

import java.util.List;

/**
 * Created by derek on 08/10/2016.
 */
public class MessageListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    List<Message> messages;

    public MessageListAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_message, null));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }
}

package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.model.Comment;
import com.derek.doraemon.model.Message;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.CommentViewHolder;
import com.derek.doraemon.view.viewholder.MessageViewHolder;

import java.util.List;

/**
 * Created by derek on 08/10/2016.
 */
public class CommentListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    List<Comment> comments;

    public CommentListAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_commet, null));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }
}

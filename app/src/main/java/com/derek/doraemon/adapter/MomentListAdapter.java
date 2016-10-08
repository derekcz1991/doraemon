package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.model.MomentItem;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.MomentViewHolder;

import java.util.List;

/**
 * Created by derek on 08/10/2016.
 */
public class MomentListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<MomentItem> momentItems;

    public MomentListAdapter(List<MomentItem> momentItems) {
        this.momentItems = momentItems;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MomentViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_moment, null));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(momentItems.get(position));
    }

    @Override
    public int getItemCount() {
        return momentItems == null ? 0 : momentItems.size();
    }
}

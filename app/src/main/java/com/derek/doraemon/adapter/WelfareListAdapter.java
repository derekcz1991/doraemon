package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.model.WelfareItem;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.WelfareItemViewHolder;

import java.util.List;

/**
 * Created by derek on 2016/9/29.
 */
public class WelfareListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<WelfareItem> WelfareItems;

    public WelfareListAdapter(List<WelfareItem> WelfareItems) {
        this.WelfareItems = WelfareItems;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WelfareItemViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_welfare, null));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(WelfareItems.get(position));
    }

    @Override
    public int getItemCount() {
        return WelfareItems == null ? 0 : WelfareItems.size();
    }

}

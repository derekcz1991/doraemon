package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.derek.doraemon.R;
import com.derek.doraemon.model.HostItem;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.HostItemViewHolder;
import java.util.List;

/**
 * Created by derek on 2016/9/29.
 */
public class HostListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<HostItem> hostItems;

    public HostListAdapter(List<HostItem> hostItems) {
        this.hostItems = hostItems;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HostItemViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_host, null));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(hostItems.get(position));
    }

    @Override
    public int getItemCount() {
        return hostItems == null ? 0 : hostItems.size();
    }

}

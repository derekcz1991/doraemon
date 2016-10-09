package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.model.NearbyItem;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.NearbyViewHolder;

import java.util.List;

/**
 * Created by derek on 09/10/2016.
 */
public class NearbyListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    List<NearbyItem> nearbyItems;

    public NearbyListAdapter(List<NearbyItem> nearbyItems) {
        this.nearbyItems = nearbyItems;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NearbyViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_nearby, null));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(nearbyItems.get(position));
    }

    @Override
    public int getItemCount() {
        return nearbyItems == null ? 0 : nearbyItems.size();
    }
}

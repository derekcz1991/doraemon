package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.model.FavItem;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.FavViewHolder;

import java.util.List;

/**
 * Created by derek on 09/10/2016.
 */
public class FavListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<FavItem> favItems;

    public FavListAdapter(List<FavItem> favItems) {
        this.favItems = favItems;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_fav, null),
            new FavViewHolder.Callback() {
                @Override
                public void onItemDelete(int position) {
                    if (position < favItems.size()) {
                        favItems.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(favItems.get(position));
    }

    @Override
    public int getItemCount() {
        return favItems == null ? 0 : favItems.size();
    }

}

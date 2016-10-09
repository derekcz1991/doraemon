package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseItem;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.MyItemViewHolder;

import java.util.List;

/**
 * Created by derek on 08/10/2016.
 */
public class MyItemListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<BaseItem> baseItems;
    private int type;

    public MyItemListAdapter(List<BaseItem> baseItems, int type) {
        this.baseItems = baseItems;
        this.type = type;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyItemViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_my_item, null),
            new MyItemViewHolder.Callback() {
                @Override
                public void onItemDelete(int position) {
                    if (position < baseItems.size()) {
                        baseItems.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        baseItems.get(position).setItemType(type);
        holder.update(baseItems.get(position));
    }

    @Override
    public int getItemCount() {
        return baseItems == null ? 0 : baseItems.size();
    }
}

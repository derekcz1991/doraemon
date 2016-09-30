package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.derek.doraemon.R;
import com.derek.doraemon.model.StarUser;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.HostItemViewHolder;
import com.derek.doraemon.view.viewholder.StarUserViewHolder;
import java.util.List;

/**
 * Created by derek on 16/8/11.
 */
public class StarUserAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<StarUser> starUsers;

    public StarUserAdapter(List<StarUser> starUsers) {
        this.starUsers = starUsers;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StarUserViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_star, null));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(starUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return starUsers == null ? 0 : starUsers.size();
    }
}

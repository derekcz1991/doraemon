package com.derek.doraemon.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.derek.doraemon.model.BaseModel;

/**
 * Created by derek on 16/8/11.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    protected Context context;

    public BaseViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    public abstract void update(BaseModel data);
}

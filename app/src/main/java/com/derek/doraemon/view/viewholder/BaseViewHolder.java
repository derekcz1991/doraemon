package com.derek.doraemon.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.derek.doraemon.model.BaseModel;

/**
 * Created by derek on 16/8/11.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void update(BaseModel data);
}

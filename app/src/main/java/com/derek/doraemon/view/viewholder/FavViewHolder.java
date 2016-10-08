package com.derek.doraemon.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.view.StarMarkView;

/**
 * Created by derek on 2016/10/9.
 */
public class FavViewHolder extends BaseViewHolder {
    @BindView(R.id.petImageView) ImageView petImageView;
    @BindView(R.id.userNameText) TextView userNameText;
    @BindView(R.id.starMarkView) StarMarkView starMarkView;


    public FavViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void update(BaseModel data) {

    }
}

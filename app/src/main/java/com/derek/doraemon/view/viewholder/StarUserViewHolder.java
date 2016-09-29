package com.derek.doraemon.view.viewholder;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.view.CircleImageView;
import com.derek.doraemon.view.SquaredImageView;
import com.derek.doraemon.view.StarMarkView;

/**
 * Created by derek on 16/8/11.
 */
public class StarUserViewHolder extends BaseViewHolder {
    @BindView(R.id.petImageView) SquaredImageView petImageView;
    @BindView(R.id.userImageView) CircleImageView userImageView;
    @BindView(R.id.starMarkView) StarMarkView starMarkView;
    @BindView(R.id.starText) TextView starText;

    public StarUserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {

    }
}

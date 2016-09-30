package com.derek.doraemon.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.model.StarUser;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.view.CircleImageView;
import com.derek.doraemon.view.SquaredImageView;
import com.derek.doraemon.view.StarMarkView;
import com.squareup.picasso.Picasso;

/**
 * Created by derek on 16/8/11.
 */
public class StarUserViewHolder extends BaseViewHolder {
    @BindView(R.id.petImageView) ImageView petImageView;
    @BindView(R.id.userImageView) CircleImageView userImageView;
    @BindView(R.id.starMarkView) StarMarkView starMarkView;
    @BindView(R.id.starText) TextView starText;

    public StarUserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {
        StarUser starUser = (StarUser) data;
        Picasso.with(itemView.getContext())
            .load(NetManager.getInstance().getHost() + starUser.getAvatarUrl())
            .into(userImageView);

        Picasso.with(itemView.getContext())
            .load(NetManager.getInstance().getHost() + starUser.getAvatarUrl())
            .into(petImageView);

        starMarkView.setStar(starUser.getRecommendNum());
        starText.setText(String.valueOf(starUser.getRecommendNum()));
    }
}

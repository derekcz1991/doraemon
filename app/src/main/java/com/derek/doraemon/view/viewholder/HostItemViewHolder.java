package com.derek.doraemon.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.view.CircleImageView;

/**
 * Created by derek on 16/8/12.
 */
public class HostItemViewHolder extends BaseViewHolder {
    @BindView(R.id.locationText) TextView locationText;
    @BindView(R.id.favBtn) ImageView favBtn;
    @BindView(R.id.wallPaper) ImageView wallPaper;
    @BindView(R.id.userImageView) CircleImageView userImageView;
    @BindView(R.id.nameText) TextView nameText;
    @BindView(R.id.timeText) TextView timeText;
    @BindView(R.id.contentText) TextView contentText;
    @BindView(R.id.likeBtn) TextView likeBtn;
    @BindView(R.id.commentBtn) TextView commentBtn;
    @BindView(R.id.msgNumText) TextView msgNumText;
    @BindView(R.id.favNumText) TextView favNumText;

    public HostItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {

    }
}

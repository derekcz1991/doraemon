package com.derek.doraemon.view.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.activity.ProfileActivity;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.model.NearbyItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.view.CircleImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by derek on 09/10/2016.
 */
public class NearbyViewHolder extends BaseViewHolder {
    @BindView(R.id.userNameText)
    TextView userNameText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.userImageView)
    CircleImageView userImageView;

    public NearbyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {
        final NearbyItem nearbyItem = (NearbyItem) data;
        Picasso.with(itemView.getContext())
            .load(NetManager.getInstance().getHost() + nearbyItem.getAvatarUrl())
            .into(userImageView);

        userNameText.setText(nearbyItem.getUserName());
        timeText.setText(nearbyItem.getUpdatedAt());
        locationText.setText(nearbyItem.getAddress());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_UID, nearbyItem.getUid());
                context.startActivity(intent);
            }
        });
    }
}

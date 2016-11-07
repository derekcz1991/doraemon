package com.derek.doraemon.view.viewholder;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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
    @BindView(R.id.btnBark)
    ImageView btnBark;

    private Callback callback;

    public interface Callback {
        void onViewPressed(long uid);

        void onViewRelease();
    }

    public NearbyViewHolder(View itemView, Callback callback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.callback = callback;
    }

    @Override
    public void update(BaseModel data) {
        final NearbyItem nearbyItem = (NearbyItem) data;
        Picasso.with(itemView.getContext())
            .load(NetManager.getInstance().getHost() + nearbyItem.getAvatarUrl())
            .placeholder(R.drawable.app_logo)
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

        btnBark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        callback.onViewPressed(nearbyItem.getUid());
                        break;
                    case MotionEvent.ACTION_UP:
                        callback.onViewRelease();
                        break;
                }
                return true;
            }
        });
    }
}

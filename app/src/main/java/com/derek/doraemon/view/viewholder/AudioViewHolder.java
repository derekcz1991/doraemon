package com.derek.doraemon.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.model.Audio;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.view.CircleImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by derek on 16/10/20.
 */
public class AudioViewHolder extends BaseViewHolder {
    @BindView(R.id.userImageView)
    CircleImageView userImageView;
    @BindView(R.id.userNameText)
    TextView userNameText;

    private Callback callback;

    public interface Callback {
        void onItemClicked(Audio audio);
    }

    public AudioViewHolder(View itemView, Callback callback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.callback = callback;
    }

    @Override
    public void update(BaseModel data) {
        final Audio audio = (Audio) data;
        Picasso.with(context)
            .load(NetManager.getInstance().getHost() + audio.getAvatarUrl())
            .into(userImageView);
        userNameText.setText(audio.getUserName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClicked(audio);
            }
        });
    }
}

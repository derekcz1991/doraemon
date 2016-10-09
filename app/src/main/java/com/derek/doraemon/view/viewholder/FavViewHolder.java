package com.derek.doraemon.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.model.FavItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.derek.doraemon.view.StarMarkView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 2016/10/9.
 */
public class FavViewHolder extends BaseViewHolder {
    @BindView(R.id.petImageView)
    ImageView petImageView;
    @BindView(R.id.userNameText)
    TextView userNameText;
    @BindView(R.id.starMarkView)
    StarMarkView starMarkView;
    @BindView(R.id.starText)
    TextView starText;

    private FavItem favItem;
    private Callback callback;

    public interface Callback {
        void onItemDelete(int position);
    }

    public FavViewHolder(View itemView, Callback callback) {
        super(itemView);
        this.callback = callback;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {
        favItem = (FavItem) data;
        Picasso.with(itemView.getContext())
            .load(NetManager.getInstance().getHost() + favItem.getPostDetail().getUserInfo().getAvatarUrl())
            .into(petImageView);
        userNameText.setText(favItem.getPostDetail().getUserInfo().getUserName());
        starMarkView.setStar(favItem.getPostDetail().getUserInfo().getRecommend());
        starText.setText(favItem.getPostDetail().getUserInfo().getRecommend() + "个评分");
    }

    @OnClick(R.id.deleteBtn)
    public void cancelCollection() {
        NetManager.getInstance().cancelCollection(favItem.getId()).enqueue(
            new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    CommonUtils.toast(resp.getMessage());
                    callback.onItemDelete(getAdapterPosition());
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }
}

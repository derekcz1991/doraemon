package com.derek.doraemon.view.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.model.BaseItem;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 08/10/2016.
 */
public class MyItemViewHolder extends BaseViewHolder {
    @BindView(R.id.petImageView)
    ImageView petImageView;
    @BindView(R.id.contentText)
    TextView contentText;
    @BindView(R.id.msgNumText)
    TextView msgNumText;
    @BindView(R.id.favNumText)
    TextView favNumText;

    private BaseItem baseItem;
    private Callback callback;

    public interface Callback {
        void onItemDelete(int position);
    }

    public MyItemViewHolder(View itemView, Callback callback) {
        super(itemView);
        this.callback = callback;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {
        baseItem = (BaseItem) data;
        if (TextUtils.isEmpty(baseItem.getPhotoUrl())) {

        } else {
            petImageView.setVisibility(View.VISIBLE);
            Picasso.with(context)
                .load(NetManager.getInstance().getHost() + baseItem.getPhotoUrl())
                .into(petImageView);
        }
        contentText.setText(baseItem.getContent());
        msgNumText.setText(String.valueOf(baseItem.getTotalComment()));
        favNumText.setText(String.valueOf(baseItem.getTotalLike()));
    }

    @OnClick(R.id.deleteBtn)
    public void deleteBtn() {
        if (baseItem.getItemType() == 0) {
            return;
        }
        NetManager.getInstance().deletePublishedItem(baseItem.getItemType(), baseItem.getId()).enqueue(
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

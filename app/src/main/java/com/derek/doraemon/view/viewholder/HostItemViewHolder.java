package com.derek.doraemon.view.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.activity.ItemDetailActivity;
import com.derek.doraemon.activity.WriteCommentActivity;
import com.derek.doraemon.model.BaseModel;
import com.derek.doraemon.model.HostItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.derek.doraemon.view.CircleImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 16/8/12.
 */
public class HostItemViewHolder extends BaseViewHolder {
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.collectBtn)
    ImageView collectBtn;
    @BindView(R.id.wallPaper)
    ImageView wallPaper;
    @BindView(R.id.userImageView)
    CircleImageView userImageView;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.contentText)
    TextView contentText;
    @BindView(R.id.likeBtn)
    TextView likeBtn;
    @BindView(R.id.commentBtn)
    TextView commentBtn;
    @BindView(R.id.msgNumText)
    TextView msgNumText;
    @BindView(R.id.favNumText)
    TextView favNumText;

    private HostItem hostItem;

    public HostItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void update(BaseModel data) {
        hostItem = (HostItem) data;
        locationText.setText(hostItem.getDistrict());
        Picasso.with(context)
            .load(NetManager.getInstance().getHost() + hostItem.getPhotoUrl())
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .resize(600, 200)
            .centerCrop()
            .into(wallPaper);
        Picasso.with(context)
            .load(NetManager.getInstance().getHost() + hostItem.getAvatarUrl())
            .resize(100, 100)
            .into(userImageView);
        nameText.setText(hostItem.getUserName());
        timeText.setText(hostItem.getCreatedAt());
        contentText.setText(hostItem.getContent());
        msgNumText.setText(String.valueOf(hostItem.getTotalComment()));
        favNumText.setText(String.valueOf(hostItem.getTotalLike()));
    }

    @OnClick(R.id.likeBtn)
    public void star() {
        NetManager.getInstance()
            .star("1", hostItem.getId())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    CommonUtils.toast(resp.getMessage());
                    hostItem.setTotalLike(hostItem.getTotalLike() + 1);
                    favNumText.setText(String.valueOf(hostItem.getTotalLike()));
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }

    @OnClick(R.id.commentBtn)
    public void comment() {
        Intent intent = new Intent(context, WriteCommentActivity.class);
        intent.putExtra(WriteCommentActivity.EXTRA_POST_ID, hostItem.getId());
        intent.putExtra(WriteCommentActivity.EXTRA_TYPE, "1");
        context.startActivity(intent);
    }

    @OnClick(R.id.collectBtn)
    public void collect() {
        NetManager.getInstance()
            .collect("1", hostItem.getId())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    CommonUtils.toast(resp.getMessage());
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }

    @OnClick(R.id.nextPageBtn)
    public void detail() {
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.EXTRA_ITEM, hostItem);
        intent.putExtra(ItemDetailActivity.EXTRA_TYPE, "1");
        context.startActivity(intent);
    }
}

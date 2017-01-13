package com.derek.doraemon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.activity.CompleteInfoActivity;
import com.derek.doraemon.activity.FavListActivity;
import com.derek.doraemon.activity.FeedbackActivity;
import com.derek.doraemon.activity.LoginActivity;
import com.derek.doraemon.activity.MessageActivity;
import com.derek.doraemon.activity.MyItemListActivity;
import com.derek.doraemon.constants.Constants;
import com.derek.doraemon.constants.SharePrefsConstants;
import com.derek.doraemon.model.UserDetail;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.SharePreferenceHelper;
import com.derek.doraemon.view.CircleImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 16/8/28.
 */
public class MeFragment extends HomeTabFragment {
    @BindView(R.id.userImageView)
    ImageView userImageView;
    @BindView(R.id.userNameText)
    TextView userNameText;
    @BindView(R.id.constellationText)
    TextView constellationText;
    @BindView(R.id.occupationText)
    TextView occupationText;

    private UserDetail userDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {
        update();
    }

    private void update() {
        NetManager.getInstance().getUserDetail(NetManager.getInstance().getUid())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    Gson gson = new Gson();
                    userDetail = gson.fromJson(gson.toJsonTree(resp.getData()), UserDetail.class);
                    Picasso.with(getActivity())
                        .load(NetManager.getInstance().getHost() + userDetail.getAvatarUrl())
                        .placeholder(R.drawable.app_logo)
                        .into(userImageView);
                    userNameText.setText(userDetail.getUserName());
                    occupationText.setText(userDetail.getProfession());
                    constellationText.setText(userDetail.getConstellation());
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
    }

    @OnClick(R.id.userImageView)
    public void editProfile() {
        Intent intent = new Intent(getActivity(), CompleteInfoActivity.class);
        intent.putExtra(CompleteInfoActivity.EXTRA_USER_DETAIL, userDetail);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.myMessage)
    public void myMessage() {
        getActivity().startActivity(new Intent(getActivity(), MessageActivity.class));
    }

    @OnClick(R.id.myPost)
    public void myPost() {
        Intent intent = new Intent(getActivity(), MyItemListActivity.class);
        intent.putExtra(MyItemListActivity.EXTRA_TYPE, 0);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.myWelfare)
    public void myWelfare() {
        Intent intent = new Intent(getActivity(), MyItemListActivity.class);
        intent.putExtra(MyItemListActivity.EXTRA_TYPE, 1);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.myMoment)
    public void myMoment() {
        Intent intent = new Intent(getActivity(), MyItemListActivity.class);
        intent.putExtra(MyItemListActivity.EXTRA_TYPE, 2);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.myFavHost)
    public void myFavHost() {
        Intent intent = new Intent(getActivity(), FavListActivity.class);
        intent.putExtra(FavListActivity.EXTRA_TYPE, 1);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.myFavWelfare)
    public void myFavWelfare() {
        Intent intent = new Intent(getActivity(), FavListActivity.class);
        intent.putExtra(FavListActivity.EXTRA_TYPE, 3);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.feedback)
    public void feedback() {
        getActivity().startActivity(new Intent(getActivity(), FeedbackActivity.class));
    }

    @OnClick(R.id.logout)
    public void logout() {
        SharePreferenceHelper.getInstance().put(SharePrefsConstants.IS_LOGIN, false);
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @OnClick(R.id.share)
    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "你的宠物养成社交助手与懂的人结伴走过与爱宠共处的点滴幸福...");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "分享到"));
    }
}

package com.derek.doraemon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.activity.MessageActivity;
import com.derek.doraemon.activity.MyItemListActivity;
import com.derek.doraemon.activity.SignUpActivity;
import com.derek.doraemon.model.UserDetail;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
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
    CircleImageView userImageView;
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
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }

    private void update() {
        NetManager.getInstance().getUserDetail(NetManager.getInstance().getUid())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    Gson gson = new Gson();
                    userDetail = gson.fromJson(gson.toJsonTree(resp.getData()), UserDetail.class);
                    Picasso.with(getActivity()).load(NetManager.getInstance().getHost() + userDetail.getAvatarUrl()).into(userImageView);
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

    @OnClick(R.id.editBtn)
    public void editProfile() {
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        intent.putExtra(SignUpActivity.EXTRA_USER_DETAIL, userDetail);
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

    @OnClick(R.id.myFav)
    public void myFav() {

    }
}

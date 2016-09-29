package com.derek.doraemon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.derek.doraemon.R;
import com.derek.doraemon.model.HostItem;
import com.derek.doraemon.model.User;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derek on 16/7/18.
 */
public class HomeFragment extends HomeTabFragment {

    @BindView(R.id.starUserRecyclerView) RecyclerView starUserRecyclerView;
    @BindView(R.id.hostRecyclerView) RecyclerView hostRecyclerView;
    @BindView(R.id.searchFab) FloatingActionButton searchFab;
    @BindView(R.id.cameraFab) FloatingActionButton cameraFab;

    private List<HostItem> hostItems;
    private RequestCallback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initData();
        //refresh();
        return view;
    }

    private void initData() {
        hostItems = new ArrayList<>();
        callback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                Gson gson = new Gson();
                hostItems =
                    gson.fromJson(gson.toJsonTree(resp.getData()), new TypeToken<List<HostItem>>() {
                    }.getType());
            }

            @Override
            public boolean fail(Resp resp) {
                return false;
            }
        });
    }

    private void refresh() {
        NetManager.getInstance().getHostList().enqueue(callback);
    }

    @OnClick(R.id.searchFab)
    public void search() {
        refresh();
    }

    @OnClick(R.id.cameraFab)
    public void takeCamera() {

    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }
}

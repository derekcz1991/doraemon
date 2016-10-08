package com.derek.doraemon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.HostListAdapter;
import com.derek.doraemon.adapter.StarUserAdapter;
import com.derek.doraemon.model.HostItem;
import com.derek.doraemon.model.StarUser;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 16/7/18.
 */
public class HomeFragment extends HomeTabFragment {

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.starUserRecyclerView) RecyclerView starUserRecyclerView;
    @BindView(R.id.hostRecyclerView) RecyclerView hostRecyclerView;
    @BindView(R.id.searchFab) FloatingActionButton searchFab;
    @BindView(R.id.cameraFab) FloatingActionButton cameraFab;

    private Gson gson;

    private List<HostItem> hostItems;
    private HostListAdapter hostListAdapter;
    private RequestCallback getHostCallback;

    private List<StarUser> starUsers;
    private StarUserAdapter starUserAdapter;
    private RequestCallback getStarUserCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initData();
        refresh();
        return view;
    }

    private void initData() {
        gson = new Gson();

        hostItems = new ArrayList<>();
        hostListAdapter = new HostListAdapter(hostItems);
        hostRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hostRecyclerView.setNestedScrollingEnabled(false);
        hostRecyclerView.setAdapter(hostListAdapter);

        getHostCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                swipeRefreshLayout.setRefreshing(false);
                hostItems.clear();
                hostItems.addAll(
                    (List<? extends HostItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                        new TypeToken<List<HostItem>>() {
                        }.getType()));
                hostListAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean fail(Resp resp) {
                swipeRefreshLayout.setRefreshing(false);
                return false;
            }

        });

        starUsers = new ArrayList<>();
        starUserAdapter = new StarUserAdapter(starUsers);
        starUserRecyclerView.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        starUserRecyclerView.setNestedScrollingEnabled(false);
        starUserRecyclerView.setAdapter(starUserAdapter);

        getStarUserCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                starUsers.clear();
                starUsers.addAll(
                    (Collection<? extends StarUser>) gson.fromJson(gson.toJsonTree(resp.getData()),
                        new TypeToken<List<StarUser>>() {
                        }.getType()));
                starUserAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean fail(Resp resp) {
                return false;
            }

        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        NetManager.getInstance().getHostList().enqueue(getHostCallback);
        NetManager.getInstance().getStarUser().enqueue(getStarUserCallback);
    }

    @OnClick(R.id.searchFab)
    public void search() {

    }

    @OnClick(R.id.cameraFab)
    public void takeCamera() {

    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }
}

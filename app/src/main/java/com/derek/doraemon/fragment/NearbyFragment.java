package com.derek.doraemon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.NearbyListAdapter;
import com.derek.doraemon.model.NearbyItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by derek on 09/10/2016.
 */
public class NearbyFragment extends HomeTabFragment {
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Gson gson;

    private List<NearbyItem> nearbyItems;
    private NearbyListAdapter nearbyListAdapter;
    private RequestCallback getNearbyCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        ButterKnife.bind(this, view);
        initData();
        refresh();
        return view;
    }

    private void initData() {
        gson = new Gson();

        nearbyItems = new ArrayList<>();
        nearbyListAdapter = new NearbyListAdapter(nearbyItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(nearbyListAdapter);

        getNearbyCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                refreshLayout.setRefreshing(false);
                nearbyItems.clear();
                nearbyItems.addAll(
                    (List<? extends NearbyItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                        new TypeToken<List<NearbyItem>>() {
                        }.getType()));
                nearbyListAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean fail(Resp resp) {
                refreshLayout.setRefreshing(false);
                return false;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        NetManager.getInstance().getNearbyList().enqueue(getNearbyCallback);
    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }
}

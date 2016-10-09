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
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.StarUserAdapter;
import com.derek.doraemon.adapter.WelfareListAdapter;
import com.derek.doraemon.model.Location;
import com.derek.doraemon.model.StarUser;
import com.derek.doraemon.model.WelfareItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 16/8/18.
 */
public class WelfareFragment extends HomeTabFragment {
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.starUserRecyclerView)
    RecyclerView starUserRecyclerView;
    @BindView(R.id.hostRecyclerView)
    RecyclerView welfareRecyclerView;
    @BindView(R.id.searchFab)
    FloatingActionButton searchFab;
    @BindView(R.id.cameraFab)
    FloatingActionButton cameraFab;
    @BindView(R.id.locationText)
    TextView locationText;

    private Gson gson;

    private List<WelfareItem> welfareItems;
    private WelfareListAdapter welfareListAdapter;
    private RequestCallback getWelfareCallback;

    private List<StarUser> starUsers;
    private StarUserAdapter starUserAdapter;
    private RequestCallback getStarUserCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        updateLocation();
        initData();
        refresh();
        return view;
    }

    private void updateLocation() {
        double[] latlong = CommonUtils.getLocation();
        if (latlong[0] != -1) {
            NetManager.getInstance().locate(latlong).enqueue(
                new RequestCallback(new RequestCallback.Callback() {
                    @Override
                    public void success(Resp resp) {
                        Gson gson = new Gson();
                        Location location = gson.fromJson(gson.toJsonTree(resp.getData()), Location.class);
                        locationText.setText(location.getDistrict());
                    }

                    @Override
                    public boolean fail(Resp resp) {
                        return false;
                    }
                }));
        }
    }

    private void initData() {
        gson = new Gson();

        welfareItems = new ArrayList<>();
        welfareListAdapter = new WelfareListAdapter(welfareItems);
        welfareRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        welfareRecyclerView.setNestedScrollingEnabled(false);
        welfareRecyclerView.setAdapter(welfareListAdapter);

        getWelfareCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                swipeRefreshLayout.setRefreshing(false);
                welfareItems.clear();
                welfareItems.addAll(
                    (List<? extends WelfareItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                        new TypeToken<List<WelfareItem>>() {
                        }.getType()));

                welfareListAdapter.notifyDataSetChanged();
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
        NetManager.getInstance().getWelfareList().enqueue(getWelfareCallback);
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

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
import com.derek.doraemon.adapter.MomentListAdapter;
import com.derek.doraemon.model.MomentItem;
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
 * Created by derek on 08/10/2016.
 */
public class MomentFragment extends HomeTabFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private Gson gson;

    private List<MomentItem> momentItems;
    private MomentListAdapter momentListAdapter;
    private RequestCallback getMomentCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moment, container, false);
        ButterKnife.bind(this, view);
        initData();
        refresh();
        return view;
    }

    private void initData() {
        gson = new Gson();

        momentItems = new ArrayList<>();
        momentListAdapter = new MomentListAdapter(momentItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(momentListAdapter);

        getMomentCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                swipeRefreshLayout.setRefreshing(false);
                momentItems.clear();
                momentItems.addAll(
                    (List<? extends MomentItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                        new TypeToken<List<MomentItem>>() {
                        }.getType()));

                momentListAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean fail(Resp resp) {
                swipeRefreshLayout.setRefreshing(false);
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
        NetManager.getInstance().getMomentList().enqueue(getMomentCallback);
    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }
}

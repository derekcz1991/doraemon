package com.derek.doraemon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.derek.doraemon.R;
import com.derek.doraemon.activity.PublishActivity;
import com.derek.doraemon.adapter.MomentListAdapter;
import com.derek.doraemon.model.MomentItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 08/10/2016.
 */
public class MomentFragment extends HomeTabFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.stBtn)
    CheckedTextView stBtn;
    @BindView(R.id.hlBtn)
    CheckedTextView hlBtn;
    @BindView(R.id.hdBtn)
    CheckedTextView hdBtn;
    @BindView(R.id.mzmBtn)
    CheckedTextView mzmBtn;

    private Gson gson;

    private List<MomentItem> momentItems;
    private MomentListAdapter momentListAdapter;
    private RequestCallback getMomentCallback;

    private int kind = 1;


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
                if (resp.getData() == null) {
                    CommonUtils.toast(resp.getMessage());
                } else {
                    momentItems.addAll(
                        (List<? extends MomentItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                            new TypeToken<List<MomentItem>>() {
                            }.getType()));
                }
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
        swipeRefreshLayout.setRefreshing(true);
        NetManager.getInstance().getMomentList(kind).enqueue(getMomentCallback);
    }

    @OnClick(R.id.cameraFab)
    public void takeCamera() {
        Intent intent = new Intent(getActivity(), PublishActivity.class);
        intent.putExtra(PublishActivity.EXTRA_TYPE, 2);
        getActivity().startActivity(intent);
    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }

    @OnClick(R.id.stBtn)
    public void stBtn() {
        if(!stBtn.isChecked()) {
            kind = 1;
            stBtn.setChecked(true);
            hlBtn.setChecked(false);
            hdBtn.setChecked(false);
            mzmBtn.setChecked(false);
            refresh();
        }
    }

    @OnClick(R.id.hlBtn)
    public void hlBtn() {
        if(!hlBtn.isChecked()) {
            kind = 2;
            stBtn.setChecked(false);
            hlBtn.setChecked(true);
            hdBtn.setChecked(false);
            mzmBtn.setChecked(false);
            refresh();
        }
    }

    @OnClick(R.id.hdBtn)
    public void hdBtn() {
        if(!hdBtn.isChecked()) {
            kind = 3;
            stBtn.setChecked(false);
            hlBtn.setChecked(false);
            hdBtn.setChecked(true);
            mzmBtn.setChecked(false);
            refresh();
        }
    }

    @OnClick(R.id.mzmBtn)
    public void mzmBtn() {
        if(!mzmBtn.isChecked()) {
            kind = 4;
            stBtn.setChecked(false);
            hlBtn.setChecked(false);
            hdBtn.setChecked(false);
            mzmBtn.setChecked(true);
            refresh();
        }
    }

}

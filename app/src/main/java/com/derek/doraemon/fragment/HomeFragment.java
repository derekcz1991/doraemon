package com.derek.doraemon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.activity.HomeActivity;
import com.derek.doraemon.activity.PublishActivity;
import com.derek.doraemon.activity.SearchHostActivity;
import com.derek.doraemon.activity.SelectLocationActivity;
import com.derek.doraemon.adapter.HostListAdapter;
import com.derek.doraemon.model.HostItem;
import com.derek.doraemon.model.Location;
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
 * Created by derek on 16/7/18.
 */
public class HomeFragment extends HomeTabFragment {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.hostRecyclerView)
    RecyclerView hostRecyclerView;
    @BindView(R.id.searchFab)
    FloatingActionButton searchFab;
    @BindView(R.id.cameraFab)
    FloatingActionButton cameraFab;
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.tab3)
    CheckedTextView tab3;
    private Gson gson;

    private List<HostItem> hostItems;
    private HostListAdapter hostListAdapter;
    private RequestCallback getHostCallback;
    private int sort = 1;
    private String city = "";

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
                if (resp.getData() == null) {
                    CommonUtils.toast(resp.getMessage());
                } else {
                    hostItems.addAll(
                        (List<? extends HostItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                            new TypeToken<List<HostItem>>() {
                            }.getType()));
                }

                hostListAdapter.notifyDataSetChanged();
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
        NetManager.getInstance().getHostList(sort, city).enqueue(getHostCallback);
    }

    @OnClick(R.id.searchFab)
    public void search() {
        startActivity(new Intent(getActivity(), SearchHostActivity.class));
    }

    @OnClick(R.id.cameraFab)
    public void takeCamera() {
        Intent intent = new Intent(getActivity(), PublishActivity.class);
        intent.putExtra(PublishActivity.EXTRA_TYPE, 0);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.wallPaper)
    public void onWallPaper() {
        getActivity().startActivityForResult(new Intent(getActivity(), SelectLocationActivity.class), 1);
    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }

    @OnClick(R.id.tab1)
    public void onTab1Checked() {
        if (!tab1.isChecked()) {
            tab1.setChecked(true);
            tab2.setChecked(false);
            tab3.setChecked(false);
            sort = 1;
            refresh();
        }
    }

    @OnClick(R.id.tab2)
    public void onTab2Checked() {
        if (!tab2.isChecked()) {
            tab1.setChecked(false);
            tab2.setChecked(true);
            tab3.setChecked(false);
            sort = 2;
            refresh();
        }
    }

    @OnClick(R.id.tab3)
    public void onTab3Checked() {
        if (!tab3.isChecked()) {
            tab1.setChecked(false);
            tab2.setChecked(false);
            tab3.setChecked(true);
            sort = 3;
            refresh();
        }
    }

    @OnClick(R.id.pinBtn)
    public void pinClick() {
        ((HomeActivity) getActivity()).setTabSelected(R.id.tab_nearby);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            String city = data.getStringExtra("city");
            if (this.city.equals(city)) {
                return;
            }
            locationText.setText(city);
            this.city = city;
            refresh();
        }
    }

}

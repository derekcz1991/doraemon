package com.derek.doraemon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.activity.PublishActivity;
import com.derek.doraemon.activity.SearchWelfareActivity;
import com.derek.doraemon.activity.SelectLocationActivity;
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
    @BindView(R.id.hostRecyclerView)
    RecyclerView welfareRecyclerView;
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

    private Gson gson;

    private List<WelfareItem> welfareItems;
    private List<WelfareItem> disPlayItems;
    private WelfareListAdapter welfareListAdapter;
    private RequestCallback getWelfareCallback;

    private int kind = 0;
    private int sort = 1;
    private String city = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welfare, container, false);
        ButterKnife.bind(this, view);

        registerForContextMenu(view);
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
        disPlayItems = new ArrayList<>();
        welfareListAdapter = new WelfareListAdapter(disPlayItems);
        welfareRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        welfareRecyclerView.setNestedScrollingEnabled(false);
        welfareRecyclerView.setAdapter(welfareListAdapter);

        getWelfareCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                swipeRefreshLayout.setRefreshing(false);
                welfareItems.clear();
                if (resp.getData() == null) {
                    CommonUtils.toast(resp.getMessage());
                } else {
                    welfareItems.addAll(
                        (List<? extends WelfareItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                            new TypeToken<List<WelfareItem>>() {
                            }.getType()));
                }
                updateDisplayList();
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
        NetManager.getInstance().getWelfareList(sort, city).enqueue(getWelfareCallback);
    }

    private void updateDisplayList() {
        switch (kind) {
            case 0:
                disPlayItems.clear();
                disPlayItems.addAll(welfareItems);
                break;
            default:
                disPlayItems.clear();
                for (int i = 0; i < welfareItems.size(); i++) {
                    if (welfareItems.get(i).getKind() == kind) {
                        disPlayItems.add(welfareItems.get(i));
                    }
                }
                break;
        }
        welfareListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(1, 0, 1, "全部");
        menu.add(1, 1, 2, "随手拍流浪狗");
        menu.add(1, 2, 3, "寻宠");
        menu.add(1, 3, 4, "领养");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (kind == item.getItemId()) {
            return false;
        }
        kind = item.getItemId();
        updateDisplayList();
        return super.onContextItemSelected(item);
    }

    @OnClick(R.id.searchFab)
    public void search() {
        startActivity(new Intent(getActivity(), SearchWelfareActivity.class));
    }

    @OnClick(R.id.typeFab)
    public void onTypeClick() {
        getActivity().openContextMenu(getView());
    }

    @OnClick(R.id.cameraFab)
    public void takeCamera() {
        Intent intent = new Intent(getActivity(), PublishActivity.class);
        intent.putExtra(PublishActivity.EXTRA_TYPE, 1);
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
            sort = 1;
            refresh();
        }
    }

    @OnClick(R.id.tab2)
    public void onTab2Checked() {
        if (!tab2.isChecked()) {
            tab1.setChecked(false);
            tab2.setChecked(true);
            sort = 2;
            refresh();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
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

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
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.derek.doraemon.R;
import com.derek.doraemon.activity.ProfileActivity;
import com.derek.doraemon.adapter.NearbyListAdapter;
import com.derek.doraemon.model.NearbyItem;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.view.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 09/10/2016.
 */
public class NearbyFragment extends HomeTabFragment {
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.switchBtn)
    ImageView switchBtn;

    private Gson gson;

    private List<NearbyItem> nearbyItems;
    private NearbyListAdapter nearbyListAdapter;
    private RequestCallback getNearbyCallback;

    // 定位相关
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    private MyLocationListener myListener = new MyLocationListener();
    private boolean isFirstLoc = true; // 是否首次定位

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        ButterKnife.bind(this, view);

        initMap();
        initData();
        refresh();
        return view;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private void initMap() {
        mBaiduMap = mMapView.getMap();
        /*mBaiduMap
            .setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));*/
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity().getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.view_info_window, null);
                final int position = marker.getExtraInfo().getInt("position");
                if (position < nearbyItems.size()) {
                    ((TextView) view.findViewById(R.id.nameText)).setText(nearbyItems.get(position).getUserName());
                    ((TextView) view.findViewById(R.id.addressText)).setText(nearbyItems.get(position).getAddress());
                }
                InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), marker.getPosition(), 135,
                    new InfoWindow.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick() {
                            Intent intent = new Intent(getContext(), ProfileActivity.class);
                            intent.putExtra(ProfileActivity.EXTRA_UID, nearbyItems.get(position).getUid());
                            getContext().startActivity(intent);
                        }
                    });
                mBaiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
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
                setUpMarker();
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

    private void setUpMarker() {
        for (int i = 0; i < nearbyItems.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_marker, null);
            CircleImageView avatar = (CircleImageView) view.findViewById(R.id.avatar);
            avatar.setBorderWidth(5);
            avatar.setBorderColor(getContext().getResources().getColor(R.color.colorPrimary));
            Picasso.with(getContext())
                .load(NetManager.getInstance().getHost() + nearbyItems.get(i).getAvatarUrl())
                .into(avatar);

            Marker marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions()
                .position(new LatLng(nearbyItems.get(i).getLatitude(), nearbyItems.get(i).getLongitude()))
                .icon(BitmapDescriptorFactory.fromView(view))
                .zIndex(9));
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            marker.setExtraInfo(bundle);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @OnClick(R.id.switchBtn)
    public void onSwitchClick() {
        if (mMapView.getVisibility() == View.VISIBLE) {
            mMapView.setVisibility(View.INVISIBLE);
            switchBtn.setImageResource(R.drawable.btn_map);
        } else {
            mMapView.setVisibility(View.VISIBLE);
            switchBtn.setImageResource(R.drawable.btn_list);
        }
    }

    private void refresh() {
        NetManager.getInstance().getNearbyList().enqueue(getNearbyCallback);
    }

    @Override
    public void onPageInto(BaseFragment fromFragment) {

    }
}

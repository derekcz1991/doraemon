package com.derek.doraemon.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.MyItemListAdapter;
import com.derek.doraemon.model.BaseItem;
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

/**
 * Created by derek on 08/10/2016.
 */
public class MyItemListActivity extends BaseTitleActivity {
    public static final String EXTRA_TYPE = "type";

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<BaseItem> items;
    private MyItemListAdapter myItemListAdapter;
    private RequestCallback getItemListCallback;
    private Gson gson;

    private int type;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        type = getIntent().getIntExtra(EXTRA_TYPE, 0);
        switch (type) {
            case 0:
                setTitleText("我的寄养");
                break;
            case 1:
                setTitleText("我的公益");
                break;
            case 2:
                setTitleText("我的圈子");
                break;
        }
        View view = View.inflate(this, R.layout.activity_message, null);
        ButterKnife.bind(this, view);

        refreshLayout.setRefreshing(true);
        initData();
        refresh();
        return view;
    }

    private void initData() {
        items = new ArrayList<>();
        myItemListAdapter = new MyItemListAdapter(items, type);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myItemListAdapter);

        gson = new Gson();
        getItemListCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                if (resp.getData() == null) {
                    CommonUtils.toast(resp.getMessage());
                } else {
                    items.clear();
                    items.addAll(
                        (Collection<? extends BaseItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                            new TypeToken<List<BaseItem>>() {
                            }.getType()));
                    myItemListAdapter.notifyDataSetChanged();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public boolean fail(Resp resp) {
                refreshLayout.setRefreshing(false);
                return false;
            }
        });

        refreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    private void refresh() {
        switch (type) {
            case 0:
                NetManager.getInstance().getMyPostList().enqueue(getItemListCallback);
                break;
            case 1:
                NetManager.getInstance().getMyWelfareList().enqueue(getItemListCallback);
                break;
            case 2:
                NetManager.getInstance().getMyMomentList().enqueue(getItemListCallback);
                break;
        }
    }


}

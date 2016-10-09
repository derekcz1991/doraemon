package com.derek.doraemon.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.FavListAdapter;
import com.derek.doraemon.model.FavItem;
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

/**
 * Created by derek on 09/10/2016.
 */
public class FavListActivity extends BaseTitleActivity {
    public static final String EXTRA_TYPE = "type";

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<FavItem> favItems;
    private FavListAdapter myFavListAdapter;
    private RequestCallback getFavListCallback;
    private Gson gson;

    private int type;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        type = getIntent().getIntExtra(EXTRA_TYPE, 1);
        switch (type) {
            case 1:
                setTitleText("收藏的窝");
                break;
            case 2:
                setTitleText("收藏的寄养");
                break;
        }
        View view = View.inflate(this, R.layout.activity_fav, null);
        ButterKnife.bind(this, view);

        refreshLayout.setRefreshing(true);
        initData();
        refresh();
        return view;
    }

    private void initData() {
        favItems = new ArrayList<>();
        myFavListAdapter = new FavListAdapter(favItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myFavListAdapter);

        gson = new Gson();
        getFavListCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                favItems.clear();
                favItems.addAll(
                    (Collection<? extends FavItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                        new TypeToken<List<FavItem>>() {
                        }.getType()));
                myFavListAdapter.notifyDataSetChanged();
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
        NetManager.getInstance().getMyFavList(type).enqueue(getFavListCallback);
    }
}

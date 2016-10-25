package com.derek.doraemon.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.HostListAdapter;
import com.derek.doraemon.model.HostItem;
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
import butterknife.OnEditorAction;

/**
 * Created by derek on 16/10/26.
 */
public class SearchHostActivity extends BaseTitleActivity {
    @BindView(R.id.recyclerView)
    RecyclerView hostRecyclerView;
    @BindView(R.id.searchText)
    EditText searchText;

    private Gson gson;
    private List<HostItem> hostItems;
    private HostListAdapter hostListAdapter;
    private RequestCallback getHostCallback;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        setTitleText("搜索寄养");
        View view = View.inflate(this, R.layout.activity_search, null);
        ButterKnife.bind(this, view);

        init();
        return view;
    }

    private void init() {
        gson = new Gson();

        hostItems = new ArrayList<>();
        hostListAdapter = new HostListAdapter(hostItems);
        hostRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hostRecyclerView.setAdapter(hostListAdapter);

        getHostCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                if (resp.getData() == null) {
                    CommonUtils.toast(resp.getMessage());
                    return;
                }
                hostItems.clear();
                hostItems.addAll(
                    (List<? extends HostItem>) gson.fromJson(gson.toJsonTree(resp.getData()),
                        new TypeToken<List<HostItem>>() {
                        }.getType()));
                hostListAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean fail(Resp resp) {

                return false;
            }

        });
    }

    @OnEditorAction(R.id.searchText)
    public boolean search(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (searchText.getText().toString().isEmpty()) {
                return false;
            }
            NetManager.getInstance().findHostPost(searchText.getText().toString()).enqueue(getHostCallback);
        }
        return false;
    }
}

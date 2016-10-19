package com.derek.doraemon.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.MessageListAdapter;
import com.derek.doraemon.model.Message;
import com.derek.doraemon.model.Audio;
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
 * Created by derek on 08/10/2016.
 */
public class MessageActivity extends BaseTitleActivity {
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Message> messages;
    private List<Audio> audioList;
    private MessageListAdapter messageListAdapter;
    private RequestCallback getMsgListCallback;
    private RequestCallback getAudioListCallback;
    private Gson gson;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        setTitleText("我的消息");
        View view = View.inflate(this, R.layout.activity_message, null);
        ButterKnife.bind(this, view);

        refreshLayout.setRefreshing(true);
        initData();
        refresh();
        return view;
    }

    private void initData() {
        gson = new Gson();

        messages = new ArrayList<>();
        audioList = new ArrayList<>();
        messageListAdapter = new MessageListAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageListAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        getMsgListCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                messages.clear();
                messages.addAll(
                    (Collection<? extends Message>) gson.fromJson(gson.toJsonTree(resp.getData()),
                        new TypeToken<List<Message>>() {
                        }.getType()));
                messageListAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public boolean fail(Resp resp) {
                refreshLayout.setRefreshing(false);
                return false;
            }
        });

        getAudioListCallback = new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                audioList.clear();
                audioList.addAll(
                    (Collection<? extends Audio>) gson.fromJson(gson.toJsonTree(resp.getData()),
                        new TypeToken<List<Audio>>() {
                        }.getType()));
                messageListAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public boolean fail(Resp resp) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        NetManager.getInstance().getLetterList().enqueue(getMsgListCallback);
    }
}

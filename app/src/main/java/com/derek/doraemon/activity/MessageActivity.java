package com.derek.doraemon.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.AudioListAdapter;
import com.derek.doraemon.adapter.MessageListAdapter;
import com.derek.doraemon.model.Message;
import com.derek.doraemon.model.Audio;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.view.ListenAudioView;
import com.derek.doraemon.view.viewholder.AudioViewHolder;
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
    @BindView(R.id.msgRefreshLayout)
    SwipeRefreshLayout msgRefreshLayout;
    @BindView(R.id.audioRefreshLayout)
    SwipeRefreshLayout audioRefreshLayout;
    @BindView(R.id.msgRecyclerView)
    RecyclerView msgRecyclerView;
    @BindView(R.id.audioRecyclerView)
    RecyclerView audioRecyclerView;
    @BindView(R.id.listenAudioView)
    ListenAudioView listenAudioView;

    private List<Message> messages;
    private List<Audio> audioList;
    private MessageListAdapter messageListAdapter;
    private AudioListAdapter audioListAdapter;
    private RequestCallback getMsgListCallback;
    private RequestCallback getAudioListCallback;
    private Gson gson;

    private int type = 0;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        setTitleText("我的消息");
        View view = View.inflate(this, R.layout.activity_message, null);
        ButterKnife.bind(this, view);

        msgRefreshLayout.setRefreshing(true);
        initData();

        NetManager.getInstance().getLetterList().enqueue(getMsgListCallback);
        NetManager.getInstance().getAudioList().enqueue(getAudioListCallback);

        return view;
    }

    private void initData() {
        gson = new Gson();

        messages = new ArrayList<>();
        audioList = new ArrayList<>();
        messageListAdapter = new MessageListAdapter(messages);
        audioListAdapter = new AudioListAdapter(audioList, new AudioViewHolder.Callback() {
            @Override
            public void onItemClicked(Audio audio) {
                listenAudioView.show(audio);
            }
        });
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgRecyclerView.setAdapter(messageListAdapter);

        audioRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        audioRecyclerView.setAdapter(audioListAdapter);

        msgRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetManager.getInstance().getLetterList().enqueue(getMsgListCallback);
            }
        });
        audioRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetManager.getInstance().getAudioList().enqueue(getAudioListCallback);
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
                msgRefreshLayout.setRefreshing(false);
            }

            @Override
            public boolean fail(Resp resp) {
                msgRefreshLayout.setRefreshing(false);
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
                audioListAdapter.notifyDataSetChanged();
                audioRefreshLayout.setRefreshing(false);
            }

            @Override
            public boolean fail(Resp resp) {
                audioRefreshLayout.setRefreshing(false);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(type == 0) {
            getMenuInflater().inflate(R.menu.menu_audio, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_message, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (type == 0) {
            type = 1;
            msgRefreshLayout.setVisibility(View.INVISIBLE);
            audioRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            type = 0;
            msgRefreshLayout.setVisibility(View.VISIBLE);
            audioRefreshLayout.setVisibility(View.INVISIBLE);
        }
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        NetManager.getInstance().getAudioList().enqueue(getAudioListCallback);
    }
}

package com.derek.doraemon.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.derek.doraemon.R;
import com.derek.doraemon.adapter.ChatListAdapter;
import com.derek.doraemon.model.Chat;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;

/**
 * Created by derek on 10/6/16.
 */
public class ChatActivity extends BaseTitleActivity {
    public final static String EXTRA_CHATTERID = "chatterId";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.msgText)
    EditText msgText;

    private ChatListAdapter chatListAdapter;
    private Chat chat;
    private long chatterId;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        chatterId = getIntent().getLongExtra(EXTRA_CHATTERID, 0);

        View view = View.inflate(this, R.layout.activity_chat, null);
        getToolbar().setNavigationIcon(R.drawable.icon_nav_blue);
        setTitleText("我的私信");
        ButterKnife.bind(this, view);

        refreshLayout.setRefreshing(true);
        initData();
        getChat();
        return view;
    }

    private void initData() {
        chat = new Chat();
        chatListAdapter = new ChatListAdapter(chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(chatListAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChat();
            }
        });
    }

    private void getChat() {
        NetManager.getInstance().getChatList(chatterId).enqueue(new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                Gson gson = new Gson();
                chat = gson.fromJson(gson.toJsonTree(resp.getData()), Chat.class);
                chatListAdapter.setChat(chat);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public boolean fail(Resp resp) {
                refreshLayout.setRefreshing(false);
                return false;
            }
        }));
    }

    @OnEditorAction(R.id.msgText)
    public boolean sendMsg(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            if (msgText.getText().toString().isEmpty()) {
                return true;
            }
            NetManager.getInstance().sendChat(chatterId, msgText.getText().toString()).enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    CommonUtils.toast(resp.getMessage());
                    msgText.setText("");
                    getChat();
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));
        }
        return false;
    }
}

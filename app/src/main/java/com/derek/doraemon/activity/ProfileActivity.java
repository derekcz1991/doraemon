package com.derek.doraemon.activity;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.czt.mp3recorder.MP3Recorder;
import com.derek.doraemon.R;
import com.derek.doraemon.constants.Constants;
import com.derek.doraemon.model.UserDetail;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;
import com.derek.doraemon.view.CircleImageView;
import com.derek.doraemon.view.RecorderView;
import com.derek.doraemon.view.StarMarkView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by derek on 16/8/12.
 */
public class ProfileActivity extends BaseTitleActivity {
    public static final String EXTRA_UID = "uid";

    @BindView(R.id.userImageView)
    CircleImageView userImageView;
    @BindView(R.id.hostNameText)
    TextView hostNameText;
    @BindView(R.id.starMarkView)
    StarMarkView starMarkView;
    @BindView(R.id.starText)
    TextView starText;
    @BindView(R.id.petNameText)
    TextView petNameText;
    @BindView(R.id.petAgeText)
    TextView petAgeText;
    @BindView(R.id.petTypeText)
    TextView petTypeText;
    @BindView(R.id.petCategoryText)
    TextView petCategoryText;
    @BindView(R.id.jobText)
    TextView jobText;
    @BindView(R.id.constellationText)
    TextView constellationText;
    @BindView(R.id.emailText)
    TextView emailText;
    @BindView(R.id.introText)
    TextView introText;
    @BindView(R.id.helloBtn)
    ImageView helloBtn;
    @BindView(R.id.recordView)
    RecorderView recorderView;

    private long uid;
    private UserDetail userDetail;

    private MP3Recorder mRecorder;
    private boolean isRecorderStart;
    private long startTime;

    private int count;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Runnable timerRunnable;

    @Override
    protected boolean showNavIcon() {
        return false;
    }

    @Override
    protected View onCreateContentView() {
        uid = getIntent().getLongExtra(EXTRA_UID, 0);
        View view = View.inflate(this, R.layout.activity_profile, null);
        getToolbar().setNavigationIcon(R.drawable.icon_nav_blue);
        ButterKnife.bind(this, view);

        initRecord();
        update();
        return view;
    }

    @OnClick(R.id.msgBtn)
    public void sendMsg() {
        if (userDetail == null) {
            return;
        }
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHATTERID, userDetail.getId());
        startActivity(intent);
    }

    private void initRecord() {
        recorderView.setVisibility(View.INVISIBLE);
        helloBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("initRecord", "event = " + event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        isRecorderStart = true;
                        helloBtn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isRecorderStart) {
                                    mRecorder = new MP3Recorder(new File(Constants.PET_FOLDER, startTime + ".mp3"));
                                    try {
                                        recorderView.setVisibility(View.VISIBLE);
                                        mRecorder.start();
                                        initTimer();
                                        mTimer.schedule(mTimerTask, 0, 1000);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    CommonUtils.toast("录制时间太短");
                                }
                            }
                        }, 500);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        recorderView.setVisibility(View.INVISIBLE);
                        if (isRecorderStart) {
                            stopRecord();
                        }
                        isRecorderStart = false;
                        break;
                }
                return true;
            }
        });
    }

    private void initTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                count++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recorderView.updateTime(5 - count);
                    }
                });
                if (count == 5) {
                    stopRecord();
                }
            }
        };
    }

    private void stopRecord() {
        isRecorderStart = false;
        mRecorder.stop();
        mTimer.cancel();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recorderView.setVisibility(View.INVISIBLE);
            }
        });
        count = 0;
    }

    @OnClick(R.id.starLayout)
    public void goMarkActivity() {
        if (userDetail == null) {
            return;
        }
        Intent intent = new Intent(this, MarkActivity.class);
        intent.putExtra(MarkActivity.EXTRA_USER_DETAIL, userDetail);
        startActivity(intent);
    }

    private void update() {
        NetManager.getInstance().getUserDetail(uid).enqueue(new RequestCallback(new RequestCallback.Callback() {
            @Override
            public void success(Resp resp) {
                Gson gson = new Gson();
                userDetail = gson.fromJson(gson.toJsonTree(resp.getData()), UserDetail.class);
                Picasso.with(ProfileActivity.this).load(NetManager.getInstance().getHost() + userDetail.getAvatarUrl()).into(userImageView);
                starMarkView.setStar(userDetail.getRecommend());
                starText.setText(userDetail.getRecommend() + "个评分");
                petNameText.setText("宠物名: " + userDetail.getPetName());
                petAgeText.setText("宠物年龄: " + userDetail.getPetAge());
                petTypeText.setText("宠物种类: " + userDetail.getPetBreed());
                petCategoryText.setText("品种: " + userDetail.getPetType());
                jobText.setText("职业: " + userDetail.getProfession());
                constellationText.setText("星座: " + userDetail.getConstellation());
                emailText.setText("电子邮件: " + userDetail.getEmail());
                introText.setText("自我介绍: " + userDetail.getIntro());
            }

            @Override
            public boolean fail(Resp resp) {
                return false;
            }
        }));
    }
}

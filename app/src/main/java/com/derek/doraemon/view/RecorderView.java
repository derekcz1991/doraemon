package com.derek.doraemon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.czt.mp3recorder.MP3Recorder;
import com.derek.doraemon.R;
import com.derek.doraemon.constants.Constants;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by derek on 16/10/18.
 */
public class RecorderView extends FrameLayout {

    private TextView timeText;

    private long receiverId;
    private boolean isRecordsStart;
    private MP3Recorder mRecorder;
    private String fileName;
    private File recordsAudio;
    private int count;
    private Timer mTimer;

    public RecorderView(Context context) {
        this(context, null);
    }

    public RecorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_recorder, null);
        timeText = (TextView) view.findViewById(R.id.timeText);
        addView(view);
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
        fileName = System.currentTimeMillis() + receiverId + ".mp3";
    }

    public void start() {
        isRecordsStart = true;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRecordsStart) {
                    setVisibility(VISIBLE);
                    recordsAudio = new File(Constants.RECORDS_FOLDER, fileName);
                    mRecorder = new MP3Recorder(recordsAudio);
                    try {
                        mRecorder.start();
                        startTimer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtils.toast("录制时间太短");
                }
            }
        }, 300);
    }

    private void startTimer() {
        mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                count++;
                post(new Runnable() {
                    @Override
                    public void run() {
                        updateTime(6 - count);
                    }
                });
                if (count == 6) {
                    stop();
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public void stop() {
        isRecordsStart = false;
        post(new Runnable() {
            @Override
            public void run() {
                setVisibility(INVISIBLE);
            }
        });
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.stop();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        count = 0;
        if (recordsAudio != null && recordsAudio.exists()) {
            NetManager.getInstance().uploadAudio(recordsAudio, receiverId)
                .enqueue(new RequestCallback(new RequestCallback.Callback() {
                    @Override
                    public void success(Resp resp) {
                        timeText.setText("发送成功");
                    }

                    @Override
                    public boolean fail(Resp resp) {
                        return false;
                    }
                }));
        }
    }

    private void updateTime(int i) {
        timeText.setText("语音倒计时:" + i + "秒");
    }
}

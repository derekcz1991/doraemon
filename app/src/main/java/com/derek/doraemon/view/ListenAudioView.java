package com.derek.doraemon.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.derek.doraemon.R;
import com.derek.doraemon.constants.Constants;
import com.derek.doraemon.model.Audio;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by derek on 16/10/21.
 */
public class ListenAudioView extends FrameLayout {
    private String TAG = "ListenAudioView";
    private TextView userNameText;
    private CircleImageView userImage;
    private String audioUrl;
    private MediaPlayer mediaPlayer;

    public ListenAudioView(Context context) {
        this(context, null);
    }

    public ListenAudioView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListenAudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_play_audio, null);
        userNameText = (TextView) view.findViewById(R.id.userNameText);
        userImage = (CircleImageView) view.findViewById(R.id.userImageView);
        view.findViewById(R.id.ignoreBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(INVISIBLE);
            }
        });
        view.findViewById(R.id.listenBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(INVISIBLE);
                try {
                    Log.d(TAG, NetManager.getInstance().getHost() + audioUrl);
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(NetManager.getInstance().getHost() + audioUrl);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    CommonUtils.toast("播放失败");
                    e.printStackTrace();
                }
            }
        });
        mediaPlayer = new MediaPlayer();
        addView(view);
    }

    public void show(Audio audio) {
        userNameText.setText(audio.getUserName());
        Picasso.with(getContext())
            .load(NetManager.getInstance().getHost() + audio.getAvatarUrl())
            .into(userImage);
        this.audioUrl = audio.getAudioUrl();
        setVisibility(VISIBLE);

    }

    //public void show()
    private void downloadAudio() {
        NetManager.getInstance().getAudio(audioUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Constants.AUDIO_FOLDER + File.separator + audioUrl);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}

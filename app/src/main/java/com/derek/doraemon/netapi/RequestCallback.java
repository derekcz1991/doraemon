package com.derek.doraemon.netapi;

/**
 * Created by derek on 2016/9/28.
 */

import android.util.Log;
import android.widget.Toast;
import com.derek.doraemon.MyApplication;
import com.derek.doraemon.utils.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;

public class RequestCallback implements Callback<Resp> {
    private Callback callback;

    public interface Callback {
        void success(Resp resp);

        boolean fail(Resp resp);

    }

    public RequestCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<Resp> call, retrofit2.Response<Resp> response) {
        if (response == null) {
            Log.d("RequestCallback", call.request().url().toString() + " ==>> response is null");
            CommonUtils.toast("网络失败, 请重试");
            return;
        }
        Resp mResp = response.body();
        Log.d("RequestCallback", call.request().url().toString() + " ==>> " + mResp);
        if (mResp == null) {
            CommonUtils.toast("数据错误, 请重试");
            Log.d("RequestCallback",
                "code ==>> " + response.code() + "   response ==>> " + response.message());
            return;
        }
        if (mResp.getCode() == 200) {
            callback.success(mResp);
        } else {
            if (!callback.fail(mResp)) {
                Toast.makeText(MyApplication.getContext(), mResp.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            }
        }
    }

    @Override
    public void onFailure(Call<Resp> call, Throwable t) {
        Log.d("RequestCallback", "onFailure == >> " + call.request().url().toString(), t);
        CommonUtils.toast("网络失败, 请重试");
    }
}

package com.ycl.tabview.network;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 方法内代码能再主线程中运行的CallBack
 */

public abstract class UICallBack implements Callback{

    //拿到主线程的handler
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(final Call call, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailureUI(call, e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        //判断是否响应成功
        if (!response.isSuccessful()){
            throw new IOException("error code:" + response.code());
        }

        //拿到响应体
        final String json = response.body().string();

        handler.post(new Runnable() {
            @Override
            public void run() {
                onResponseUI(call, json);
            }
        });
    }

    public abstract void onFailureUI(Call call, IOException e);
    public abstract void onResponseUI(Call call, String body);


}

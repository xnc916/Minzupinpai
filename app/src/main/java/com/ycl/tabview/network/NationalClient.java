package com.ycl.tabview.network;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by gameben on 2017-08-14.
 */

public class NationalClient {


    private static NationalClient easyShopClient;

    private OkHttpClient okHttpClient;
    private Gson gson;

    public static NationalClient getInstance() {
        if (easyShopClient == null) {
            easyShopClient = new NationalClient();
        }
        return easyShopClient;
    }


    private NationalClient() {
        //初始化日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设置拦截级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                //添加日志拦截器
                .addInterceptor(httpLoggingInterceptor)
                .build();

        gson = new Gson();
    }




    //修改头像
    public Call uploadAvatar(String id , String s) {

        RequestBody requestboy = new FormBody.Builder()
                .add("id",id)
                .add("image",s)
                .build();

        Request request = new Request.Builder()
                .url("http://3.1budai.com/mobile/user1.php?act=Upimage")
                .post(requestboy)
                .build();

        return okHttpClient.newCall(request);
    }
}

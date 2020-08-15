package com.example.protectionapp.network;

import com.example.protectionapp.model.NotificationBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiResonse {
    @GET("send_notification.php")
    Call<NotificationBean> sendMsg(@Query("tokens") String tokens, @Query("msg") String msg);

    @GET("file_send_notification.php")
    Call<NotificationBean> fileSendMsg(@Query("tokens") String tokens, @Query("msg") String msg);
}

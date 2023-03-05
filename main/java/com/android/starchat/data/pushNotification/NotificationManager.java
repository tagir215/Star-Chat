package com.android.starchat.data.pushNotification;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationManager {
    public static void sendNotification(String title, String message, String topic){
        PushNotification pushNotification = createPushNotification(title,message,"/topics/"+topic);
        Log.e("Notification", "Payload: " + new Gson().toJson(pushNotification));
        new Thread(new Runnable() {
            @Override
            public void run() {
                RetrofitInstance.getNotificationAPI().postNotification(pushNotification).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful())
                            Log.e("Notification","Response: "+response.body());
                        else
                            Log.e("Notification","Response: not successful");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Notification","this is a failure :( idk here's some message "+t.getMessage());
                    }
                });
            }
        }).start();
    }

    private static PushNotification createPushNotification(String title, String body, String topic){
        return new PushNotification(new NotificationData(title,body),topic);
    }
}

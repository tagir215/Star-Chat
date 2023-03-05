package com.android.starchat.data.pushNotification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;

    private static Retrofit getRetrofitInstance(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(NotificationConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static NotificationAPI getNotificationAPI(){
        return getRetrofitInstance().create(NotificationAPI.class);
    }
}

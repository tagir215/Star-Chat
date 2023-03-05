package com.android.starchat.data.pushNotification;

import static com.android.starchat.data.pushNotification.NotificationConstants.CONTENT_TYPE;
import static com.android.starchat.data.pushNotification.NotificationConstants.SERVER_KEY;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPI {
    @Headers({"Authorization: key=" + SERVER_KEY, "Content-Type:" + CONTENT_TYPE})
    @POST("fcm/send")
    Call<ResponseBody> postNotification(@Body PushNotification pushNotification);
}

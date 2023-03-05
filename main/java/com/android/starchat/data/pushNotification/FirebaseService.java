package com.android.starchat.data.pushNotification;



import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.starchat.R;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.ui.uiMain.mainActivity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "notification_channel";
    private static final String CHANNEL_NAME = "star chat";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        ApplicationUser user = ((MainApplication)getApplication()).getUser();
        if(message.getNotification()!=null){
            if(user.getId().equals(message.getNotification().getTitle()))
                return;
            generateNotification(message.getNotification().getTitle(),message.getNotification().getBody());
        }
    }

    public void generateNotification(String title, String message){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Notification notification = builder.build();
        notificationManager.notify(0,notification);
    }

    private RemoteViews getRemoteView(String title, String message) {
        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.l_notification);
        remoteViews.setTextViewText(R.id.notificationTitle,title);
        remoteViews.setTextViewText(R.id.notificationMessage,message);
        return remoteViews;
    }



}

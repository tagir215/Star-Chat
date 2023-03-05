package com.android.starchat.data.pushNotification;

public class PushNotification {
    private NotificationData notification;
    private String to;

    public PushNotification(NotificationData notification, String to) {
        this.notification = notification;
        this.to = to;
    }

    public NotificationData getNotification() {
        return notification;
    }

    public void setNotification(NotificationData data) {
        this.notification = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

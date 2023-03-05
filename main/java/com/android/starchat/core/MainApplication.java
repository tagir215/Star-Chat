package com.android.starchat.core;

public class MainApplication extends android.app.Application {

    private ApplicationUser user;
    @Override
    public void onCreate() {
        super.onCreate();
        user = new ApplicationUser(this);

    }
    public ApplicationUser getUser(){
        return user;
    }




}

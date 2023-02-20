package com.android.starchat.core;

import com.android.starchat.contacts.User;

public class MainApplication extends android.app.Application {

    private ApplicationUser user;
    @Override
    public void onCreate() {
        super.onCreate();
        user = new ApplicationUser(this);
        if(user.getId()!=null)
            user.createDaoUser();
    }
    public ApplicationUser getUser(){
        return user;
    }




}

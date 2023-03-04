package com.android.starchat.data.contacts;

import android.content.Intent;
import android.net.Uri;

import com.android.starchat.ui.uiMain.mainActivity.MainActivity;

public class ContactPhone {
    private final String name;
    private final String phoneNumber;
    private final MainActivity mainActivity;

    public ContactPhone(MainActivity mainActivity, String name, String phoneNumber) {
        this.mainActivity = mainActivity;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void invite(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String message = "Hello "+name+"! Let's chat on STAR CHAT! Pretty much everyone uses it at this point!";
        intent.setData(Uri.parse("sms:"+phoneNumber));
        intent.putExtra("sms_body",message);
        mainActivity.startActivity(intent);
    }


}

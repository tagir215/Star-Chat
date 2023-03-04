package com.android.starchat.core;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnlineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatus(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatus(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setStatus(false);
    }
    private void setStatus(boolean status){
        ApplicationUser user = ((MainApplication) getApplication()).getUser();
        if (user.getDaoUser() != null) {
            user.getDaoUser().updateStatus(status);
        }
    }
}

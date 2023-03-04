package com.android.starchat.core;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ApplicationUserTest {
    private final Context context = ApplicationProvider.getApplicationContext();
    private final String phoneNumber = "+35855511111";
    @Test
    public void testGenerateId() {
        ApplicationUser user = new ApplicationUser(context);
        user.setPhone(phoneNumber);
        user.generateId();
        String time =String.valueOf( System.currentTimeMillis());
        String expected = "user"+user.getPhone()+time;
        assertEquals(expected.substring(0,20),user.getId().substring(0,20));
    }




}
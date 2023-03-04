package com.android.starchat.util;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.starchat.data.user.User;
import com.android.starchat.core.ApplicationUser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FileHelperTest {
    Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void testSaveAndLoad() {
        String phoneNumber = "+3585551111";
        ApplicationUser user = new ApplicationUser(context);
        user.setPhone(phoneNumber);
        user.generateId();
        user.saveUser();
        User userLoaded = FileHelper.loadUser(context);
        assertEquals(user.getId(),userLoaded.getId());
        assertEquals(user.getAbout(),userLoaded.getAbout());
        assertEquals(user.getPhone(),userLoaded.getPhone());
        assertEquals(user.getName(),userLoaded.getName());
    }
}
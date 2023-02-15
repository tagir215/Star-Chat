package com.android.starchat.contacts;

import static org.junit.Assert.*;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Random;


public class GroupTest {

    @Test
    public void testGenerateGroupId() {
        Group group = new Group();
        User user = new User();
        user.setId("user3582");
        group.generateGroupId(user);
        String systemTime = String.valueOf(System.currentTimeMillis());
        String idWithoutChar = user.getId().replaceAll("[^\\d]","");
        assertEquals("group"+idWithoutChar+systemTime,group.getId());
        System.out.println(group.getId());
    }
}
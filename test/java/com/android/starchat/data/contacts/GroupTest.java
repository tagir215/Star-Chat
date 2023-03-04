package com.android.starchat.data.contacts;

import static org.junit.Assert.*;

import com.android.starchat.data.group.Group;
import com.android.starchat.data.user.User;

import org.junit.Test;


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
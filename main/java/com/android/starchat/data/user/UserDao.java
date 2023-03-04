package com.android.starchat.data.user;

import com.google.firebase.database.DatabaseReference;

public interface UserDao {
    void create();
    void createContact(User contact);
    void updateStatus(boolean online);
}

package com.android.starchat.data.group;
import com.android.starchat.data.user.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public interface GroupDao {
    void createGroup();
    void subscribe(String id);
    void updateTime(DatabaseReference databaseReference);
    void updateText(String text);
    void deleteGroup();
    void updateLastVisited(User user);
    void addValueEventListener(ValueEventListener valueEventListener);
    void removeValueEventListener();

}

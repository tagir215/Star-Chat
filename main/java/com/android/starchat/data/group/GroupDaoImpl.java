package com.android.starchat.data.group;

import com.android.starchat.data.user.User;
import com.android.starchat.util.DateHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;


public class GroupDaoImpl implements GroupDao{
    private final Group group;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference("groups");
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = firebaseStorage.getReference("images");
    private DatabaseReference textR;
    private ValueEventListener valueEventListener;

    public GroupDaoImpl(Group group){
        this.group = group;
    }

    public void createGroup(){
        DatabaseReference groupR = databaseReference.child(group.getId());
        groupR.child("id").setValue(group.getId());
        groupR.child("name").setValue(group.getName());
        groupR.child("lastMessage").setValue("new group");
        updateTime(groupR);
        if(group.getGroupJPEG()!=null){
            StorageReference photoR = storageReference.child(group.getId());
            photoR.putBytes(group.getGroupJPEG());
        }
        DatabaseReference groupM = groupR.child("members");
        List<String>memberIds = group.getMemberIds();
        for(String id : memberIds){
            groupM.child(id).setValue(DateHandler.dateToString(new Date()));
        }
        subscribe(group.getId());
    }


    public void subscribe(String id){
        FirebaseMessaging.getInstance().subscribeToTopic(id);
    }


    public void updateTime(DatabaseReference databaseReference){
        databaseReference.child("date").setValue(DateHandler.dateToString(new Date()));
    }

    public void updateText(String text){
        textR = databaseReference.child(group.getId()).child("text");
        textR.child(DateHandler.dateToString(new Date())).setValue(text);
        databaseReference.child(group.getId()).child("lastMessage").setValue(text);
        updateTime(databaseReference.child(group.getId()));
    }

    public void deleteGroup(){
        databaseReference.child(group.getId()).removeValue();
    }

    public void updateLastVisited(User user){
        DatabaseReference membersR = databaseReference.child(group.getId()).child("members");
        membersR.child(user.getId()).setValue(DateHandler.dateToString(new Date()));
    }

    public void addValueEventListener(ValueEventListener valueEventListener){
        if(this.valueEventListener!=null)
            return;
        this.valueEventListener = valueEventListener;
        textR = databaseReference.child(group.getId()).child("text");
        textR.limitToLast(1).addValueEventListener(valueEventListener);

    }
    public void removeValueEventListener(){
        if(valueEventListener==null)
            return;
        textR.removeEventListener(valueEventListener);
        valueEventListener = null;
    }



}

package com.android.starchat.firebase;

import com.android.starchat.contacts.Group;
import com.android.starchat.contacts.User;
import com.android.starchat.util.DateHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DAOGroup {
    private final Group group;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference("groups");
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = firebaseStorage.getReference("images");
    private DatabaseReference textR;
    private ValueEventListener valueEventListener;

    public DAOGroup(Group group){
        this.group = group;
    }

    public void initGroup(){
        DatabaseReference groupR = databaseReference.child(group.getId());
        groupR.child("id").setValue(group.getId());
        groupR.child("name").setValue(group.getName());
        groupR.child("lastMessage").setValue("new group");
        uploadTime(groupR);
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

    private void subscribe(String id){
        FirebaseMessaging.getInstance().subscribeToTopic(id);
    }


    private void uploadTime(DatabaseReference databaseReference){
        databaseReference.child("date").setValue(DateHandler.dateToString(new Date()));
    }

    public void uploadText(String text){
        textR = databaseReference.child(group.getId()).child("text");
        textR.child(DateHandler.dateToString(new Date())).setValue(text);
        databaseReference.child(group.getId()).child("lastMessage").setValue(text);
        uploadTime(databaseReference.child(group.getId()));
    }

    public void deleteGroup(){
        databaseReference.child(group.getId()).removeValue();
    }

    public void uploadLastVisited(User user){
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
        textR.removeEventListener(valueEventListener);
        valueEventListener = null;
    }

    public DatabaseReference getTextR() {
        return textR;
    }

}

package com.android.starchat.firebase;

import com.android.starchat.contacts.Group;
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
        int i=0;
        for(String id : memberIds){
            groupM.child("member"+i).setValue(id);
            i++;
        }
        subscribe(group.getId());
    }

    private void subscribe(String id){
        FirebaseMessaging.getInstance().subscribeToTopic(id);
    }


    private void uploadTime(DatabaseReference databaseReference){
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        databaseReference.child("date").setValue(dateFormat.format(new Date()));
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        databaseReference.child("time").setValue(timeFormat.format(new Date()));
    }

    public void uploadText(String text){
        textR = databaseReference.child(group.getId()).child("text");
        textR.child(String.valueOf(System.currentTimeMillis())).setValue(text);
        databaseReference.child(group.getId()).child("lastMessage").setValue(text);
        uploadTime(databaseReference.child(group.getId()));
    }

    public String getText(){
        return "wow";
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
    }

    public DatabaseReference getTextR() {
        return textR;
    }

}

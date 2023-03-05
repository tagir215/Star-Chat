package com.android.starchat.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.starchat.data.contacts.ContactPhone;
import com.android.starchat.data.group.Group;
import com.android.starchat.data.user.User;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.util.DateHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class FirebaseRealtimeDatabase {
    private static final String TAG = "Firebase";
    public static void getUserByPhoneNumber(ContactPhone contact, ApplicationUser applicationUser, Listener listener){
        String name = contact.getName();
        String phoneNumber = contact.getPhoneNumber();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        Query query = databaseReference.orderByChild("phone").equalTo(phoneNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot : snapshot.getChildren()){
                    String phoneF = userSnapshot.child("phone").getValue(String.class);
                    if(phoneF!=null && phoneF.equals(phoneNumber)){
                        User user = userSnapshot.getValue(User.class);
                        user.setName(name);
                        applicationUser.getUserContacts().add(user);
                        applicationUser.getDaoUser().createContact(user);
                        listener.onDataChanged();
                        Firestore.getUserPhoto(user,listener);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG,error +" search by phone failed");
            }
        });
    }

    public static void getUsersByIds(List<User>userList , List<String>userIds, Listener listener){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        for(String id : userIds){
            Query query = databaseReference.orderByChild("id").equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot userSnapshot : snapshot.getChildren()){
                        User user = userSnapshot.getValue(User.class);
                        userList.add(user);
                        listener.onDataChanged();
                        Firestore.getUserPhoto(user,listener);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG,error+" couldn't find user");
                }
            });
        }
    }

    public static void getGroupsByUser(ApplicationUser user, Listener listener){
        DatabaseReference groupsR = FirebaseDatabase.getInstance().getReference("groups");

        Query query = groupsR.orderByChild("members/"+user.getId()).startAt("");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot groupSnapShot : snapshot.getChildren()){

                    Group group = groupSnapShot.getValue(Group.class);
                    user.getGroupHashMap().put(group.getId(), group);
                    Firestore.getGroupPhoto(group,listener);
                    getMembers(group,groupSnapShot,user.getId(),listener);
                    listener.onDataChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG,"group get fail");
            }
        });
    }

    private static void getMembers(Group group, DataSnapshot groupSnapshot,String userId,Listener listener){
        DatabaseReference membersR = groupSnapshot.child("members").getRef();
        membersR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot memberSnapshot : snapshot.getChildren()){
                    group.getMemberIds().add(memberSnapshot.getKey());
                    if(memberSnapshot.getKey().equals(userId))
                        group.setLastVisited(memberSnapshot.getValue(String.class));
                }
                getText(group,groupSnapshot,listener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG,"member not found");
            }
        });
    }

    private static void getText(Group group, DataSnapshot groupSnapshot,Listener listener){
        DatabaseReference textR = groupSnapshot.child("text").getRef();
        Date currentDate = DateHandler.stringToDate(group.getLastVisited());
        textR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot text : snapshot.getChildren()){
                    group.getTextList().add(text.getValue(String.class));
                    Date date = DateHandler.stringToDate(text.getKey());
                    if(currentDate.compareTo(date)<0){
                        group.setNewMessages(group.getNewMessages()+1);
                        listener.onDataChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG,"text not found");
            }
        });
    }


    public interface Listener{
        void onDataChanged();
    }

}

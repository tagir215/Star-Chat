package com.android.starchat.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.starchat.contacts.ContactPhone;
import com.android.starchat.contacts.Group;
import com.android.starchat.contacts.User;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.util.Constants;
import com.android.starchat.util.DateHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;

public class FireBaseQueries {

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
                        applicationUser.getDaoUser().uploadContact(user);
                        listener.onDataChanged();
                        getUserPhoto(user,listener);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase",error +" search by phone failed");
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
                        getUserPhoto(user,listener);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase",error+" couldn't find user");
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
                    getGroupPhoto(group,listener);
                    getMembers(group,groupSnapShot,user.getId(),listener);
                    listener.onDataChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
                Log.e("Firebase","member not found");
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
                Log.e("Firebase","text not found");
            }
        });
    }

    public static void getUserPhoto(User user, Listener listener){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference photoR = firebaseStorage.getReference().child("images").child(user.getId());
        photoR.getBytes(Constants.MAX_FIREBASE_IMAGE_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                user.setPhoto(bytes);
                listener.onDataChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase","photo not found");
            }
        });
    }

    public static void getGroupPhoto(Group group,Listener listener){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference photoR = firebaseStorage.getReference().child("images").child(group.getId());
        photoR.getBytes(Constants.MAX_FIREBASE_IMAGE_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                group.setGroupJPEG(bytes);
                listener.onDataChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase","photo not found");
            }
        });
    }

    public interface Listener{
        void onDataChanged();
    }

}

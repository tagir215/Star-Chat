package com.android.starchat.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.starchat.contacts.ContactPhone;
import com.android.starchat.contacts.Group;
import com.android.starchat.contacts.User;
import com.android.starchat.core.ApplicationUser;
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

import java.util.List;
import java.util.Objects;

public class FireBase {

    public static void getUsersByPhoneNumber(ContactPhone contact, List<User> userList, Listener listener){
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
                        User starUser = userSnapshot.getValue(User.class);
                        starUser.setName(name);
                        userList.add(starUser);
                        listener.onDataChanged();
                        getUserPhoto(starUser,listener);
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

    public static void getGroupsByUsers(ApplicationUser user, Listener listener){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("groups");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot groupSnapshots : snapshot.getChildren()){

                    DataSnapshot members = groupSnapshots.child("members");
                    for(DataSnapshot memberSnapshots : members.getChildren()){

                        if(Objects.equals(memberSnapshots.getValue(String.class), user.getId())){
                            Group group = groupSnapshots.getValue(Group.class);
                            for (DataSnapshot member : members.getChildren()){
                                group.getMemberIds().add(member.getValue(String.class));
                            }
                            DataSnapshot text = groupSnapshots.child("text");
                            for(DataSnapshot message : text.getChildren()){
                                group.getTextList().add(message.getValue(String.class));
                            }
                            getGroupPhoto(group,listener);
                            user.getGroupHashMap().put(group.getId(), group);
                            listener.onDataChanged();
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase",error+" couldn't find group");
            }
        });


    }

    public static void getUserPhoto(User user, Listener listener){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference photoR = firebaseStorage.getReference().child("images").child(user.getId());
        photoR.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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
        photoR.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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

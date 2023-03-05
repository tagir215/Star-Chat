package com.android.starchat.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import com.android.starchat.data.group.Group;
import com.android.starchat.data.user.User;
import com.android.starchat.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Firestore {
    private static final String TAG = "Firestore";

    public static void getGroupPhoto(Group group, FirebaseRealtimeDatabase.Listener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                        Log.e(TAG,"photo not found");
                    }
                });
            }
        }).start();

    }

    public static void getUserPhoto(User user, FirebaseRealtimeDatabase.Listener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                        Log.e(TAG,"photo not found");
                    }
                });
            }
        }).start();

    }
}

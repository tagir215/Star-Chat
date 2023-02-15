package com.android.starchat.firebase;
import com.android.starchat.contacts.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DAOUser {
    private final User user;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference photoR = firebaseStorage.getReference().child("images");
    public DAOUser(User user) {
        this.user = user;
    }

    public void upload(){
        DatabaseReference userR = databaseReference.child(user.getId());
        userR.child("id").setValue(user.getId());
        userR.child("phone").setValue(user.getPhone());
        userR.child("about").setValue(user.getAbout());
        userR.child("name").setValue(user.getName());
        if(user.getPhoto()!=null)
            photoR.child(user.getId()).putBytes(user.getPhoto());
    }


}

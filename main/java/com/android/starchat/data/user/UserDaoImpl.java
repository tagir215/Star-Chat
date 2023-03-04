package com.android.starchat.data.user;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserDaoImpl implements UserDao{
    private final User user;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference photoR = firebaseStorage.getReference().child("images");
    public UserDaoImpl(User user) {
        this.user = user;
    }

    public void create(){
        DatabaseReference userR = databaseReference.child(user.getId());
        userR.child("id").setValue(user.getId());
        userR.child("phone").setValue(user.getPhone());
        userR.child("about").setValue(user.getAbout());
        userR.child("name").setValue(user.getName());
        if(user.getPhoto()!=null)
            photoR.child(user.getId()).putBytes(user.getPhoto());
    }
    public void createContact(User contact){
        DatabaseReference contactsR = databaseReference.child(user.getId()).child("contacts");
        contactsR.child(contact.getPhone()).setValue(contact.getName());

    }
    public void updateStatus(boolean online){
        DatabaseReference statusR = databaseReference.child(user.getId()).child("online");
        if(online){
            statusR.setValue(true);
        }else{
            statusR.setValue(false);
        }
    }

}

package com.android.starchat.core;

import android.content.Context;
import android.util.Log;
import com.android.starchat.contacts.ContactPhone;
import com.android.starchat.contacts.Group;
import com.android.starchat.contacts.User;
import com.android.starchat.firebase.DAOUser;
import com.android.starchat.util.Constants;
import com.android.starchat.util.FileHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ApplicationUser extends User {
    private final Context context;
    private DAOUser daoUser;
    private List<ContactPhone>phoneContacts = new ArrayList<>();
    private List<User> userContacts = new ArrayList<>();
    private HashMap<String,Group> groupHashMap = new HashMap<>();


    public ApplicationUser(Context context) {
        this.context = context;
        loadUser();
    }

    public void createDaoUser(){
        daoUser = new DAOUser(this);
        daoUser.upload();
    }


    public void generateId() {
        long systemTime = System.currentTimeMillis();
        Random random = new Random(systemTime);
        int r = random.nextInt(10000);
        setId("user" + getPhone()+""+ systemTime + "" +r);
        setName("Citizen"+r);
    }

    public void saveUser(){
        FileHelper.saveUser(context,getCopy());
    }


    private void loadUser() {
        File dir = new File(context.getFilesDir(), Constants.USER);
        if (!dir.exists())
            return;
        File file = new File(dir, "user");
        if (!file.exists())
            return;
        User user = FileHelper.loadUser(context);
        setId(user.getId());
        setName(user.getName());
        setPhone(user.getPhone());
        setAbout(user.getAbout());
        setPhoto(user.getPhoto());
    }


    public List<ContactPhone> getPhoneContacts() {
        return phoneContacts;
    }

    public void setPhoneContacts(List<ContactPhone> phoneContacts) {
        this.phoneContacts = phoneContacts;
    }

    public HashMap<String, Group> getGroupHashMap() {
        return groupHashMap;
    }

    public void setGroupHashMap(HashMap<String, Group> groups) {
        this.groupHashMap = groups;
    }

    public List<User> getUserContacts() {
        return userContacts;
    }

    public void setUserContacts(List<User> userContacts) {
        this.userContacts = userContacts;
    }

    public User getCopy(){
        User user2 = new User();
        user2.setId(getId());
        user2.setPhone(getPhone());
        user2.setName(getName());
        user2.setAbout(getAbout());
        user2.setPhoto(getPhoto());
        return user2;
    }

    public DAOUser getDaoUser() {
        if(daoUser==null)
            daoUser=new DAOUser(this);
        return daoUser;
    }
}

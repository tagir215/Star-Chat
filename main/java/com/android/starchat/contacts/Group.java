package com.android.starchat.contacts;

import com.android.starchat.firebase.DAOGroup;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String phone;
    private String id;
    private String name;
    private String lastMessage;
    private String time;
    private String date;
    private List<String>memberIds = new ArrayList<>();
    private final List<String>textList = new ArrayList<>();
    private byte[] groupJPEG;
    private List<User>userList = new ArrayList<>();
    private DAOGroup daoGroup;

    public Group(){}

    public void generateGroupId(User user){
        String time =String.valueOf(System.currentTimeMillis());
        String ddd = user.getId().replaceAll("[^\\d]","");
        id ="group"+ddd+""+time;
    }

    public void uploadToFirebase(){
        daoGroup = new DAOGroup(this);
        daoGroup.initGroup();
    }


    public String getPhone() {
        return phone;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getMemberIds() {
        return memberIds;
    }
    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
    public List<String>getTextList(){
        return textList;
    }
    public DAOGroup getDaoGroup() {
        return daoGroup;
    }
    public List<User> getUserList() {
        return userList;
    }

    public byte[] getGroupJPEG() {
        return groupJPEG;
    }

    public void setGroupJPEG(byte[] groupJPEG) {
        this.groupJPEG = groupJPEG;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}

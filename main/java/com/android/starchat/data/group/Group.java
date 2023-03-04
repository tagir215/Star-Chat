package com.android.starchat.data.group;

import com.android.starchat.data.user.User;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String phone;
    private String id;
    private String name;
    private String lastMessage;
    private String date;
    private List<String>memberIds = new ArrayList<>();
    private final List<String>textList = new ArrayList<>();
    private byte[] groupJPEG;
    private final List<User>userList = new ArrayList<>();
    private GroupDaoImpl groupDaoImpl;
    private int newMessages;
    private String lastVisited;

    public Group(){}

    public void generateGroupId(User user){
        String time =String.valueOf(System.currentTimeMillis());
        String ddd = user.getId().replaceAll("[^\\d]","");
        id ="group"+ddd+""+time;
    }

    public void uploadToFirebase(){
        if(groupDaoImpl ==null)
            groupDaoImpl = new GroupDaoImpl(this);
        groupDaoImpl.createGroup();
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

    public void setDaoGroup(GroupDaoImpl groupDaoImpl) {
        this.groupDaoImpl = groupDaoImpl;
    }

    public GroupDaoImpl getDaoGroup() {
        if(groupDaoImpl ==null)
            groupDaoImpl = new GroupDaoImpl(this);
        return groupDaoImpl;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNewMessages() {
        return newMessages;
    }

    public void setNewMessages(int newMessages) {
        this.newMessages = newMessages;
    }

    public String getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(String lastVisited) {
        this.lastVisited = lastVisited;
    }
}

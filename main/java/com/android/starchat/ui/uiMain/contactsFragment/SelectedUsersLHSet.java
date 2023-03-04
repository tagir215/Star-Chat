package com.android.starchat.ui.uiMain.contactsFragment;

import com.android.starchat.data.user.User;

import java.util.LinkedHashSet;

public class SelectedUsersLHSet {
    private final LinkedHashSet<User>linkedHashSet;
    private Listener listener;
    public SelectedUsersLHSet(){
        linkedHashSet = new LinkedHashSet<>();
    }
    public boolean contains(User user){
        if(linkedHashSet.contains(user)){
            linkedHashSet.remove(user);
            listener.onChange();
            return true;
        }
        else{
            linkedHashSet.add(user);
            listener.onChange();
            return false;
        }
    }
    public LinkedHashSet getLinkedHasSet(){
        return linkedHashSet;
    }

    public void addListener(Listener listener){
        this.listener = listener;
    }

    public interface Listener{
        void onChange();
    }

}

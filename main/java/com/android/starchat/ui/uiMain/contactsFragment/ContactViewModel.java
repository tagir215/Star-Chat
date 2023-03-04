package com.android.starchat.ui.uiMain.contactsFragment;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.data.contacts.ContactManager;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;

public class ContactViewModel extends ViewModel {

    public void createRecyclerViewContacts(Context context, RecyclerView recyclerView){
        ApplicationUser user = ((MainApplication)context.getApplicationContext()).getUser();
        if(user.getPhoneContacts()==null)
            return;
        recyclerView.setNestedScrollingEnabled(false);
        RVAdapterPhoneContacts adapter = new RVAdapterPhoneContacts(context,user.getPhoneContacts());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    public void createRecyclerViewUsers(Context context, RecyclerView recyclerView,SelectedUsersLHSet selectedUsersLHSet){
        ApplicationUser user = ((MainApplication)context.getApplicationContext()).getUser();
        RVAdapterUsers adapter = new RVAdapterUsers(context,user.getUserContacts(),selectedUsersLHSet);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        ContactManager.createUsers(context, new ContactManager.Listener() {
            @Override
            public void onChange() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}

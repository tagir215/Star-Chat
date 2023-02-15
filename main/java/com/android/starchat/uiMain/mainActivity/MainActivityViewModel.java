package com.android.starchat.uiMain.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.R;
import com.android.starchat.contacts.ContactManager;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.firebase.FireBase;
import com.android.starchat.uiMain.contactsFragment.ContactViewModel;
import com.android.starchat.uiMain.contactsFragment.ContactsFragment;
import com.android.starchat.uiMain.profileFragment.ProfileFragment;
import com.android.starchat.uiMain.contactsFragment.ChooserFragment;
import com.android.starchat.uiStart.StartActivity;

public class MainActivityViewModel extends ViewModel {


    public void handleContacts(MainActivity mainActivity){
        ContactManager.createContacts(mainActivity);
        ApplicationUser user = ((MainApplication)mainActivity.getApplication()).getUser();
        if(!user.getPhoneContacts().isEmpty()){
            mainActivity.startContactsFragment();
        }else{
            Toast.makeText(mainActivity,"No contacts found",Toast.LENGTH_SHORT).show();
        }
    }

    public void createRecyclerViewGroups(Context context, RecyclerView recyclerView){
        ApplicationUser user = ((MainApplication)context.getApplicationContext()).getUser();
        recyclerView.setNestedScrollingEnabled(false);
        RVAdapterGroups adapter = new RVAdapterGroups(context);
        if(user.getGroupHashMap().isEmpty()){
            FireBase.getGroupsByUsers(user, new FireBase.Listener() {
                @Override
                public void onDataChanged() {
                    adapter.notifyDataSetChanged();
                }
            });
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


}

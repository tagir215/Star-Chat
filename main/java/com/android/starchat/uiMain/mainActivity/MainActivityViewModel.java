package com.android.starchat.uiMain.mainActivity;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.firebase.FireBaseQueries;

public class MainActivityViewModel extends ViewModel {


    public void createRecyclerViewGroups(Context context, RecyclerView recyclerView){
        ApplicationUser user = ((MainApplication)context.getApplicationContext()).getUser();
        recyclerView.setNestedScrollingEnabled(false);
        RVAdapterGroups adapter = new RVAdapterGroups(context);
        user.getGroupHashMap().clear();
        FireBaseQueries.getGroupsByUsers(user, new FireBaseQueries.Listener() {
            @Override
            public void onDataChanged() {
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


}

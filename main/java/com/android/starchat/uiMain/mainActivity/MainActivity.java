package com.android.starchat.uiMain.mainActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.android.starchat.R;
import com.android.starchat.contacts.ContactManager;
import com.android.starchat.contacts.Group;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.core.OnlineActivity;
import com.android.starchat.firebase.DAOGroup;
import com.android.starchat.uiMain.contactsFragment.ChooserFragment;
import com.android.starchat.uiMain.contactsFragment.ContactsFragment;
import com.android.starchat.uiMain.profileFragment.ProfileFragment;
import com.android.starchat.uiStart.StartActivity;

public class MainActivity extends OnlineActivity {

    private MainActivityViewModel viewModel;
    private final int REQUEST_CODE_CONTACTS = 0;
    private final int REQUEST_CODE_NEW_GROUP = 1;
    private RecyclerView recyclerViewGroups;
    private MainApplication mainApplication;
    private String contextMenuTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApplication = (MainApplication) getApplication();
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        setContactsButton();
        setDisplayShowTitleEnabledBecauseOnCreateOptionsMenuIsNotCalledForSomeReason();
        recyclerViewGroups = findViewById(R.id.mainRecyclerViewGroups);
        if(mainApplication.getUser().getId()==null)
            startStartActivity();

    }



    private void setContactsButton(){
        ImageButton contactsButton = findViewById(R.id.mainContactsButton);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContactManager.checkContactPermission(MainActivity.this,REQUEST_CODE_CONTACTS))
                    handlePermissionResult(REQUEST_CODE_CONTACTS);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]!=PackageManager.PERMISSION_GRANTED)
            return;
       handlePermissionResult(requestCode);
    }

    private void handlePermissionResult(int requestCode){
        ApplicationUser user = ((MainApplication)getApplication()).getUser();
        if(user.getPhoneContacts().isEmpty())
            ContactManager.createContacts(this);
        if(user.getPhoneContacts().isEmpty())
            return;

        if(requestCode==REQUEST_CODE_CONTACTS){
            startContactsFragment();
        }else if(requestCode==REQUEST_CODE_NEW_GROUP){
            startChooserFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        contextMenuTag = (String)v.getTag();
        getMenuInflater().inflate(R.menu.group_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.menuDeleteGroup){
            ApplicationUser user = ((MainApplication)getApplication()).getUser();
            Group group =user.getGroupHashMap().get(contextMenuTag);
            group.setDaoGroup(new DAOGroup(group));
            group.getDaoGroup().deleteGroup();
            viewModel.createRecyclerViewGroups(this,recyclerViewGroups);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuMainProfile)
            startProfileFragment();
        if(item.getItemId()==R.id.menuMainNewGroup){
            if(ContactManager.checkContactPermission(MainActivity.this,REQUEST_CODE_NEW_GROUP)){
                handlePermissionResult(REQUEST_CODE_NEW_GROUP);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDisplayShowTitleEnabledBecauseOnCreateOptionsMenuIsNotCalledForSomeReason(){
        Toolbar toolbar =  findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void startProfileFragment() {
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.mainFragmentContainer, new ProfileFragment())
            .commit();
    }
    private void startChooserFragment(){
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.mainFragmentContainer,new ChooserFragment())
            .commit();
    }

    private void startStartActivity(){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    protected void startContactsFragment(){
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.mainFragmentContainer,new ContactsFragment())
            .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mainApplication.getUser().getId()!=null)
            viewModel.createRecyclerViewGroups(this,recyclerViewGroups);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mainFragmentContainer);
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
        else
            super.onBackPressed();
    }

    private final CountingIdlingResource idlingResource = new CountingIdlingResource("Firebase");

    public CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }

}

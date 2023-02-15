package com.android.starchat.uiMain.mainActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.android.starchat.R;
import com.android.starchat.contacts.ContactManager;
import com.android.starchat.core.MainApplication;
import com.android.starchat.uiMain.contactsFragment.ChooserFragment;
import com.android.starchat.uiMain.contactsFragment.ContactsFragment;
import com.android.starchat.uiMain.profileFragment.ProfileFragment;
import com.android.starchat.uiStart.StartActivity;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication mainApplication = (MainApplication) getApplication();
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        setContactsButton();
        setDisplayShowTitleEnabledBecauseOnCreateOptionsMenuIsNotCalledForSomeReason();
        RecyclerView recyclerViewGroups = findViewById(R.id.mainRecyclerViewGroups);
        viewModel.createRecyclerViewGroups(this,recyclerViewGroups);
        if(mainApplication.getUser().getId()==null)
            startStartActivity();
    }



    private void setContactsButton(){
        ImageButton contactsButton = findViewById(R.id.mainContactsButton);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.handleContacts(MainActivity.this);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            viewModel.handleContacts(MainActivity.this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuMainProfile)
            startProfileFragment();
        if(item.getItemId()==R.id.menuMainNewGroup)
            startChooserFragment();
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

    private CountingIdlingResource idlingResource = new CountingIdlingResource("Firebase");

    public CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }
}

package com.android.starchat.ui.uiChat;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.starchat.R;
import com.android.starchat.data.group.Group;
import com.android.starchat.core.OnlineActivity;
import com.android.starchat.openGL.glRenderer.GLRenderer;
import com.android.starchat.ui.uiMain.mainActivity.MainActivity;

public class ChatActivity extends OnlineActivity {
    private GLSurfaceView glSurfaceView;
    private GLRenderer renderer;
    private ChatActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);
        viewModel.setGroup(this);
        setContentView(R.layout.activity_chat);
        setOpenGL();
        viewModel.setMotionEventHandler(renderer);
        viewModel.setScrollPosition(this,renderer,findViewById(R.id.chatConstraintLayoutLines),findViewById(R.id.chatLine),findViewById(R.id.chatLineFull));
        setToolbar();
        setSendButton();
        setTouchControls();
        viewModel.setValueEventListenerForNewMessages();
        Toolbar toolbar = findViewById(R.id.toolbarChat);
        TextView textView = toolbar.findViewById(R.id.toolbarChatGroupName);
        textView.setText(getGroup().getName());
    }




    private void setOpenGL(){
        glSurfaceView = findViewById(R.id.glSurfaceView);
        glSurfaceView.setEGLContextClientVersion(2);
        renderer = new GLRenderer(this);
        glSurfaceView.setRenderer(renderer);
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbarChat);
        setDisplayShowTitleEnabledBecauseOnCreateOptionsMenuIsNotCalledForSomeReason(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setSendButton(){
        final EditText editText = findViewById(R.id.mainEditText);
        viewModel.startUpText(renderer);
        ImageButton imageButton = findViewById(R.id.mainSendButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.textToRenderer(editText,renderer);
            }
        });
    }

    private void setTouchControls(){
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return viewModel.handleMotionEvent(motionEvent);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        viewModel.removeValueEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        viewModel.setValueEventListenerForNewMessages();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.removeValueEventListener();
    }

    public Group getGroup(){
        return viewModel.getGroup();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.group_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.chatFragmentContainer);
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuGroupSettings){
            Fragment groupFragment = new GroupFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.chatFragmentContainer,groupFragment).commit();
        }
        return true;
    }
    private void setDisplayShowTitleEnabledBecauseOnCreateOptionsMenuIsNotCalledForSomeReason(Toolbar toolbar){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
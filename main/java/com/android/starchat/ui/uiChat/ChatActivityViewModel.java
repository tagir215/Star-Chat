package com.android.starchat.ui.uiChat;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;

import com.android.starchat.data.group.Group;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.data.pushNotification.NotificationManager;
import com.android.starchat.openGL.glRenderer.GLRenderer;
import com.android.starchat.openGL.glRenderer.MotionEventHandler;
import com.android.starchat.openGL.glRenderer.ScrollPosition;
import com.android.starchat.util.FileHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.List;

public class ChatActivityViewModel extends ViewModel {

    private StringBuilder stringBuilder = new StringBuilder();
    private float textDistance;
    private boolean started;
    private GLRenderer renderer;
    MotionEventHandler motionEventHandler;
    private Group group;
    private File saveFile;
    private ApplicationUser user;


    public void setGroup(ChatActivity chatActivity){
        if(group==null){
            user = ((MainApplication)chatActivity.getApplication()).getUser();
            String groupId = chatActivity.getIntent().getStringExtra("group");
            if(groupId==null){
                group = new Group();
                group.setName("group not found");
            }
            else{
                group = user.getGroupHashMap().get(groupId);
            }
            if(group!=null)
                appendStringList(group.getTextList());


        }
    }
    public void setMotionEventHandler(GLRenderer renderer){
        motionEventHandler = new MotionEventHandler(renderer);
    }
    public void setScrollPosition(Activity activity,GLRenderer renderer, ConstraintLayout csLayout, View timeline, View fullTimeLine){
        renderer.setScrollPosition(new ScrollPosition(activity,renderer,csLayout,timeline,fullTimeLine));
    }

    protected boolean handleMotionEvent(MotionEvent event){

        return motionEventHandler.handleMotionEvent(event);
    }


    protected void setFile(File file){
        this.saveFile = file;
        load();
    }

    private void load(){
        if(stringBuilder.length()!=0)
            return;
        stringBuilder.append(FileHelper.loadInternalStorage(saveFile));
    }
    private void save(){
        if(stringBuilder.length()>0)
            FileHelper.saveInternalStorage(saveFile,stringBuilder.toString());
    }





    protected void startUpText(GLRenderer renderer){
        this.renderer = renderer;
        renderer.setText(stringBuilder.toString());
        renderer.getScrollPosition().setDistance(textDistance);
    }

    protected void textToRenderer(EditText editText, GLRenderer renderer){
        String text =user.getName()+":   "+ editText.getText().toString();
        if(text.equals(user.getName()+":   "))
            text = user.getName()+":   ...";
        getGroup().getDaoGroup().updateText(text);
        updateRenderer();
        editText.setText("");
        textDistance = renderer.getScrollPosition().getTargetDistance();
        NotificationManager.sendNotification(group.getName(), text, group.getId());
    }


    public void updateRenderer(){
        renderer.createTextObject(stringBuilder.toString());
    }



    private void appendStringList(List<String>stringList){
        if(!stringList.isEmpty()){
            stringBuilder = new StringBuilder();
            String groupName = group.getName().replaceAll(" ","-");
            stringBuilder.append("t:"+groupName+" \n\n");
            for(String text : stringList){
                stringBuilder.append(text+"\n\n");
            }
        }
    }

    public void setValueEventListenerForNewMessages(){
        getGroup().getDaoGroup().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getGroup().getDaoGroup().updateLastVisited(user);
                if(!started){
                    started = true;
                    return;
                }

                for(DataSnapshot ds : snapshot.getChildren()){
                    String text = ds.getValue(String.class);
                    group.getTextList().add(text);
                    appendStringList(group.getTextList());
                    updateRenderer();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeValueEventListener(){
        getGroup().getDaoGroup().removeValueEventListener();
        started = false;
    }

    public Group getGroup(){
        return group;
    }

}

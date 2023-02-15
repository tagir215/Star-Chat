package com.android.starchat.uiChat;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.android.starchat.contacts.Group;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.firebase.DAOGroup;
import com.android.starchat.glRenderer.GLRenderer;
import com.android.starchat.util.FileHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.List;

public class ChatActivityViewModel extends ViewModel {

    private StringBuilder stringBuilder = new StringBuilder();
    private float textDistance;
    private float oldY;
    private float momentum;
    private volatile boolean interruptMomentum;
    private boolean started;
    private GLRenderer renderer;
    private Group group;
    private DAOGroup daoGroup;
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
                daoGroup = new DAOGroup(group);
            }
            if(group!=null)
                appendStringList(group.getTextList());
        }
    }


    Runnable momentumRunnable = new Runnable() {
        @Override
        public void run() {
            momentumLoop();
        }
    };

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

    private void momentumLoop(){
        while (Math.abs(momentum)>0.01f){
            if(interruptMomentum)
                break;

            momentum = momentum/1.1f;
            renderer.scrollDistance(momentum);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    protected void startUpText(GLRenderer renderer){
        this.renderer = renderer;
        renderer.setText(stringBuilder.toString());
        renderer.setDistance(textDistance);

    }

    protected void textToRenderer(EditText editText, GLRenderer renderer){
        String text =user.getName()+":   "+ editText.getText().toString();
        if(text.equals(user.getName()+":   "))
            text = user.getName()+":   ...";
        daoGroup.uploadText(text);
        updateRenderer();
        editText.setText("");
        textDistance = renderer.getTargetDistance();
    }


    public void updateRenderer(){
        renderer.createTextObject(stringBuilder.toString());
    }

    protected boolean handleMotionEvent(View view, MotionEvent event, GLSurfaceView glSurfaceView, GLRenderer renderer){
        if(event==null)
            return false;


        float y = event.getY();
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            oldY = y;
            interruptMomentum = true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            interruptMomentum = false;
            Thread thread = new Thread(momentumRunnable);
            thread.start();
            return false;
        }

        momentum = y-oldY;
        renderer.scrollDistance(momentum);
        oldY = y;
        return true;
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
        if(daoGroup==null)
            return;
        daoGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
        daoGroup.removeValueEventListener();
    }

    public Group getGroup(){
        return group;
    }

}

package com.android.starchat.ui.uiMain.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.R;
import com.android.starchat.data.group.Group;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.ui.uiChat.ChatActivity;
import com.android.starchat.util.DateHandler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RVAdapterGroups extends RecyclerView.Adapter<RVAdapterGroups.HolderChats> {

    private final LayoutInflater inflater;
    private final MainActivity mainActivity;
    private final ApplicationUser user;
    private List<String>groupIds = new ArrayList<>();
    private final Date currentDate;

    public RVAdapterGroups(Context context) {
        mainActivity = (MainActivity) context;
        inflater = LayoutInflater.from(context);
        user = ((MainApplication)mainActivity.getApplication()).getUser();
        currentDate = new Date();
    }

    @NonNull
    @Override
    public HolderChats onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.l_group,parent,false);
        return new HolderChats(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterGroups.HolderChats holder, int position) {
        Group group = user.getGroupHashMap().get(groupIds.get(position));
        holder.titleText.setText(group.getName());

        Date date = DateHandler.stringToDate(group.getDate());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.getDefault());
        if(dateFormat.format(date).equals(dateFormat.format(currentDate))){
            DateFormat timeInstance = DateFormat.getTimeInstance(DateFormat.SHORT,Locale.getDefault());
            holder.dateText.setText(timeInstance.format(date));
        }else{
            holder.dateText.setText(dateFormat.format(date));
        }

        holder.lastMessage.setText(group.getLastMessage());
        if(group.getGroupJPEG()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(group.getGroupJPEG(),0,group.getGroupJPEG().length);
            holder.imageButton.setImageBitmap(bitmap);
        }else{
            holder.imageButton.setImageResource(R.drawable.ic_baseline_person_24);
        }

        if(group.getNewMessages()>0){
            holder.newMessages.setText(String.valueOf(group.getNewMessages()));
            holder.newMessages.setVisibility(View.VISIBLE);
        }else{
            holder.newMessages.setVisibility(View.INVISIBLE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, ChatActivity.class);
                intent.putExtra("group",group.getId());
                mainActivity.startActivity(intent);
            }
        });
        holder.layout.setTag(group.getId());
        mainActivity.registerForContextMenu(holder.layout);
    }

    @Override
    public int getItemCount() {
        groupIds = new ArrayList<>();
        groupIds.addAll(user.getGroupHashMap().keySet());
        return user.getGroupHashMap().size();
    }


    protected static class HolderChats extends RecyclerView.ViewHolder{
        ConstraintLayout layout;
        ImageButton imageButton;
        TextView titleText;
        TextView dateText;
        TextView lastMessage;
        TextView newMessages;
        public HolderChats(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.menuHolderPhoto);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout = itemView.findViewById(R.id.menuHolderLayout);
            titleText = itemView.findViewById(R.id.menuHolderTitle);
            dateText = itemView.findViewById(R.id.menuHolderDate);
            lastMessage = itemView.findViewById(R.id.menuHolderLastMessage);
            newMessages = itemView.findViewById(R.id.menuHolderNewMessages);
        }
    }




}

package com.android.starchat.uiMain.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.android.starchat.contacts.Group;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.uiChat.ChatActivity;

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
    private String currentDate;
    public RVAdapterGroups(Context context) {
        mainActivity = (MainActivity) context;
        inflater = LayoutInflater.from(context);
        user = ((MainApplication)mainActivity.getApplication()).getUser();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        currentDate = dateFormat.format(new Date());
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
        if(currentDate.equals(group.getDate()))
            holder.dateText.setText(group.getTime());
        else
            holder.dateText.setText(group.getDate());
        holder.lastMessage.setText(group.getLastMessage());
        if(group.getGroupJPEG()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(group.getGroupJPEG(),0,group.getGroupJPEG().length);
            holder.imageButton.setImageBitmap(bitmap);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, ChatActivity.class);
                intent.putExtra("group",group.getId());
                mainActivity.startActivity(intent);
            }
        });
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
        public HolderChats(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.menuHolderPhoto);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout = itemView.findViewById(R.id.menuHolderLayout);
            titleText = itemView.findViewById(R.id.menuHolderTitle);
            dateText = itemView.findViewById(R.id.menuHolderDate);
            lastMessage = itemView.findViewById(R.id.menuHolderLastMessage);
        }
    }


}

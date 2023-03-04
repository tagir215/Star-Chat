package com.android.starchat.ui.uiChat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.R;
import com.android.starchat.data.group.Group;
import com.android.starchat.data.user.User;

public class RVAdapterEndless extends RecyclerView.Adapter<RVAdapterEndless.MemberHolder> {

    private final LayoutInflater inflater;
    private int SIZE;
    private final Group group;

    public RVAdapterEndless(Context context, Group group) {
        this.group = group;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.l_member,parent,false);
        return new MemberHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MemberHolder holder, int position) {
        int actualPosition = getActualPosition(position);
        User user = group.getUserList().get(actualPosition);
        holder.message.setText(user.getAbout());
        holder.number.setText(user.getName());
        if(user.isOnline()){
            holder.status.setText("online");
            holder.status.setTextColor(Color.GREEN);
        }else{
            holder.status.setText("offline");
            holder.status.setTextColor(Color.RED);
        }
        if(user.getPhoto()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getPhoto(),0,user.getPhoto().length);
            holder.imageButton.setImageBitmap(bitmap);
        }else{
            holder.imageButton.setImageResource(R.drawable.ic_baseline_person_24);
        }


    }


    private int getActualPosition(int position){
        int actualPosition = position;
        while (actualPosition>SIZE-1){
            actualPosition-=SIZE;
        }
        return actualPosition;
    }

    @Override
    public int getItemCount() {
        SIZE = group.getUserList().size();
        if(SIZE==0)
            return 0;
        else
            return 1000;
    }


    protected class MemberHolder extends RecyclerView.ViewHolder{
        ImageButton imageButton;
        float scale = 1;
        TextView number;
        TextView message;
        TextView status;
        ConstraintLayout layout;
        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.memberNumber);
            layout = itemView.findViewById(R.id.memberLayout);
            status = itemView.findViewById(R.id.memberStatus);
            message = itemView.findViewById(R.id.memberMessage);
            imageButton = itemView.findViewById(R.id.memberPhoto);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}

package com.android.starchat.ui.uiMain.contactsFragment;

import android.content.Context;
import android.graphics.Color;
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
import com.android.starchat.data.user.User;
import com.android.starchat.util.BitmapHelper;

import java.util.List;

public class RVAdapterUsers extends RecyclerView.Adapter<RVAdapterUsers.UserHolder> {
    private final List<User> userList;
    private final LayoutInflater inflater;
    private final SelectedUsersLHSet selectedUsers;

    public RVAdapterUsers(Context context, List<User> userList,SelectedUsersLHSet selectedUsers) {
        this.userList = userList;
        this.selectedUsers = selectedUsers;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.l_user,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getName());
        holder.phone.setText(user.getPhone());
        if(user.getPhoto()!=null)
            holder.imageButton.setImageBitmap(BitmapHelper.getBitmap_fromJPEG(user.getPhoto()));

        if(selectedUsers==null)
            return;
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = userList.get(holder.getAdapterPosition());
                if(selectedUsers.contains(user)){
                    holder.name.setTextColor(Color.WHITE);
                }
                else{
                    holder.name.setTextColor(Color.BLUE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phone;
        ImageButton imageButton;
        ConstraintLayout layout;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            phone = itemView.findViewById(R.id.userPhoneNumber);
            imageButton = itemView.findViewById(R.id.userImageButton);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout = itemView.findViewById(R.id.userLayout);
        }
    }
}

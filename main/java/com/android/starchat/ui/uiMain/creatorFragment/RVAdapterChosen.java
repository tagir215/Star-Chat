package com.android.starchat.ui.uiMain.creatorFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.starchat.R;
import com.android.starchat.data.user.User;
import com.android.starchat.util.BitmapHelper;

public class RVAdapterChosen extends RecyclerView.Adapter<RVAdapterChosen.ChosenHolder> {
    Object[]users;
    LayoutInflater inflater;

    public RVAdapterChosen(Context context, Object[] users) {
        this.users = users;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RVAdapterChosen.ChosenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.l_chosen,parent,false);
        return new ChosenHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterChosen.ChosenHolder holder, int position) {
        User user = (User)users[position];
        holder.name.setText(user.getName());
        if(user.getPhoto()!=null)
            holder.imageView.setImageBitmap(BitmapHelper.getBitmap_fromJPEG(user.getPhoto()));
    }

    @Override
    public int getItemCount() {
        return users.length;
    }

    public class ChosenHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout layout;
        ImageView imageView;

        public ChosenHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chosenName);
            layout = itemView.findViewById(R.id.chosenLayout);
            imageView = itemView.findViewById(R.id.chosenPhoto);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}

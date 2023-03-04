package com.android.starchat.ui.uiMain.contactsFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.R;
import com.android.starchat.data.contacts.ContactPhone;

import java.util.List;

public class RVAdapterPhoneContacts extends RecyclerView.Adapter<RVAdapterPhoneContacts.ContactHolder> {

    List<ContactPhone>contactList;
    LayoutInflater inflater;
    public RVAdapterPhoneContacts(Context context, List<ContactPhone> contactList) {
        this.contactList = contactList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.l_contact,parent,false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        holder.name.setText(contactList.get(position).getName());
        holder.phoneNumber.setText(contactList.get(position).getPhoneNumber());
        holder.inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactList.get(holder.getAdapterPosition()).invite();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phoneNumber;
        Button inviteButton;
        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contactName);
            phoneNumber = itemView.findViewById(R.id.contactNumber);
            inviteButton = itemView.findViewById(R.id.contactInvite);
        }
    }
}

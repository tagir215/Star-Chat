package com.android.starchat.ui.uiMain.contactsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.R;

public class ContactsFragment extends Fragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerViewPhone = view.findViewById(R.id.contactsRecyclerViewPhone);
        ContactViewModel viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        viewModel.createRecyclerViewContacts(getActivity(), recyclerViewPhone);
        RecyclerView recyclerViewUsers = view.findViewById(R.id.contactsRecyclerViewUsers);
        viewModel.createRecyclerViewUsers(getActivity(), recyclerViewUsers,null);
        setToolbar(view);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts,container,false);
    }

    private void setToolbar(View view){
        Toolbar toolbar = view.findViewById(R.id.contactsToolbar);
        toolbar.setTitle(getResources().getString(R.string.contacts));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactsFragment.this.getParentFragmentManager().beginTransaction().remove(ContactsFragment.this).commit();
            }
        });
    }



}

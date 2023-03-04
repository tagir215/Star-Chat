package com.android.starchat.ui.uiMain.contactsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.R;
import com.android.starchat.ui.uiMain.creatorFragment.CreatorFragment;

public class ChooserFragment extends Fragment {
    private RecyclerView recyclerView;
    private SelectedUsersLHSet selectedUsers;
    private ImageButton ok;
    private ContactViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooser,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerViewChooser);
        selectedUsers = new SelectedUsersLHSet();
        viewModel.createRecyclerViewUsers(getContext(),recyclerView,selectedUsers);
        ok = view.findViewById(R.id.chooserOk);
        setToolbar(view);
        handleSelected();
        setOkButton();
    }

    private void setToolbar(View view){
        Toolbar toolbar = view.findViewById(R.id.chooserToolbar);
        toolbar.setTitle(getResources().getString(R.string.new_group));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooserFragment.this.getParentFragmentManager().beginTransaction().remove(ChooserFragment.this).commit();
            }
        });
    }

    private void setOkButton(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreatorFragment();
            }
        });
    }

    private void handleSelected(){
        selectedUsers.addListener(new SelectedUsersLHSet.Listener() {
            @Override
            public void onChange() {
                if(selectedUsers.getLinkedHasSet().size()>0){
                    ok.setVisibility(View.VISIBLE);
                }else{
                    ok.setVisibility(View.INVISIBLE);
                }
            }
        });
    }





    private void startCreatorFragment(){
        CreatorFragment fragment = new CreatorFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected",selectedUsers.getLinkedHasSet());
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentContainer,fragment)
                .addToBackStack(null)
                .commit();
    }
}

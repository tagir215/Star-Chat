package com.android.starchat.ui.uiMain.profileFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.starchat.R;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.data.user.UserDaoImpl;
import com.android.starchat.util.FileHelper;

public class EditFragment extends Fragment {
    private EditText editText;
    private Button save;
    private Button cancel;
    private ApplicationUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.l_edit,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = ((MainApplication)getActivity().getApplication()).getUser();
        save = view.findViewById(R.id.editSave);
        cancel = view.findViewById(R.id.editCancel);
        editText = view.findViewById(R.id.editEditText);
        setButtons();
        editText.requestFocus();
    }

    private void setButtons(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String argument = getArguments().getString("setting");
                if(argument.equals("name"))
                    user.setName(editText.getText().toString());
                if(argument.equals("about"))
                    user.setAbout(editText.getText().toString());
                if(argument.equals("phone"))
                    user.setPhone(editText.getText().toString());

                UserDaoImpl userDaoImpl = new UserDaoImpl(user);
                userDaoImpl.create();
                FileHelper.saveUser(getContext(),user.getCopy());
                refresh();
                closeFragment();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
    }
    private void refresh(){
        Fragment fragment = new ProfileFragment();
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentContainer,fragment)
                .commit();
    }

    private void closeFragment(){
        getParentFragmentManager().beginTransaction().remove(EditFragment.this).commit();
    }
}

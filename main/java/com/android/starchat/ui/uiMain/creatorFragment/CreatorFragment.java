package com.android.starchat.ui.uiMain.creatorFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.starchat.R;
import com.android.starchat.data.group.Group;
import com.android.starchat.data.user.User;
import com.android.starchat.core.MainApplication;
import com.android.starchat.ui.uiMain.contactsFragment.ChooserFragment;
import com.android.starchat.util.BitmapHelper;
import com.android.starchat.util.Crop;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class CreatorFragment extends Fragment {
    private EditText editText;
    private ImageButton imageButton;
    private ImageButton ok;
    private RecyclerView recyclerView;
    private LinkedHashSet<User>selectedUsers;
    private ActivityResultLauncher<String>activityResultLauncher;
    private Bitmap bitmap;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEditText(view);
        imageButton = view.findViewById(R.id.creatorGroupPhoto);
        recyclerView = view.findViewById(R.id.creatorRecyclerView);
        ok = view.findViewById(R.id.creatorOk);
        setRecyclerView();
        setOKButton();
        setToolbar(view);
        setImagePicker();

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator,container,false);
        return view;
    }

    private void setRecyclerView(){
        selectedUsers = (LinkedHashSet<User>) getArguments().getSerializable("selected");
        Object[] users = selectedUsers.toArray();
        RVAdapterChosen rvAdapterChosen = new RVAdapterChosen(getContext(),(users));
        recyclerView.setAdapter(rvAdapterChosen);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
    }

    private void setEditText(View view){
        editText = view.findViewById(R.id.creatorGroupNameEditText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText.getText().length()>0){
                    ok.setVisibility(View.VISIBLE);
                }
                else{
                    ok.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setOKButton(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = new Group();
                group.setName(editText.getText().toString());
                if(bitmap!=null)
                    group.setGroupJPEG(BitmapHelper.getJPEG_fromBitmap(bitmap));
                List<String> userIds = new ArrayList<>();
                for(User user : selectedUsers){
                    userIds.add(user.getId());
                }
                MainApplication mainApplication =(MainApplication) getActivity().getApplication();
                userIds.add(mainApplication.getUser().getId());
                group.setMemberIds(userIds);
                group.generateGroupId(mainApplication.getUser());
                group.uploadToFirebase();
                closeFragment();
            }
        });
    }


    private void closeFragment(){
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        getActivity().startActivity(intent);

    }

    private void setToolbar(View view){
        Toolbar toolbar = view.findViewById(R.id.creatorToolbar);
        toolbar.setTitle(getResources().getString(R.string.new_group));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment();
            }
        });
    }

    private void replaceFragment(){
        CreatorFragment.this.getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentContainer,new ChooserFragment())
                .commit();
    }


    private void setImagePicker(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                bitmap = BitmapHelper.getBitmap_fromUri(getContext(),result);
                bitmap = Crop.cropCircle(bitmap);
                imageButton.setImageBitmap(bitmap);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch("image/*");
            }
        });
    }

}

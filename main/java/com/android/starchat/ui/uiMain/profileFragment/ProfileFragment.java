package com.android.starchat.ui.uiMain.profileFragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.starchat.R;
import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.data.user.UserDaoImpl;
import com.android.starchat.util.BitmapHelper;
import com.android.starchat.util.Crop;
import com.android.starchat.util.FileHelper;
import com.android.starchat.util.SelectionInterface;

public class ProfileFragment extends Fragment {
    private View view;
    private ImageButton imageButton;
    private ActivityResultLauncher<String>activityResultLauncher;
    private ApplicationUser user;
    private Bitmap bitmapCropped;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        this.view = view;
        setToolbar();
        setSettings();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user = ((MainApplication)getActivity().getApplication()).getUser();
        setImageButton(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private int[] settingNames = {
            R.string.profile_name,
            R.string.profile_about,
            R.string.profile_number,
    };
    private int[] images = {
            R.drawable.ic_baseline_person_24,
            R.drawable.ic_baseline_info_24,
            R.drawable.ic_baseline_phone_24,
    };
    private void editName(){
        Bundle bundle = new Bundle();
        bundle.putString("setting","name");
        startEditFragment(bundle);
    }
    private void editPhone(){
        Bundle bundle = new Bundle();
        bundle.putString("setting","phone");
        startEditFragment(bundle);
    }
    private void editAbout(){
        Bundle bundle = new Bundle();
        bundle.putString("setting","about");
        startEditFragment(bundle);
    }

    SelectionInterface[] actions = new SelectionInterface[]{
            this::editName,
            this::editAbout,
            this::editPhone,
    };


    private void startEditFragment(Bundle bundle){
        Fragment fragment = new EditFragment();
        fragment.setArguments(bundle);
        getParentFragmentManager()
                .beginTransaction()
                .add(R.id.profileFragmentContainer,fragment)
                .commit();
    }


    private void setSettings(){
        ConstraintLayout[] layouts = new ConstraintLayout[images.length];
        layouts[0] = view.findViewById(R.id.profileSettingName);
        layouts[1] = view.findViewById(R.id.profileSettingAbout);
        layouts[2] = view.findViewById(R.id.profileSettingNumber);
        MainApplication mainApplication = (MainApplication) getActivity().getApplicationContext();
        String[] values = {
                mainApplication.getUser().getName(),
                mainApplication.getUser().getAbout(),
                mainApplication.getUser().getPhone(),
        };

        for (int i=0; i<layouts.length; i++){
            ((ImageView)layouts[i].findViewById(R.id.editImage)).setImageResource(images[i]);
            ((TextView)layouts[i].findViewById(R.id.mainEditText)).setText(view.getResources().getString(settingNames[i]));
            ((TextView)layouts[i].findViewById(R.id.editValue)).setText(values[i]);
            int finalI = i;
            ((ImageButton)layouts[i].findViewById(R.id.profileEdit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actions[finalI].doAction();
                }
            });
        }



    }



    private void setToolbar(){
        Toolbar toolbar = view.findViewById(R.id.profileToolbar);
        toolbar.setTitle(getResources().getString(R.string.profile));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
    }

    private void closeFragment(){
        ProfileFragment.this.getParentFragmentManager()
                .beginTransaction()
                .remove(ProfileFragment.this)
                .commit();
    }

    private void setImageButton(View view){
        imageButton = view.findViewById(R.id.profileImage);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Bitmap bitmap = BitmapHelper.getBitmap_fromUri(getContext(),result);
                bitmapCropped = Crop.cropCircle(bitmap);
                imageButton.setImageBitmap(bitmapCropped);
                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                user.setPhoto(BitmapHelper.getJPEG_fromBitmap(bitmapCropped));
                UserDaoImpl userDaoImpl = new UserDaoImpl(user);
                userDaoImpl.create();
                FileHelper.saveUser(getContext(),user.getCopy());
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch("image/*");
            }
        });

        if(user.getPhoto()!=null){
            imageButton.setImageBitmap(BitmapHelper.getBitmap_fromJPEG(user.getPhoto()));
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

    }




}

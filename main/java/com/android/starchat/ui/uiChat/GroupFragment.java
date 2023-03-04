package com.android.starchat.ui.uiChat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.R;
import com.android.starchat.data.group.Group;
import com.android.starchat.data.firebase.FirebaseRealtimeDatabase;
import com.android.starchat.util.BitmapHelper;

public class GroupFragment extends Fragment {

    private View view;
    private Context context;
    private boolean positionSet;
    private Group group;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group,container,false);
        this.view = view;
        this.context = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        group = ((ChatActivity)getActivity()).getGroup();
        setPhoto(view);
        setTexts(view);
        createRecyclerView();
        setCloseButton();
    }

    private void setPhoto(View view){
        ImageButton groupPhoto = view.findViewById(R.id.toolbarGroupPhoto);
        groupPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(group.getGroupJPEG()!=null)
            groupPhoto.setImageBitmap(BitmapHelper.getBitmap_fromJPEG(group.getGroupJPEG()));
    }
    private void setTexts(View view){
        TextView groupTitle = view.findViewById(R.id.toolbarGroupName);
        groupTitle.setText(group.getName());
        TextView participants = view.findViewById(R.id.toolbarGroupParticipants);
        String s = group.getMemberIds().size()+" "+getResources().getString(R.string.participants);
        participants.setText(s);
    }

    private void createRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewGroup);
        LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        manager.scrollToPosition(500);
        RVAdapterEndless adapter = new RVAdapterEndless(context,group);
        Group group = ((ChatActivity)getActivity()).getGroup();
        group.getUserList().clear();
        FirebaseRealtimeDatabase.getUsersByIds(group.getUserList(), group.getMemberIds(), new FirebaseRealtimeDatabase.Listener() {
            @Override
            public void onDataChanged() {
                adapter.notifyDataSetChanged();
                if(!positionSet){
                    manager.scrollToPosition(500);
                    positionSet = true;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        setSettingButtons();
        setScrollEffect(recyclerView);
    }
    private void setScrollEffect(RecyclerView recyclerView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int screenCenter = displayMetrics.widthPixels / 2;

            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    LinearLayoutManager manager =(LinearLayoutManager)recyclerView.getLayoutManager();
                    int firstPos = manager.findFirstVisibleItemPosition();
                    int lastPos = manager.findLastVisibleItemPosition();
                    for (int n=firstPos; n<lastPos; n++){
                        RVAdapterEndless.MemberHolder holder = (RVAdapterEndless.MemberHolder) recyclerView.findViewHolderForAdapterPosition(n);
                        int[] pos = new int[2];
                        holder.layout.getLocationOnScreen(pos);
                        float center;
                        float width = (float)holder.layout.getWidth()*holder.scale;
                        center = pos[0] + width/2;

                        float distanceFromCenter = Math.abs( center - screenCenter );
                        float scale =  1.2f- (distanceFromCenter/3f/screenCenter);
                        float alpha = 1.5f- (distanceFromCenter/screenCenter);
                        float pivot =(center-screenCenter) / screenCenter*2;
                        holder.layout.setPivotX(width/2 +pivot*2);
                        holder.layout.setAlpha(alpha);
                        holder.layout.setScaleX(scale);
                        holder.layout.setScaleY(scale);
                        holder.scale = scale;
                    }
                }
            });
        }
    }

    private void setCloseButton(){
        Toolbar toolbar = view.findViewById(R.id.toolbarGroup);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupFragment.this.getParentFragmentManager()
                        .beginTransaction()
                        .remove(GroupFragment.this)
                        .commit();
            }
        });
    }




    private void setSettingButtons(){
        int[] resources= {
                R.drawable.ic_baseline_person_add_24,
                R.drawable.ic_baseline_add_link_24,
                R.drawable.ic_baseline_person_remove_24,
                R.drawable.ic_baseline_notifications_24,
                R.drawable.ic_baseline_image_24,
        };
        String[] settingTexts = {
                "add People",
                "add via link",
                "leave group",
                "notification settings",
                "media settings",
        };

        int[] iconColors = {
                ContextCompat.getColor(context,R.color.white),
                ContextCompat.getColor(context,R.color.white),
                ContextCompat.getColor(context,R.color.black),
                ContextCompat.getColor(context,R.color.setting_color),
                ContextCompat.getColor(context,R.color.setting_color),
        };
        ColorStateList[] backgroundColors = {
                ContextCompat.getColorStateList(context,R.color.green),
                ContextCompat.getColorStateList(context,R.color.green),
                ContextCompat.getColorStateList(context,R.color.red),
                ColorStateList.valueOf(Color.alpha(0)),
                ColorStateList.valueOf(Color.alpha(0)),
        };

        LinearLayout[] layouts = new LinearLayout[settingTexts.length];
        layouts [0]= view.findViewById(R.id.groupAddPerson);
        layouts [1]= view.findViewById(R.id.groupAddViaLink);
        layouts [2]= view.findViewById(R.id.groupLeaveGroup);
        layouts [3]= view.findViewById(R.id.groupNotificationSettings);
        layouts [4]= view.findViewById(R.id.groupMediaSettings);
        for (int i=0; i< layouts.length; i++){
            ImageView imageView = ((ImageView)layouts[i].findViewById(R.id.settingImage));
            ((TextView)layouts[i].findViewById(R.id.settingText)).setText(settingTexts[i]);;
            imageView.setImageResource(resources[i]);
            imageView.setBackgroundTintList(backgroundColors[i]);
            imageView.setColorFilter(iconColors[i],PorterDuff.Mode.SRC_ATOP);

        }

    }
}

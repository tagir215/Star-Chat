package com.android.starchat.ui.uiStart;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.starchat.R;

public class StartActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    private StartActivityViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        viewModel = new ViewModelProvider(this).get(StartActivityViewModel.class);
        viewModel.setActivity(this);
        setOpenRecyclerButton();
        setOkButton();
    }

    private void setOpenRecyclerButton(){
        ImageButton button = findViewById(R.id.startRecyclerOpenButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyclerView();
            }
        });
    }

    private void setOkButton(){
        ImageButton button = findViewById(R.id.startOk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.ok();
            }
        });
    }

    private void setRecyclerView(){
        if(recyclerView!=null){
            recyclerView.setVisibility(View.VISIBLE);
            return;
        }
        recyclerView = findViewById(R.id.startRecyclerView);
        RVAdapterCountries adapter = new RVAdapterCountries(this,RegionHelper.createCountries(viewModel));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.startVerifyFragment(viewModel.number);
                    return;
                } else {
                    Toast.makeText(StartActivity.this,"Permission not granted",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}

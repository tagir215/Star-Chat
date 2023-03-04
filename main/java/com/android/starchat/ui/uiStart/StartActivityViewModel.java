package com.android.starchat.ui.uiStart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.android.starchat.R;
import com.android.starchat.core.MainApplication;
import com.android.starchat.ui.uiMain.mainActivity.MainActivity;
import com.android.starchat.util.PermissionHelper;

public class StartActivityViewModel extends ViewModel {
    private Country selectedCountry;
    StartActivity startActivity;
    String number;

    public void setActivity(StartActivity startActivity){
        this.startActivity = startActivity;
    }

    public void setSelectedCountry(Country country){
        if(country!=null)
            selectedCountry = country;
        startActivity.recyclerView.setVisibility(View.INVISIBLE);
        TextView textView = startActivity.findViewById(R.id.startCodeText);
        textView.setText("+"+selectedCountry.getCode());
    }

    public void ok(){
        MainApplication application = (MainApplication) startActivity.getApplication();
        TextView textView = startActivity.findViewById(R.id.startCodeText);
        EditText editText = startActivity.findViewById(R.id.startEditText);
        number = textView.getText()+""+editText.getText();
        application.getUser().setPhone(number);
        if(!checkPermissions())
            return;
        startVerifyFragment(number);

    }
    private boolean checkPermissions(){
        String permission = Manifest.permission.SEND_SMS;
        if(ContextCompat.checkSelfPermission(startActivity, permission)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(startActivity,permission)){
                createRequestRationaleDialog();
            }else{
                ActivityCompat.requestPermissions(startActivity,new String[]{Manifest.permission.SEND_SMS},0);
            }
            return false;
        }
        return true;
    }
    private void createRequestRationaleDialog(){
        new AlertDialog.Builder(startActivity)
                .setTitle("SMS permission required")
                .setMessage("Please allow the SMS permission in the Star Chat application settings. " +
                        "Star Chat requires access to your SMS messages in order to " +
                        "verify your phone number. We will send a verification " +
                        "code to the number you provide, and use that code to ensure " +
                        "that you are the owner of the phone. This information will be " +
                        "used for security purposes only.")
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PermissionHelper.openAppSettings(startActivity);
                    }
                })
                .setNegativeButton("CANCEL",null)
                .create()
                .show();

    }


    public void startVerifyFragment(String number){
        Fragment fragment = new VerifyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("number",number);
        fragment.setArguments(bundle);
        startActivity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.startFragmentContainer,fragment)
                .commit();
    }
    private void startMainActivity(){
        Intent intent = new Intent(startActivity, MainActivity.class);
        startActivity.startActivity(intent);
    }


}

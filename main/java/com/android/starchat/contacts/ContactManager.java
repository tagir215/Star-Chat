package com.android.starchat.contacts;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.starchat.core.ApplicationUser;
import com.android.starchat.core.MainApplication;
import com.android.starchat.firebase.FireBase;
import com.android.starchat.uiMain.mainActivity.MainActivity;
import com.android.starchat.util.PermissionHelper;
import java.util.ArrayList;
import java.util.List;

public class ContactManager {

    private static boolean checkContactPermission(MainActivity mainActivity){
        if(ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity,Manifest.permission.READ_CONTACTS)){
                createContactPermissionRationaleDisplay(mainActivity);
            }else{
                ActivityCompat.requestPermissions(mainActivity,new String[]{Manifest.permission.READ_CONTACTS},0);
            }
            return false;
        }
        return true;
    }


    private static void createContactPermissionRationaleDisplay(MainActivity mainActivity){
        new AlertDialog.Builder(mainActivity)
                .setTitle("Contacts Permission required")
                .setMessage("Please allow the Contacts Permission. Star Chat will help you connect " +
                        "to your contacts. The information will not be used for anything else lol")
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PermissionHelper.openAppSettings(mainActivity);
                    }
                })
                .setNegativeButton("CANCEL",null)
                .create()
                .show();

    }


    public static void createContacts(MainActivity mainActivity){
        if(!checkContactPermission(mainActivity))
            return;
        List<ContactPhone> contactList = new ArrayList<>();
        ContentResolver contentResolver = mainActivity.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cursor != null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                String number = "";
                int columnIndexId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                String id = cursor.getString(columnIndexId);
                int columnIndexName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String name = cursor.getString(columnIndexName);
                int columnIndexHasNumber = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                int hasNumber = Integer.parseInt(cursor.getString(columnIndexHasNumber));
                if(hasNumber>0){
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (phoneCursor.moveToNext()) {
                        int columnIndexNumber = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        number = phoneCursor.getString(columnIndexNumber);
                    }
                }
                contactList.add(new ContactPhone(mainActivity,name,number));
            }
        }
        ((MainApplication)mainActivity.getApplication()).getUser().setPhoneContacts(contactList);
    }


    public static void createUsers(Context context, Listener listener){
        ApplicationUser user = ((MainApplication)context.getApplicationContext()).getUser();
        if(user.getUserContacts().isEmpty()){
            List<ContactPhone> contactList = user.getPhoneContacts();
            for(int i=0; i<contactList.size(); i++){
                FireBase.getUsersByPhoneNumber(contactList.get(i), user.getUserContacts(), new FireBase.Listener() {
                    @Override
                    public void onDataChanged() {
                        listener.onChange();
                    }
                });
            }
        }
    }

    public interface Listener{
        void onChange();
    }




}

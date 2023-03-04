package com.android.starchat.ui.uiStart;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
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
import com.android.starchat.ui.uiMain.mainActivity.MainActivity;

public class VerifyFragment extends Fragment {
    private EditText editText;
    private Button resend;
    private Button changeNumber;
    private String phoneNumber;
    private String code = "4444";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verify,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        phoneNumber = getArguments().getString("number");
        editText = view.findViewById(R.id.verifyCodeInput);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                handleTyping(editText.getText().toString());
                return false;
            }
        });

        resend = view.findViewById(R.id.verifyResend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });

        changeNumber = view.findViewById(R.id.verifyChange);
        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });

        sendSMS();
    }
    private void closeFragment(){
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }
    private void handleTyping(String string){
        if(string.equals(code)){
            ApplicationUser user =  ((MainApplication)getContext().getApplicationContext()).getUser();
            user.setPhone(phoneNumber);
            user.setAbout(getResources().getString(R.string.default_about));
            if(user.getId()==null){
                user.generateId();
                user.saveUser();
            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            getContext().startActivity(intent);
        }
    }

    private void sendSMS(){
        SmsManager smsManager = SmsManager.getDefault();
        String message = "Your verification code is "+code;
        smsManager.sendTextMessage(phoneNumber,null,message,null,null);
    }
}

package com.android.starchat.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.android.starchat.R;
import com.android.starchat.data.user.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

public class FileHelper {

    public static String fileToString(InputStream inputStream){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) != null){
                stringBuilder.append(line +"\n");
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }



    public static Bitmap getTextureBitmap(Context context,int screenWidth, int screenHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        return BitmapFactory.decodeResource(context.getResources(), R.drawable.star_wars_font,options);
    }




    public static void saveInternalStorage(File file, String string){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadInternalStorage(File file){
        String text;
        try {
            InputStream inputStream =new FileInputStream(file);
            text =fileToString(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return text;
    }


    public static void saveUser(Context context, User user){
        File dir = new File(context.getFilesDir(),Constants.USER);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir,"user");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject((User)user);
            fos.close();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static User loadUser(Context context){
        File dir = new File(context.getFilesDir(),Constants.USER);
        User user = null;
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir,"user");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            user = (User) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

}

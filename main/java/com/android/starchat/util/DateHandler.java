package com.android.starchat.util;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHandler {
    public static String dateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        return simpleDateFormat.format(date);
    }
    public static Date stringToDate(String string){
        if(string==null)
            return new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date date;
        try {
            date = simpleDateFormat.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
}

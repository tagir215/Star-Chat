package com.android.starchat.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class Crop {
    public static Bitmap cropCircle(Bitmap bitmap){
        int size;
        if(bitmap.getWidth()>bitmap.getHeight())
            size = bitmap.getHeight();
        else
            size = bitmap.getWidth();
        Bitmap cropped = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cropped);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0,size,size);
        paint.setAntiAlias(true);
        canvas.drawCircle(size/2f,size/2f,size/2f,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,rect,rect,paint);
        return cropped;
    }
}

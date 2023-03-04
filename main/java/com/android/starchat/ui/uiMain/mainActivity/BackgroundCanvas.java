package com.android.starchat.ui.uiMain.mainActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.starchat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BackgroundCanvas extends View {

    private Path path;
    private Paint linePaint;
    private Paint starPaint;
    private int screenWidth;
    private int screenHeight;
    private int startY = 10;
    private int lineCount;
    private int lineSpacing;
    private List<int[]> stars;
    private boolean drawn;


    public BackgroundCanvas(Context context) {
        super(context);
        init(null);
    }

    public BackgroundCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public BackgroundCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }


    private void init(AttributeSet attrs) {
        path = new Path();
        starPaint = new Paint();
        starPaint.setColor(Color.GRAY);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(getResources().getColor(R.color.half_opaque_green));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeWidth(1f);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        lineSpacing = 10;
        lineCount = screenHeight/lineSpacing;
        Random random = new Random();
        stars = new ArrayList<>();
        for(int i=0; i<100; i++){
            int x = random.nextInt(screenWidth);
            int y = random.nextInt(screenHeight);
            int w = random.nextInt(3);
            stars.add(new int[]{x,y,w});
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.reset();
        canvas.drawColor(Color.BLACK);
        for (int i = 0; i < lineCount; i++) {
            path.moveTo(0, startY + i * lineSpacing);
            path.lineTo(screenWidth, startY + i * lineSpacing);
        }
        canvas.drawPath(path, linePaint);
        for(int i=0; i<stars.size(); i++){
            int[] star = stars.get(i);
            canvas.drawCircle(star[0],star[1],star[2],starPaint);
        }

    }


}

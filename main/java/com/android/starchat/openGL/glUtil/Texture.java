package com.android.starchat.openGL.glUtil;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.starchat.util.FileHelper;

public class Texture {

    public static int generateTexture(Context context){
        final int[] textureObjectIds = new int[1];

        glGenTextures(1,textureObjectIds,0);
        if(textureObjectIds[0]==0)
            Log.e("textures","something wrong texture ");
        glBindTexture(GL_TEXTURE_2D,textureObjectIds[0]);
        setParameters();
        loadBitmapToOpenGL(context);
        glBindTexture(GL_TEXTURE_2D,0);

        return textureObjectIds[0];
    }

    private static void setParameters(){
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    }
    private static void loadBitmapToOpenGL(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Bitmap bitmap = FileHelper.getTextureBitmap(context,displayMetrics.widthPixels,displayMetrics.heightPixels);
        texImage2D(GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();
        glGenerateMipmap(GL_TEXTURE_2D);
    }
}

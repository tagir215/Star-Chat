package com.android.starchat.glRenderer;

import static android.opengl.Matrix.setLookAtM;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.android.starchat.glObject.StarObject;
import com.android.starchat.glText.WordManager;
import com.android.starchat.util.Constants;
import com.android.starchat.glUtil.MatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    private final Context context;

    private float[] mvpMatrixText = new float[16];
    private float[] mvpMatrixStar = new float[16];
    private float[] viewProjectionMatrix = new float[16];
    private float distance = -3;
    private float targetDistance = 0;
    private WordManager wordManager;
    private String theText;
    private StarObject starObject;
    public GLRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        wordManager = new WordManager(context);
        starObject = new StarObject(context);
        transformText();
        transformStars();
        createTextObject(theText);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        setViewProjectionMatrix(width,height);
        transformStars();
        transformText();
    }



    private void setViewProjectionMatrix(int width, int height){
        final float[] projectionMatrix = new float[16];
        final float[] viewMatrix = new float[16];
        MatrixHelper.perspectiveM(projectionMatrix,110,(float)width/(float)height,1,100000);
        setLookAtM(viewMatrix,0,0,5,10,0,-1,-20,0,1,0);
        viewProjectionMatrix = MatrixHelper.multiplyMM(projectionMatrix,viewMatrix);
    }


    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0f, 0f, 0f, 1.0f);

        if(targetDistance<distance)
            distance-=0.01f;

        if(wordManager.textObject!=null){
            transformText();
            wordManager.textObject.draw(mvpMatrixText);
        }

        starObject.draw(mvpMatrixText);
    }

    private void transformText(){
        float[] modelMatrix = new float[16];
        MatrixHelper.identityM(modelMatrix);
        MatrixHelper.rotateM(modelMatrix,90 ,1,0,0);
        MatrixHelper.translateM(modelMatrix,-Constants.TEXT_WIDTH/2,0,distance);
        mvpMatrixText = MatrixHelper.multiplyMM(viewProjectionMatrix, modelMatrix);

    }
    private void transformStars(){
        float[] modelMatrix = new float[16];
        MatrixHelper.identityM(modelMatrix);
        MatrixHelper.translateM(modelMatrix,-Constants.TEXT_WIDTH/2,0,50);
        mvpMatrixStar = MatrixHelper.multiplyMM(viewProjectionMatrix,modelMatrix);
    }

    public void createTextObject(String text){
        if(text.equals(""))
            return;
        theText = text;
        if(wordManager==null){
            theText = text;
            return;
        }
        wordManager.createTextObject(text);
        int TEXT_OFFSET = -3;
        targetDistance = -(wordManager.textObject.getTextHeight() + TEXT_OFFSET);
    }

    public void setText(String text){
        theText = text;
    }
    public void scrollDistance(float dy){
        distance += dy*Constants.SCALE;
        targetDistance = distance;
    }
    public void setDistance(float distance){
        this.distance = distance;
    }
    public float getTargetDistance(){
        return targetDistance;
    }
}

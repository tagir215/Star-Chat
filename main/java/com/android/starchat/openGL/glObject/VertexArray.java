package com.android.starchat.openGL.glObject;


import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

import android.content.Context;

import com.android.starchat.util.FileHelper;
import com.android.starchat.openGL.glUtil.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class VertexArray {
    FloatBuffer floatBuffer;
    ShortBuffer shortBuffer;
    int programId;
    int vertexShaderId;
    int fragmentShaderId;

    public void createVertexBuffer(float[] vertices){
        floatBuffer = ByteBuffer
                .allocateDirect(vertices.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertices);
    }

    public void createIndexBuffer(short[] vertices){
        shortBuffer = ByteBuffer
                .allocateDirect(vertices.length*2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(vertices);
    }

    public void resetBuffer(){
        floatBuffer.position(0);
        shortBuffer.position(0);
    }

    public void setMatrix(float[] matrix){
        int mVPMatrixLocationHandle = glGetUniformLocation(programId,"mVPMatrix");
        glUniformMatrix4fv(mVPMatrixLocationHandle,1,false,matrix,0);
    }

    public void setPositionAttribute(int dimensions,int stride){
        int aPositionLocationHandle = glGetAttribLocation(programId, "vPosition");
        glEnableVertexAttribArray(aPositionLocationHandle);
        glVertexAttribPointer(aPositionLocationHandle,dimensions,GL_FLOAT,false,stride,floatBuffer);
    }

    public void setTextureAttribute(int offset,int dimensions,int textureId, int stride){
        floatBuffer.position(offset);
        int uTextureUnitLocationHandle = glGetUniformLocation(programId,"uTextureUnit");
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,textureId);
        glUniform1i(uTextureUnitLocationHandle,0);
        int aTextureCoordinatesLocationHandle = glGetAttribLocation(programId,"aTextureCoordinates");
        glVertexAttribPointer(aTextureCoordinatesLocationHandle,dimensions,GL_FLOAT,false,stride,floatBuffer);
        glEnableVertexAttribArray(aTextureCoordinatesLocationHandle);
    }

    public void setColorAttribute(int offset, int dimensions, int stride){
        floatBuffer.position(offset);
        int aColorLocationHandle = glGetAttribLocation(programId,"aColor");
        glEnableVertexAttribArray(aColorLocationHandle);
        glVertexAttribPointer(aColorLocationHandle,dimensions,GL_FLOAT,false,stride,floatBuffer);
    }

    public void buildProgram(Context context, int vertexShaderResourceId, int fragmentShaderRecourseId){
        String vertexShaderCode = FileHelper.fileToString(context.getResources().openRawResource(vertexShaderResourceId));
        String fragmentShaderCode = FileHelper.fileToString(context.getResources().openRawResource(fragmentShaderRecourseId));
        int[] program = ShaderHelper.buildProgram(vertexShaderCode,fragmentShaderCode);
        programId = program[0];
        vertexShaderId = program[1];
        fragmentShaderId = program[2];
        glUseProgram(programId);
    }


}

package com.android.starchat.openGL.glUtil;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glDetachShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

import android.util.Log;



public class ShaderHelper {

    public static int[] buildProgram(String vertexShaderCode, String fragmentShaderCode){
        int vertexShader = compileShader(GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = compileShader(GL_FRAGMENT_SHADER,fragmentShaderCode);
        int program = linkProgram(vertexShader,fragmentShader);
        validate(program);
        return new int[] {program,vertexShader,fragmentShader};
    }

    private static int compileShader(int type, String shaderCode){
        final int shaderObjectId = glCreateShader(type);
        checkStatus(shaderObjectId,"createShader");

        glShaderSource(shaderObjectId,shaderCode);
        glCompileShader(shaderObjectId);
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);
        checkStatus(compileStatus[0],"glshadersource: "+shaderCode);

        return shaderObjectId;
    }


    private static int linkProgram(int vertexShader, int fragmentShader){
        final int programId = glCreateProgram();
        checkStatus(programId,"createprogram");
        glAttachShader(programId,vertexShader);
        glAttachShader(programId,fragmentShader);
        glLinkProgram(programId);

        final int[] status = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, status, 0);
        checkStatus(status[0],"linking");

        return programId;
    }

    public static void deleteProgram(int program, int vertexShader, int fragmentShader){
        glDetachShader(program,vertexShader);
        glDetachShader(program,fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteProgram(program);
    }



    private static boolean validate(int programId){
        glValidateProgram(programId);
        int[] status = new int[1];
        glGetProgramiv(programId,GL_VALIDATE_STATUS,status,0);
        checkStatus(status[0],"validateprogram");
        return status[0] != 0;
    }

    private static void checkStatus(int status, String type){
        if(status==0){
            Log.e("ShaderHelper","this may be the problem sir "+type);
        }
    }
}

package com.android.starchat.openGL.glObject;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDrawElements;
import android.content.Context;

import com.android.starchat.R;
import com.android.starchat.openGL.glUtil.ShaderHelper;

import java.util.ArrayList;

public class TextObject {
    private final VertexArray vertexArray;
    private final Context context;
    private final int COORDS_PER_VERTEX = 3;
    private final int STRIDE = 20;
    private final float H = 2;
    private final float[] textHeightHandle = new float[1];
    private int textureId;
    private final short[] drawOrder;

    public TextObject(Context context, ArrayList<Quad> quadList,int textureId){
        this.context = context;
        float[] vertices=  TextVerticesGenerator.createVertices(quadList,STRIDE,textHeightHandle);
        drawOrder = TextVerticesGenerator.createDrawOrderIndexes(quadList.size());
        vertexArray = new VertexArray();
        vertexArray.createVertexBuffer(vertices);
        vertexArray.createIndexBuffer(drawOrder);
        this.textureId = textureId;
    }

    public void draw(float[] mVP_matrix){
        vertexArray.buildProgram(context, R.raw.vertex_shader_text,R.raw.fragment_shader_text);
        vertexArray.resetBuffer();
        vertexArray.setMatrix(mVP_matrix);
        vertexArray.setPositionAttribute(3,STRIDE);
        vertexArray.setTextureAttribute(3,2,textureId,STRIDE);
        glDrawElements(GL_TRIANGLES, drawOrder.length,GL_UNSIGNED_SHORT, vertexArray.shortBuffer);
        ShaderHelper.deleteProgram(vertexArray.programId, vertexArray.vertexShaderId, vertexArray.fragmentShaderId);
    }

    public float getTextHeight(){
        return textHeightHandle[0];
    }
}

package com.android.starchat.openGL.glObject;

import static android.opengl.GLES10.glDrawElements;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;

import android.content.Context;

import com.android.starchat.R;

import java.util.List;

public class StarObject {
    private final VertexArray vertexArray;
    private final Context context;
    private final int STRIDE = 24;
    private final short[] indexes;
    private final float[] vertices;
    public StarObject(Context context){
        this.context = context;
        List<double[]>coordinates = StarVerticesGenerator.createStarCoordinates();
        vertices = StarVerticesGenerator.createVertices(coordinates,STRIDE);
        indexes = StarVerticesGenerator.createDrawOrderIndexes(coordinates);
        vertexArray = new VertexArray();
        vertexArray.createVertexBuffer(vertices);
        vertexArray.createIndexBuffer(indexes);

    }

    public void draw(float[] mVPMatrix){
        vertexArray.resetBuffer();
        vertexArray.buildProgram(context, R.raw.vertex_shader_star,R.raw.fragment_shader_star);
        vertexArray.setMatrix(mVPMatrix);
        vertexArray.setPositionAttribute(3,STRIDE);
        vertexArray.setColorAttribute(3,3,STRIDE);
        glDrawElements(GL_TRIANGLES,indexes.length,GL_UNSIGNED_SHORT,vertexArray.shortBuffer);
    }
}

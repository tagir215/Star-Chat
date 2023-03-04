package com.android.starchat.openGL.glObject;

import java.util.ArrayList;

public class TextVerticesGenerator {

    private static final float TEXTURE_WIDTH = 5.12f;
    private static final float TEXTURE_HEIGHT = 5.12f;

    public static float[] createVertices(ArrayList<Quad> quadList,int STRIDE,float[] textHeightHandle){
        float[] v = new float[quadList.size()*STRIDE];
        int nxt = 0;

        for (int i=0; i<quadList.size(); i++){
            Quad q = quadList.get(i);
            float x = q.x;
            float y = q.y;
            float w = q.width * q.scale;
            float h = q.height * q.scale;
            v[nxt]=x;      v[nxt+1]=y+h;  v[nxt+2] =0;   v[nxt+3]= pxX(q,0);   v[nxt+4]= pxY(q,h);
            v[nxt+5]=x;    v[nxt+6]=y;    v[nxt+7] =0;   v[nxt+8]= pxX(q,0);   v[nxt+9]= pxY(q,0);
            v[nxt+10]=x+w; v[nxt+11]=y;   v[nxt+12]=0;   v[nxt+13]= pxX(q,w);     v[nxt+14]= pxY(q,0);
            v[nxt+15]=x+w; v[nxt+16]=y+h; v[nxt+17]=0;   v[nxt+18]= pxX(q,w);     v[nxt+19]= pxY(q,h);
            nxt += STRIDE;

            if(y+h > textHeightHandle[0])
                textHeightHandle[0] = y+h;
        }
        return v;
    }


    public static float pxX(Quad quad,float w){
        return w/ TEXTURE_WIDTH/quad.scale + ( quad.characterInfo.getX() / 512);
    }
    public static float pxY(Quad quad, float h){
        return h/ TEXTURE_HEIGHT/quad.scale + ( quad.characterInfo.getY() / 512 );
    }

    public static short[] createDrawOrderIndexes(int size){
        short[] indexes = new short[size*6];
        int next = 0;
        for (int i=0; i<indexes.length; i+=6){
            indexes[i] =  (short)next;
            indexes[i+1]= (short)(next+1);
            indexes[i+2]= (short)(next+2);
            indexes[i+3]= (short)(next);
            indexes[i+4]= (short)(next+2);
            indexes[i+5]= (short)(next+3);
            next+=4;
        }
        return indexes;
    }
}

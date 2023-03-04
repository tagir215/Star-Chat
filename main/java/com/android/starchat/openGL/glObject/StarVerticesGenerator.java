package com.android.starchat.openGL.glObject;

import com.android.starchat.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarVerticesGenerator {
    public static List<double[]> createStarCoordinates(){
        Random random = new Random();
        List<double[]> coordinates = new ArrayList<>();
        final int starArea = 10000;
        final int starCount = 1000;
        for(int i=0; i<starCount; i++){
            coordinates.add(new double[]{
                    random.nextDouble()*starArea/2-starArea/4f+ Constants.TEXT_WIDTH/2,
                    random.nextDouble()*-starArea/2,
                    random.nextDouble()*starArea/2-starArea/4f,
            });
        }
        return coordinates;
    }

    public static float[] createVertices(List<double[]>coordinates,int STRIDE){
        float[] v = new float[coordinates.size()*STRIDE];
        int next = 0;
        for(int i=0; i<v.length; i+=STRIDE){
            double[] c = coordinates.get(next);
            next++;
            float x = (float)c[0];
            float y = (float)c[1];
            float z = (float)c[2];
            float s = 7f;
            float brightness = 0.3f;
            Random rColor = new Random();
            float r =(1-rColor.nextFloat()/4) * brightness;
            float g =(1-rColor.nextFloat()/4) * brightness;
            float b =(1-rColor.nextFloat()/4) * brightness;


            v[i  ]=x;   v[1+i]=y;    v[2+i]=z+s;   v[3+i]=r;  v[4+i]=g;  v[5+i]=b;
            v[6+i]=x;   v[7+i]=y;    v[8+i]=z;     v[9+i]=r;  v[10+i]=g; v[11+i]=b;
            v[12+i]=x+s; v[13+i]=y;  v[14+i]=z;    v[15+i]=r; v[16+i]=g; v[17+i]=b;
            v[18+i]=x+s; v[19+i]=y;  v[20+i]=z+s;  v[21+i]=r; v[22+i]=g; v[23+i]=b;
        }
        return v;
    }

    public static short[] createDrawOrderIndexes(List<double[]>coordinates){
        short[] indexes = new short[coordinates.size()*6];
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

package com.android.starchat.openGL.glUtil;

public class MatrixHelper {
    public static void identityM(float[] m){
        m[0] = 1;
        m[1] = 0;
        m[2] = 0;
        m[3] = 0;

        m[4] = 0;
        m[5] = 1;
        m[6] = 0;
        m[7] = 0;

        m[8] = 0;
        m[9] = 0;
        m[10] = 1;
        m[11] = 0;

        m[12] = 0;
        m[13] = 0;
        m[14] = 0;
        m[15] = 1;

    }

    public static void translateM(float[]m, float x, float y, float z){
        m[12] = x;
        m[13] = y;
        m[14] = z;
    }

    public static void scaleM(float[]m, float x, float y, float z){
        m[0] = x;
        m[5] = y;
        m[10] = z;
    }

    public static void rotateM(float[]m, float angle, int x, int y, int z){
        angle = angle * ((float)Math.PI /180);
        if(x==1) {
            m[5] = (float) Math.cos(angle);
            m[6] = (float) Math.sin(angle);
            m[9] = -(float)Math.sin(angle);
            m[10] = (float)Math.cos(angle);
        }
        if(y==1){
            m[0] = (float)Math.cos(angle);
            m[2] = -(float)Math.sin(angle);
            m[8] = (float)Math.sin(angle);
            m[10] = (float)Math.cos(angle);
        }
        if(z==1){
            m[0] = (float)Math.cos(angle);
            m[1] = (float)Math.sin(angle);
            m[4] = -(float)Math.sin(angle);
            m[5] = (float)Math.cos(angle);
        }
    }


    public static void perspectiveM(float[] m, float angle, float aspect, float near, float far) {
        angle = (float) (angle * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angle / 2.0));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;
        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;
        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((far + near) / (far - near));
        m[11] = -1f;
        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * far * near) / (far - near));
        m[15] = 0f;

    }




    public static float[] multiplyMM(float[] m1, float[] m2){
        float[] result = new float[16];
        float[] columns = orderColumns(m1);
        int next = 0;
        for(int i=0; i<15; i+=4){
            multiplyBlock(result,m2,get4(columns,i),next);
            next ++;
        }

        return result;
    }
    private static float[] orderColumns(float[] m1){
        float[] columns = new float[16];
        int d=0;
        for (int i=0; i<16; i++){
            columns[i] = m1[d];
            d+=4;
            if(d>=16) {
                d -= 15;
            }
        }
        return columns;
    }
    private static float[] get4(float[] m, int d){
        return new float[] {
                m[d],m[d+1],m[d+2],m[d+3]
        };
    }
    private static void multiplyBlock(float[] result, float[] m,float[] column, int d){
        int next = d;
        for (int i=0; i<15; i+=4){
            result[next] = row_x_column_sum(get4(m,i),column);
            next+=4;
        }
    }
    private static float row_x_column_sum(float[] row, float[] column){
        float sum = 0;
        for (int i=0; i<row.length; i++){
            sum += row[i] * column[i];
        }
        return sum;
    }


}

package com.android.starchat.util;

import org.junit.Test;

import com.android.starchat.openGL.glUtil.MatrixHelper;


public class TestMatrix {
    @Test
    public void testMultiplyMM() {
        float[] m1 = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        float[] m2 = {15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0};
        float[] result = MatrixHelper.multiplyMM(m1,m2);
        for (int i=0; i<16; i++){
            System.out.println(String.valueOf(i)+" "+result[i]);
        }
    }

}
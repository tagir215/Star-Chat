package com.android.starchat.openGL.glObject;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.starchat.openGL.glText.CharacterInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class TextVerticesGeneratorTest {

    ArrayList<Quad>quadList = new ArrayList<>();

    private void init(){
        CharacterInfo c = new CharacterInfo('c',0,2,10,5,0,0,0,0,0);
        CharacterInfo a = new CharacterInfo('a',5,6,5,4,0,0,0,0,0);
        CharacterInfo r = new CharacterInfo('r',0,0,20,4,0,0,0,0,0);
        quadList.add(new Quad(0,0,c));
        quadList.add(new Quad(2,2,a));
        quadList.add(new Quad(4,4,r));
    }

    @Test
    public void createVertices() {
        init();
        float[] vertices = TextVerticesGenerator.createVertices(quadList,20,new float[1]);
        assertEquals(0,vertices[0],0.05);
        assertEquals(0.05,vertices[1],0.05);
        assertEquals(0.009,vertices[4],0.05);
    }

    @Test
    public void pxX() {
        init();
        float px =TextVerticesGenerator.pxX(quadList.get(0),0);
        float py =TextVerticesGenerator.pxX(quadList.get(0),1);
        assertEquals(0,px,0.1);
        assertEquals(0.195,py,0.1);
    }

    @Test
    public void pxY() {
        init();
        float px =TextVerticesGenerator.pxY(quadList.get(0),0);
        float py =TextVerticesGenerator.pxY(quadList.get(0),1);
        assertEquals(0,px,0.1);
        assertEquals(0.195,py,0.1);
    }

    @Test
    public void createDrawOrderIndexes() {
        int size = 100;
        short[] indexes = TextVerticesGenerator.createDrawOrderIndexes(size);
        assertEquals(0,indexes[0]);
        assertEquals(1,indexes[1]);
        assertEquals(2,indexes[2]);
        assertEquals(0,indexes[3]);
        assertEquals(2,indexes[4]);
        assertEquals(3,indexes[5]);
        assertEquals(4,indexes[6]);
        assertEquals(5,indexes[7]);
        assertEquals(6,indexes[8]);
        assertEquals(4,indexes[9]);
        assertEquals(6,indexes[10]);
        assertEquals(7,indexes[11]);
    }
}
package com.android.starchat.openGL.glObject;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StarVerticesGeneratorTest {

    @Test
    public void createStarCoordinates() {
        List<double[]> coordinates = StarVerticesGenerator.createStarCoordinates();

        assertEquals(10000,coordinates.size());
    }

    @Test
    public void createVertices() {
        List<double[]>coordinates = new ArrayList<>();
        coordinates.add(new double[]{1,1,0,1});
        coordinates.add(new double[]{10,10,0,1});
        coordinates.add(new double[]{5,5,0,1});
        int stride = 24;
        float[] vertices = StarVerticesGenerator.createVertices(coordinates,stride);
        assertEquals(1,vertices[0],0.1);
        assertEquals(1,vertices[1],0.1);
        assertEquals(5,vertices[2],0.1);
        assertEquals(10,vertices[stride],0.1);
    }


    @Test
    public void createDrawOrderIndexes() {
        List<double[]>coordinates = StarVerticesGenerator.createStarCoordinates();
        short[] indexes =  StarVerticesGenerator.createDrawOrderIndexes(coordinates);
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
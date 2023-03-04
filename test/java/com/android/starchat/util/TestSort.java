package com.android.starchat.util;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.android.starchat.ui.uiStart.Country;

import org.junit.Test;

public class TestSort {

    Country[] countries = new Country[]{
            new Country("e","34","zealand"),
            new Country("f","34","zealand"),
            new Country("a","34","zealand"),
            new Country("h","34","zealand"),
            new Country("c","34","zealand"),
            new Country("d","34","zealand"),
            new Country("g","34","zealand"),
            new Country("k","34","zealand"),
            new Country("b","34","zealand"),
            new Country("j","34","zealand"),
            new Country("m","34","zealand"),
            new Country("l","34","zealand"),
            new Country("i","34","zealand"),
            new Country("n","34","zealand"),
    };
    String[] expected = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n"};

    @Test
    public void testNonRecursive(){

        Country[] result = Sort.mergeSortNonRecursive(countries);
        String[] resultString = new String[result.length];
        for(int i =0; i< result.length; i++){
            System.out.println(result[i].getName());
            resultString[i] = result[i].getName();
        }
        assertArrayEquals(expected,resultString);
    }
    @Test
    public void testRecursive(){
        Country[] result = Sort.mergeSortRecursive(countries);
        String[] resultString = new String[result.length];
        for(int i =0; i< result.length; i++){
            System.out.println(result[i].getName());
            resultString[i] = result[i].getName();
        }
        assertArrayEquals(expected,resultString);
    }


    @Test
    public void testMerge(){
        Country[] left = new Country[]{
                new Country("a","ee","ee"),
                new Country("c","ee","ee"),
                new Country("e","ee","ee"),
                new Country("g","ee","ee"),
                new Country("j","ee","ee"),
                new Country("m","ee","ee"),
                new Country("p","ee","ee"),
                new Country("z","ee","ee"),
        };
        Country[] right = new Country[]{
                new Country("b","ee","ee"),
                new Country("d","ee","ee"),
                new Country("f","ee","ee"),
        };
        Country[] c =Sort.merge(left,right);
        for (int i=0; i<c.length; i++){
            System.out.println(c[i].getName());
        }

    }
    @Test
    public void testCompare(){
        Country countrya = new Country("b","ee","rr");
        Country countryb = new Country("d","ee","rr");
        assertEquals(true,Sort.isA_beforeB(countrya,countryb));

        countrya = new Country("aa","ee","rr");
        countryb = new Country("ab","ee","rr");
        assertEquals(true,Sort.isA_beforeB(countrya,countryb));

        countrya = new Country("aaaaa","ee","rr");
        countryb = new Country("abc","ee","rr");
        assertEquals(true,Sort.isA_beforeB(countrya,countryb));

        countrya = new Country("aaaaa","ee","rr");
        countryb = new Country("aaaaaa","ee","rr");
        assertEquals(true,Sort.isA_beforeB(countrya,countryb));

        countrya = new Country("aaaaab","ee","rr");
        countryb = new Country("aaaaaf","ee","rr");
        assertEquals(true,Sort.isA_beforeB(countrya,countryb));

        countrya = new Country("aaa","ee","rr");
        countryb = new Country("aaa","ee","rr");
        assertEquals(true,Sort.isA_beforeB(countrya,countryb));

    }
}

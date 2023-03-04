package com.android.starchat.util;

import android.content.Context;

import com.android.starchat.openGL.glText.CharacterInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CharacterInfoHelper {

    public static HashMap<Character, CharacterInfo> createCharInfos(Context context){
        HashMap<Character, CharacterInfo>letterHashMap = new HashMap<>();
        try {
            InputStream inputStream = context.getResources().getAssets().open("star_wars_font.fnt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line=reader.readLine())!=null){
                CharacterInfo characterInfo = create1Letter(line);
                if(characterInfo !=null){
                    letterHashMap.put((char) characterInfo.getId(), characterInfo);

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return letterHashMap;
    }
    private static CharacterInfo create1Letter(String line){
        String[] words = line.split("\\s+");
        if(!words[0].equals("char"))
            return null;

        int[] n = new int[words.length-1];
        for (int i=1; i<words.length; i++){
            n[i-1] =Integer.parseInt(  words[i].replaceAll("[^\\d-]","")  );
        }

        return new CharacterInfo(n[0],n[1],n[2],n[3],n[4],n[5],n[6],n[7],n[8],n[9]);
    }
}

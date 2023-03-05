package com.android.starchat.openGL.glText;

import static com.android.starchat.util.Constants.TEXT_WIDTH;

import android.content.Context;

import com.android.starchat.openGL.glObject.Quad;
import com.android.starchat.openGL.glObject.TextObject;
import com.android.starchat.openGL.glUtil.Texture;
import com.android.starchat.util.CharacterInfoHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class WordManager {
    private final Context context;
    public TextObject textObject;
    private final int textureId;
    private final HashMap<Character,CharacterInfo>infoHashMap;
    private final ArrayList<Word>wordList = new ArrayList<>();
    private float gColumn;
    private float gRow;
    public WordManager(Context context){
        this.context =context;
        textureId = Texture.generateTexture(context);
        infoHashMap = CharacterInfoHelper.createCharInfos(context);
    }

    public void createTextObject(String text){
        wordList.clear();
        gColumn = 0;
        gRow = 0;
        createWordList(text);
        textObject = new TextObject(context, wordsToQuadList(wordList),textureId);
    }




    public void createWordList(String text){
        String[] words = text.split("(?<= )|(?<=\n)");
        for (int i=0; i<words.length; i++){
            String wordText = words[i];


            Word word;
            if(wordText.contains("t:")){
                gRow+=3;
                String s1 = wordText.replaceAll("-"," ");
                String s2 = s1.replaceAll("t:","");
                if(s2.length()>15)
                    s2 = s2.substring(0,14)+"...";
                word = new Word(textToCharacterInfos(s2.toUpperCase(Locale.ROOT)),true);
                setQuads(word);
                gRow+=2;
                gColumn = 0;
            }else{
                word = new Word(textToCharacterInfos(wordText),false);
                setQuads(word);
            }

            if(i>0){
                word.leftWord=wordList.get(i-1);
                wordList.get(i-1).nextWord = word;
            }
            wordList.add(word);

        }
        Alignment.alignToSides(wordList.get(wordList.size()-1));
    }
    public void setQuads(Word word){
        if(gColumn+ word.wordWidth> TEXT_WIDTH && word.wordWidth< TEXT_WIDTH){
            gRow++; gColumn=0;
        }
        float[] rowAndColumn = word.createQuads(gColumn,gRow);
        gColumn = rowAndColumn[0];
        gRow = rowAndColumn[1];
    }
    public ArrayList<CharacterInfo> textToCharacterInfos(String text){
        ArrayList<CharacterInfo> characterInfos = new ArrayList<>();
        for (int i=0; i<text.length(); i++){
            CharacterInfo ci = infoHashMap.get(text.charAt(i));
            if(ci!=null)
                characterInfos.add(ci);
            else
                characterInfos.add(infoHashMap.get('#'));
        }
        return characterInfos;
    }



    public ArrayList<Quad>wordsToQuadList(ArrayList<Word>words){
        ArrayList<Quad>quads = new ArrayList<>();
        for (int i=0; i<words.size(); i++){
            quads.addAll(words.get(i).quadList);
        }
        return quads;
    }


}

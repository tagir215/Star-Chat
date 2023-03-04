package com.android.starchat.openGL.glText;

import com.android.starchat.util.Constants;

public class Alignment {
    public static void alignToSides(Word word) {
        Word currentWord = word;
        float lineWidth = 0;

        while (true) {
            lineWidth += currentWord.wordWidth;

            if(currentWord.leftWord==null){
                alignTitle(currentWord);
                break;
            }


            if (currentWord.leftWord.row != currentWord.row) {
                if(!currentWord.title)
                    alignLine(currentWord,lineWidth);
                else
                    alignTitle(currentWord);
                lineWidth = 0;
            }

            currentWord = currentWord.leftWord;
        }

    }
    private static void alignLine(Word word,float lineWidth){


        final float minLineWidth = Constants.TEXT_WIDTH/2;
        if(lineWidth<minLineWidth || lineWidth>Constants.TEXT_WIDTH)
            return;

        final float spacingMultiplier = Constants.TEXT_WIDTH / lineWidth;
        word.align(0,spacingMultiplier);
    }

    private static void alignTitle(Word word){
        final float spacingMultiplier = 1;
        final float left = Constants.TEXT_WIDTH/2 - word.wordWidth/2;
        word.align(left,spacingMultiplier);
    }
}

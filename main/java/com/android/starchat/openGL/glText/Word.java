package com.android.starchat.openGL.glText;

import com.android.starchat.util.Constants;
import com.android.starchat.openGL.glObject.Quad;

import java.util.ArrayList;

public class Word {
    ArrayList<Quad>quadList;
    Word leftWord;
    Word nextWord;
    boolean title;

    ArrayList<CharacterInfo> characterInfos;
    float wordWidth;
    float column;
    float row;

    public Word(ArrayList<CharacterInfo>characterInfos,boolean title){
        this.characterInfos = characterInfos;
        this.title = title;
        calculateWidth();
    }

    private void calculateWidth(){
        for (int i=0; i<characterInfos.size(); i++){
            wordWidth += characterInfos.get(i).getXadvance();
        }
        if(title)
            wordWidth*=Constants.TITLE_SCALE;
    }

    public float[] createQuads(float column, float row){
        this.column = column * Constants.SCALE;
        this.row = row * Constants.SCALE;
        quadList = new ArrayList<>();

        for(int i = 0; i< characterInfos.size(); i++){
            CharacterInfo c = characterInfos.get(i);
            float offset = c.getXoffset();
            quadList.add(new Quad(column +offset,row,c));
            column+=c.getXadvance();

            if(wordWidth>Constants.TEXT_WIDTH && column>Constants.TEXT_WIDTH){
                column = 0;
                row++;
            }
            if((char)c.getId()=='\n'){
                column = 0;
                row+=1;
            }

        }
        return new float[] {column,row};

    }

    void align(float left, float spacingMultiplier){
        if(leftWord!=null && leftWord.row == row){
            column = leftWord.column;
        }
        wordWidth = 0;

        if(left>0){
            column = left;
        }
        float scale = 1;
        if(title){
            scale*=Constants.TITLE_SCALE;
        }

        for (int i=0; i<quadList.size(); i++){
            Quad quad = quadList.get(i);
            CharacterInfo c = characterInfos.get(i);
            float offset = c.getXoffset()*scale*spacingMultiplier;
            quad.setX(column+offset);
            column += quad.getCharacterInfo().getXadvance()*scale * spacingMultiplier;
            wordWidth+=column;

            if(title){
                quad.setScale(Constants.TITLE_SCALE);
                CharacterInfo info = quad.getCharacterInfo();
                quad.setY(quad.getY()-info.getYoffset() + info.getYoffset()*Constants.TITLE_SCALE);

            }
        }

        if(nextWord!=null && nextWord.row == row){
            nextWord.align(0,spacingMultiplier);
        }
    }

}

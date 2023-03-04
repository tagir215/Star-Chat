package com.android.starchat.openGL.glObject;

import com.android.starchat.openGL.glText.CharacterInfo;

public class Quad {
    float x;
    float y;
    float width;
    float height;
    float scale = 1;
    CharacterInfo characterInfo;

    public Quad(float x, float y, CharacterInfo c) {
        this.x = x + (float) c.getXoffset();
        this.y = y + (float) c.getYoffset();
        this.characterInfo = c;
        width = c.getWidth();
        height = c.getHeight();
    }

    public float getX() {
        return x;
    }
    public CharacterInfo getCharacterInfo() {
        return characterInfo;
    }
    public void setX(float x) {
        this.x = x;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width*scale;
    }

    public float getHeight() {
        return height*scale;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setY(float y) {
        this.y = y;
    }
}

package com.android.starchat.openGL.glText;

import com.android.starchat.util.Constants;

public class CharacterInfo {
    private final int id;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final float xoffset;
    private final float yoffset;
    private final float xadvance;
    private final int page;
    private final int chnl;



    public CharacterInfo(int id, float x, float y, float width, float height, float xoffset, float yoffset, float xadvance, int page, int chnl) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
        this.xadvance = xadvance;
        this.page = page;
        this.chnl = chnl;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width * Constants.SCALE;
    }

    public float getHeight() {
        return height * Constants.SCALE;
    }

    public float getXoffset() {
        return xoffset * Constants.SCALE;
    }

    public float getYoffset() {
        return yoffset * Constants.SCALE;
    }

    public float getXadvance() {
        return xadvance * Constants.SCALE;
    }

    public int getPage() {
        return page;
    }

    public int getChnl() {
        return chnl;
    }


}

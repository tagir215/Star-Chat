package com.android.starchat.ui.uiStart;

public class Country {
    private final String name;
    private final String code;
    private final  String region;
    private Action action;

    public Country(String name, String code, String region) {
        this.name = name;
        this.code = code;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getRegion() {
        return region;
    }

    public void setAction(Action action){
        this.action = action;
    }
    public interface Action{
        void setCountry();
    }
    public Action getAction(){
        return action;
    }
}

package com.android.starchat.openGL.glUtil;

import com.android.starchat.openGL.glText.CharacterInfo;

import java.util.HashMap;

public class TextContent {
    private final HashMap<String, CharacterInfo>letterHashMap = new HashMap<>();
    public static String ascii =  "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "1234567890" +
            "\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*"+
            " ¡¢£¤¥¦§¨©ª«¬\u00AD®¯°±²³´µ¶·¸¹º»" +
            "¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙ" +
            "ÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷" +
            "øùúûüýþÿ";

    public static String hello = "Hello, my name is princes Leia, I want " +
            "to thank you for your hard work and patience. Hope I can help you " +
            "become a nice Jedi lord. See you bye bye.";


    public static String crawl = "It is a dark time for the Rebellion. Although the Death Star has been destroyed, Imperial troops have driven the Rebel forces from their hidden base and pursued them across the galaxy.\n" +
            "\n" +
            "Evading the dreaded Imperial Starfleet, a group of freedom fighters led by Luke Skywalker has established a new secret base on the remote ice world of Hoth.\n" +
            "\n" +
            "The evil lord Darth Vader, obsessed with finding young Skywalker, has dispatched thousands of remote probes into the far reaches of space";

}

package com.example.dobit.recall;

/**
 * Created by dobit on 3/11/2017.
 */

public class Voice {
    public enum Gender{
        Male, Female
    }

    public Voice(String lang){
        this.lang = lang;
        this.voiceName = "";
        this.gender = Gender.Female;
        this.isServiceVoice = true;
    }

    public Voice(String lang, String voiceName, Gender gender, Boolean isServiceVoice){
        this.lang = lang;
        this.voiceName = voiceName;
        this.gender = gender;
        this.isServiceVoice = isServiceVoice;
    }

    public final String lang;
    public final String voiceName;
    public final Gender gender;
    public final Boolean isServiceVoice;
}

package it.runningexamples.fiscalcode;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String THEME = "theme";
    public static final String FIRST_START = "start";
    public static final String TEMP_INTRO = "intro";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public PreferenceManager (Context context){
        this.context = context;
        pref = context.getSharedPreferences(SHARED_PREFS, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstStart(boolean isFirstStart) {
        editor.putBoolean(FIRST_START, isFirstStart);
        editor.apply();
    }

    public void setThemePref(int theme) {
        editor.putInt(THEME,theme);
        editor.apply();
    }

    public boolean isFirstStart() {
        return pref.getBoolean(FIRST_START, true);
    }

    public int getTheme() {
        return pref.getInt(THEME,0);
    }

    public void setTempIntro(boolean isTempIntro){
        editor.putBoolean(TEMP_INTRO,isTempIntro);
        editor.apply();
    }

    public boolean isTempIntro(){
        return pref.getBoolean(TEMP_INTRO,false);
    }

    public void setFirstActivity(String activity,Boolean isFirstMain){
        editor.putBoolean(activity,isFirstMain);
        editor.apply();
    }

    public boolean isFirstActivity(String activity){
        return pref.getBoolean(activity,true);
    }


}

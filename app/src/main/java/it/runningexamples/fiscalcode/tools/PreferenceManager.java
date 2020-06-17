/**
 * Classe ausiliaria utilizzata per la gestione delle SharedPreferences, in modo da non doverle
 * utilizzare in modo esplicito all'interno delle varie activity.
 */

package it.runningexamples.fiscalcode.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static final String SHARED_PREFS = "sharedPrefs"; //NON-NLS
    public static final String THEME = "theme"; //NON-NLS
    public static final String FIRST_START = "start"; //NON-NLS
    public static final String TEMP_INTRO = "intro"; //NON-NLS

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public PreferenceManager (Context context){
        this.context = context;
        pref = context.getSharedPreferences(SHARED_PREFS, PRIVATE_MODE);
        editor = pref.edit();
    }

    // Imposta un booleano per tenere traccia se l'applicazione è stata già avviata almeno una volta
    public void setFirstStart(boolean isFirstStart) {
        editor.putBoolean(FIRST_START, isFirstStart);
        editor.apply();
    }

    public boolean isFirstStart() {
        return pref.getBoolean(FIRST_START, true);
    }

    // Imposta all'interno delle shared preference un intero che identifica il tema scelto dall'utente
    public void setThemePref(int theme) {
        editor.putInt(THEME,theme);
        editor.apply();
    }

    public int getTheme() {
        return pref.getInt(THEME,0);
    }

    // Imposta un booleano utilizzato per gestire il lancio manuale della WelcomeActivity tramite
    // la schermata delle impostazioni
    public void setTempIntro(boolean isTempIntro){
        editor.putBoolean(TEMP_INTRO,isTempIntro);
        editor.apply();
    }

    public boolean isTempIntro(){
        return pref.getBoolean(TEMP_INTRO,false);
    }

    // Imposta un booleano per gestire il primo lancio di una activity specifica, in modo da
    // poter mostrare un tutorial per interagire con l'interfaccia utente
    public void setFirstActivity(String activity,Boolean isFirst){
        editor.putBoolean(activity,isFirst);
        editor.apply();
    }

    public boolean isFirstActivity(String activity){
        return pref.getBoolean(activity,true);
    }
}

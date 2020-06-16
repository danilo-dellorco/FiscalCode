package it.runningexamples.fiscalcode.tools;

import android.content.Context;
import it.runningexamples.fiscalcode.R;

public class ThemeUtilities {
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    public static void applyActivityTheme(Context context){
        PreferenceManager prefs = new PreferenceManager(context);
        int theme = prefs.getTheme();
        if (theme == THEME_LIGHT){
            context.setTheme(R.style.LightTheme);
        }
        if (theme == THEME_DARK){
            context.setTheme(R.style.DarkTheme);
        }
    }
}

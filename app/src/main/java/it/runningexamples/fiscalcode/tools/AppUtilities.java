package it.runningexamples.fiscalcode.tools;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.ui.MainActivity;
import it.runningexamples.fiscalcode.ui.SettingsActivity;

public class AppUtilities extends AppCompatActivity {
    private static final int THEME_LIGHT = 0;
    private static final int THEME_DARK = 1;
    private static final int PENDING_ID = 12345;

    // Metodo utilizzato per applicare il tema in base a quello impostato nelle Shared Preference
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

    public static void restartApp(Context context) {
        Intent mStartActivity = new Intent(context, MainActivity.class);
        mStartActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent.getActivity(context, PENDING_ID, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        System.exit(0);
    }
}

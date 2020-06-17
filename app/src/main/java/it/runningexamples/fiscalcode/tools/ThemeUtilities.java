/**
 * Classe ausiliaria utilizzata per la gestione del Tema. In questo modo non è necessario
 * all'interno delle varie activity utilizzare esplicitamente le shared preference ed i
 * metodi per settare i diversi colori.
 */

package it.runningexamples.fiscalcode.tools;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.ui.MainActivity;

public class ThemeUtilities extends AppCompatActivity {
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

    // Metodo utilizzato per ottenere il colore della ActionBar se non si è nello stato
    // di selezione multipla, in base al tema impostato
    public static int getActionColor(Context mContext){
        int colorId;
        PreferenceManager prefs = new PreferenceManager(mContext);
        if (prefs.getTheme() == 1){
            colorId = ContextCompat.getColor(mContext, R.color.colorActionBarDark);
        }else{
            colorId = ContextCompat.getColor(mContext, R.color.colorActionBarLight);
        }
        return colorId;
    }

    // Metodo utilizzato per ottenere il colore della ActionBar se nello stato
    // di selezione multipla, in base al tema impostato
    public static int getActionSelectedColor(Context mContext){
        int colorId;
        PreferenceManager prefs = new PreferenceManager(mContext);
        if (prefs.getTheme() == 1){
            colorId = ContextCompat.getColor(mContext, R.color.colorAccentDark);
        }else{
            colorId = ContextCompat.getColor(mContext, R.color.colorAccentLight);
        }
        return colorId;
    }

    // Metodo utilizzato per ottenere il colore della CardView nello stato di
    // selezione multipla, in base al tema impostato
    public static int getSelectionColor(Context mContext) {
        int colorId;
        PreferenceManager prefs = new PreferenceManager(mContext);

        if (prefs.getTheme() == 1) {
            colorId = ContextCompat.getColor(mContext, R.color.colorCardSelectedDark);
        } else {
            colorId = ContextCompat.getColor(mContext, R.color.colorCardSelectedLight);
        }
        return colorId;
    }

    // Metodo utilizzato per ottenere il colore della CardView se non si è nello stato di
    // selezione multipla, in base al tema impostato
    public static int getCardColor(Context mContext) {
        int colorId;
        PreferenceManager prefs = new PreferenceManager(mContext);
        if (prefs.getTheme() == 1) {
            colorId = ContextCompat.getColor(mContext, R.color.colorCardDark);
        } else {
            colorId = ContextCompat.getColor(mContext, R.color.colorCardLight);
        }
        return colorId;
    }

    // Metodo utilizzato per impostare il colore del testo nel bottone del DatePicker dopo
    // aver inserito correttamente una data. Il colore viene scelto in base al tema impostato
    public static void setDateTextColor(Context context, Button button) {
        PreferenceManager prefs = new PreferenceManager(context);
        if (prefs.getTheme() == 0) {
            button.setTextColor(ContextCompat.getColor(context,R.color.colorTextNormalLight));
        }
        else{
            button.setTextColor(ContextCompat.getColor(context,R.color.colorTextNormalDark));
        }
    }

    // Metodo utilizzato per impostare il colore del testo nel bottone del DatePicker a quello
    // di default dopo aver resettato manualmente il form per il calcolo.
    // Il colore viene scelto in base al tema impostato.
    public static void resetDateTextColor(Context context, Button button) {
        PreferenceManager prefs = new PreferenceManager(context);
        if (prefs.getTheme() == 0) {
            button.setTextColor(ContextCompat.getColor(context,R.color.colorHintLight));
        }
        else{
            button.setTextColor(ContextCompat.getColor(context,R.color.colorHintDark));
        }
    }

    // Metodo utilizzato per riavviare l'applicazione quando viene cambiato il tema.
    public static void restartApp(Context context) {
        Intent mStartActivity = new Intent(context, MainActivity.class);
        mStartActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent.getActivity(context, PENDING_ID, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        System.exit(0);
    }

}

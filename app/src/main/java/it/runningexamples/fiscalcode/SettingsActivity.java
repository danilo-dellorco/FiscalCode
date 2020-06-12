package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener{
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String THEME = "1";
    int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        int theme = sharedPreferences.getInt(THEME,0);
        if (theme == 0){
            setTheme(R.style.LightTheme);
        }
        else{
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch swDarkMode = findViewById(R.id.swDarkMode);
        if (theme == 0){
            swDarkMode.setChecked(false);
        }
        else{
            swDarkMode.setChecked(true);
        }
        swDarkMode.setOnCheckedChangeListener(this);


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isChecked){
            editor.putInt(THEME,1);
        }
        else {
            editor.putInt(THEME,0);
        }
        editor.apply();
        new AlertDialog.Builder(this)
                .setMessage("Per applicare il tema è necessario riavviare l'applicazione, vuoi farlo ora?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent mStartActivity = new Intent(SettingsActivity.this, MainActivity.class);
                        int mPendingIntentId = 123456;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        System.exit(0);}})
                .setNegativeButton(android.R.string.no, null).show();
    }
}

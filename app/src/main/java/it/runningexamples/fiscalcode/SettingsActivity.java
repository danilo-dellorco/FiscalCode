package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener,View.OnClickListener{
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String THEME = "1";
    private static final int THEME_LIGHT = 0;
    private static final int THEME_DARK = 1;
    PreferenceManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = new PreferenceManager(this);
        int theme = prefs.getTheme();
        Switch swDarkMode = null;

        if (theme == THEME_LIGHT){
            setTheme(R.style.LightTheme);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            swDarkMode = findViewById(R.id.swDarkMode);
            swDarkMode.setChecked(false);
        }
        if (theme == THEME_DARK){
            setTheme(R.style.DarkTheme);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            swDarkMode = findViewById(R.id.swDarkMode);
            swDarkMode.setChecked(true);
        }
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnIntro = findViewById(R.id.btnIntro);
        btnProfile.setOnClickListener(this);
        btnIntro.setOnClickListener(this);
        swDarkMode.setOnCheckedChangeListener(this);
        Toolbar toolbarSettings = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbarSettings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked){
            prefs.setThemePref(THEME_DARK);
        }
        else {
            prefs.setThemePref(THEME_LIGHT);
        }
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnIntro){
            prefs.setTempIntro(true);
            startActivity(new Intent(SettingsActivity.this, WelcomeActivity.class));
            finish();
        }
        if (v.getId() == R.id.btnProfile){
            startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
            finish();
        }
    }
}

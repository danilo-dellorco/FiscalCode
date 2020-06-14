package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ActivityManager;
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

import java.io.File;

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
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
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
        showDialogRestart("Per applicare il tema è necessario riavviare l'applicazione, vuoi farlo ora?", false);
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
        if (v.getId() == R.id.btnDelete){
            showDialogRestart("Per eliminare i dati è necessario riavviare l'applicazione, vuoi farlo ora?", true);
        }
    }

/*
    Presente anche un metodo introdotto in API 19 ActivityManager.clearApplicationUserData()
    ma va ad eliminare i dati ma killa anche le activity in run
 */

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

                }
            }
        }
        restartApp();
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public void showDialogRestart(String textToShow, final Boolean clearApp){
        new AlertDialog.Builder(this)
                .setMessage(textToShow)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (clearApp) {
                            clearApplicationData();
                        }else{
                            restartApp();
                        }
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    public void restartApp(){
        Intent mStartActivity = new Intent(SettingsActivity.this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        System.exit(0);
    }



}

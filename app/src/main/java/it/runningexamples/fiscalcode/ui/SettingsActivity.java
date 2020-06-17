package it.runningexamples.fiscalcode.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.google.android.material.snackbar.Snackbar;


import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.db.AppDatabase;
import it.runningexamples.fiscalcode.tools.ThemeUtilities;
import it.runningexamples.fiscalcode.tools.PreferenceManager;

public class SettingsActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener, View.OnClickListener {
    private static final int THEME_LIGHT = 0;
    private static final int THEME_DARK = 1;
    private static final String SETTINGS = "settings" ;
    public int lastTheme;
    public boolean lastChecked;
    PreferenceManager prefs;
    public Switch swDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = new PreferenceManager(this);
        int theme = prefs.getTheme();
        swDarkMode = null;

        if (theme == THEME_LIGHT) {
            setTheme(R.style.LightTheme);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            swDarkMode = findViewById(R.id.swDarkMode);
            swDarkMode.setChecked(false);
        }
        if (theme == THEME_DARK) {
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
        if (prefs.isFirstActivity(SETTINGS)){
            firstTutorial();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        lastTheme = prefs.getTheme();
        lastChecked = buttonView.isChecked();
        if (isChecked) {
            prefs.setThemePref(THEME_DARK);
            showDialogRestart(getString(R.string.changeThemeAlert), false);
        } else {
            prefs.setThemePref(THEME_LIGHT);
            showDialogRestart(getString(R.string.changeThemeAlert), false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnIntro) {
            prefs.setTempIntro(true);
            startActivity(new Intent(SettingsActivity.this, WelcomeActivity.class));
            finish();
        }
        if (v.getId() == R.id.btnProfile) {
            startActivity(new Intent(SettingsActivity.this, ProfileSettingsActivity.class));
        }
        if (v.getId() == R.id.btnDelete) {
            showDialogRestart(getString(R.string.deleteAlert), true);
        }
    }

    public void deleteDB() {
        AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().deleteAll();
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.dataEliminated, Snackbar.LENGTH_LONG).show();
    }

    public void showDialogRestart(String textToShow, final Boolean clearApp) {
        new AlertDialog.Builder(this)
                .setMessage(textToShow)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (clearApp) {
                            deleteDB();
                        } else {
                            ThemeUtilities.restartApp(SettingsActivity.this);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!clearApp) {
                            /* Quando viene scelto "Annulla" riporta lo switch nello stato precedente */
                            prefs.setThemePref(lastTheme);
                            swDarkMode.setOnCheckedChangeListener (null);
                            swDarkMode.setChecked (!lastChecked);
                            swDarkMode.setOnCheckedChangeListener (SettingsActivity.this);
                        }
                    }
                }).show();
    }

    private void firstTutorial(){
        BubbleShowCaseBuilder builder1 = new BubbleShowCaseBuilder(SettingsActivity.this);
        builder1.title(getString(R.string.bubbleSettingsDark));
        builder1.targetView(findViewById(R.id.swDarkMode));
        builder1.show();
        prefs.setFirstActivity(SETTINGS,false);
    }
}

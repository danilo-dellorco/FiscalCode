package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager prefs = new PreferenceManager(this);

        if (prefs.isFirstStart()){
            startActivity(new Intent(this, WelcomeActivity.class));
        }
        else{
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}

package it.runningexamples.fiscalcode.ui;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import it.runningexamples.fiscalcode.tools.PreferenceManager;

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

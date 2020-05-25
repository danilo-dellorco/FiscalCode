package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    CodiceFiscale prova = new CodiceFiscale("Pippo", "Lo Presti", 7,5,1900, 'F', "Tecchiena");
    prova.calculateNomeCF();
    }

}

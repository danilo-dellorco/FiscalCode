package it.runningexamples.fiscalcode;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CFDetail extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfdetail);
        tv = findViewById(R.id.tvDio);
        CodiceFiscaleEntity cf = (CodiceFiscaleEntity)getIntent().getExtras().getParcelable("CF");
        tv.setText(cf.getNome());

    }
}
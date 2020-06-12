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
        CodiceFiscale cf = getIntent().getExtras().getParcelable("CF");
        tv.setText(cf.getFinalFiscalCode());

    }
}
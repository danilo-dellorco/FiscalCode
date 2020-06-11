package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CodiceFiscale";
    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DarkTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvRisultato = findViewById(R.id.tvRisultato);
        holder = new Holder();

        CodiceFiscale prova = new CodiceFiscale("Gesu", "Lo Presti", 7,5,1900, 'F', "Tecchiena");
        String result = prova.calculateNomeCF();
        tvRisultato.setText(result);
    }


    private class Holder implements View.OnClickListener{
        Parser parser;
        List<Comune> comuniList;
        AutoCompleteTextView atComuni;
        Button btnCalcola;
        Comune comuneSelected;
        String comuneCode;
        String prov;


        AdapterView.OnItemClickListener onItemClickListener;

        public Holder(){
            btnCalcola = findViewById(R.id.btnCalcola);
            btnCalcola.setOnClickListener(this);
            parser = new Parser(MainActivity.this);
            comuniList = parser.parse();

            onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {      //TODO quando clicca
                    comuneSelected = (Comune) parent.getItemAtPosition(position);
                    Log.d(TAG, comuneSelected.getCode()+" "+comuneSelected.getName());
                }
            };
            setUpAutoCompleteTextView();
        }

        private void setUpAutoCompleteTextView() {
            atComuni = findViewById(R.id.atComuni);
            ArrayAdapter<Comune> dataAdapter = new ArrayAdapter<Comune>(MainActivity.this,
                    android.R.layout.simple_dropdown_item_1line, comuniList);
            atComuni.setAdapter(dataAdapter);
            atComuni.setOnItemClickListener(onItemClickListener);
        }



        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCalcola){
            }
        }
    }
}

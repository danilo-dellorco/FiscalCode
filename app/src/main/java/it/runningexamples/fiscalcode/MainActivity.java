package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

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


    private class Holder{
        Parser parser;
        List<Comune> comuniList;
        AutoCompleteTextView atComuni;

        public Holder(){
            parser = new Parser(MainActivity.this);
            comuniList = parser.parse();
            setUpAutoCompleteTextView();
        }

        private void setUpAutoCompleteTextView() {
            atComuni = findViewById(R.id.atComuni);
            ArrayAdapter<Comune> dataAdapter = new ArrayAdapter<Comune>(MainActivity.this,
                    android.R.layout.simple_dropdown_item_1line, comuniList);
            atComuni.setAdapter(dataAdapter);

        }

      }
}

package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CodiceFiscale";
    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DarkTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        holder = new Holder();

    }


    private class Holder implements View.OnClickListener {
        Parser parser;
        List<Comune> comuniList;
        AutoCompleteTextView atComuni;
        Button btnCalcola;
        Comune comuneSelected;

        TextView tvRisultato;
        TextView etBirthday;
        EditText etName;
        EditText etSurname;
        RadioGroup rgGender;

        AdapterView.OnItemClickListener onItemClickListener;

        public Holder() {
            tvRisultato = findViewById(R.id.tvRisultato);
            rgGender = findViewById(R.id.rgGender);
            etBirthday = findViewById(R.id.etData);
            etName = findViewById(R.id.etNome);
            etSurname = findViewById(R.id.etCognome);
            btnCalcola = findViewById(R.id.btnCalcola);
            btnCalcola.setOnClickListener(this);
            atComuni = findViewById(R.id.atComuni);
            atComuni.setDropDownBackgroundResource(R.color.colorSecondaryDark);
            parser = new Parser(MainActivity.this);
            comuniList = parser.parse();

            setUpDateDialog();
            setUpAutoCompleteTextView();
        }

        private void setUpDateDialog() {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            // set current Date
            etBirthday.setText(String.format("%02d/%02d/%d", day, month + 1, year));

            View.OnClickListener birthdayListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            };
            etBirthday.setOnClickListener(birthdayListener);
        }

        private void setUpAutoCompleteTextView() {
            ArrayAdapter<Comune> dataAdapter = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_dropdown_item_1line, comuniList);

            onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    comuneSelected = (Comune) parent.getItemAtPosition(position);
                    hideKeyboard();
                    Log.d(TAG, comuneSelected.getCode() + " " + comuneSelected.getName());
                }
            };
            atComuni.setAdapter(dataAdapter);
            atComuni.setOnItemClickListener(onItemClickListener);
        }

        private void showDatePickerDialog(View v) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCalcola) {
                hideKeyboard();
                String surname = etSurname.getText().toString();
                String name = etName.getText().toString();
                int radioID = rgGender.getCheckedRadioButtonId();

                String gender = (String) ((RadioButton) findViewById(radioID)).getText();
                // Get birthday
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date birthDay = new Date();
                try {
                    birthDay = simpleDateFormat.parse(etBirthday.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!name.equals("") & !surname.equals("") & comuneSelected != null) {
                    CodiceFiscale codiceFiscale = new CodiceFiscale(name, surname, birthDay, gender, comuneSelected);
                    String fiscalCode = codiceFiscale.calculateCF();

                    tvRisultato.setText(fiscalCode);
                }else{
                    Toast.makeText(getApplicationContext(), "Dati mancanti", Toast.LENGTH_LONG).show(); //todo toast specifico per non aver selezionato il comune
                }
            }
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}


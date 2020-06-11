package it.runningexamples.fiscalcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String THEME = "1";
    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        int theme = sharedPreferences.getInt(THEME,0);
        Toast.makeText(getApplicationContext(),Integer.toString(theme),Toast.LENGTH_SHORT).show();
        if (theme == 0){
            setTheme(R.style.LightTheme);
        }
        else{
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        holder = new Holder();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private class Holder implements View.OnClickListener {
        Parser parser;
        List<Comune> comuniList;
        AutoCompleteTextView atComuni;
        Button btnCalcola,btnChangeTheme;
        ImageButton btnCalendar;
        Comune comuneSelected;
        Toolbar toolbar;

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

            btnCalendar = findViewById(R.id.btn_calendar);
            btnCalendar.setOnClickListener(this);

            atComuni = findViewById(R.id.atComuni);
            parser = new Parser(MainActivity.this);
            comuniList = parser.parse();
            toolbar = findViewById(R.id.toolbar);
            btnChangeTheme = findViewById(R.id.btn_changeTheme);
            btnChangeTheme.setOnClickListener(this);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

            etBirthday.setOnClickListener(this);
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
                computeCF();
            }
            if (v.getId() == R.id.btn_changeTheme){
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(THEME,1);
                editor.apply();
                int theme = sharedPreferences.getInt(THEME,0);
                Toast.makeText(getApplicationContext(),Integer.toString(theme),Toast.LENGTH_SHORT).show();
            }

            if (v.getId() == R.id.etData || v.getId() == R.id.btn_calendar){
                showDatePickerDialog(v);
            }
        }

        private void computeCF() {
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
            } else {
                Toast.makeText(getApplicationContext(), "Dati mancanti", Toast.LENGTH_LONG).show(); //todo toast specifico per non aver selezionato il comune
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


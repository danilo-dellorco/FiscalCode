package it.runningexamples.fiscalcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//TODO menu in alto è nero anche nel tema light
//TODO riordinare main activity ed inserire edittext material
//TODO terminare datepicker
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CodiceFiscale";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String THEME = "1";
    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        int theme = sharedPreferences.getInt(THEME,0);
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


    private class Holder implements View.OnClickListener,Switch.OnCheckedChangeListener {
        Parser parser;
        List<Comune> comuniList;
        AutoCompleteTextView atComuni;
        Button btnChangeTheme;
        FloatingActionButton btnCalcola;
        Comune comuneSelected;
        Toolbar toolbar;
        Switch swEstero;

        TextView tvRisultato;
        Button btnBirthday;
        EditText etName;
        EditText etSurname;
        RadioGroup rgGender;
        ImageView ivBarCode;

        com.google.android.material.textfield.TextInputLayout autocompleteLayout;

        AdapterView.OnItemClickListener onItemClickListener;

        public Holder() {
            tvRisultato = findViewById(R.id.tvRisultato);
            rgGender = findViewById(R.id.rgGender);
            btnBirthday = findViewById(R.id.btnData);
            etName = findViewById(R.id.etNome);
            etSurname = findViewById(R.id.etCognome);
            btnCalcola = findViewById(R.id.btnCalcola);
            btnCalcola.setOnClickListener(this);

            atComuni = findViewById(R.id.atComuni);
            parser = new Parser(MainActivity.this);
            swEstero = findViewById(R.id.swEstero);
            swEstero.setOnCheckedChangeListener(this);
            comuniList = parser.parse();
            toolbar = findViewById(R.id.toolbar);
            autocompleteLayout = findViewById(R.id.autocompleteLayout);
            //btnChangeTheme = findViewById(R.id.btn_changeTheme);
            //btnChangeTheme.setOnClickListener(this);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            setUpDateDialog();
            setUpAutoCompleteTextView();
        }

        private void setUpDateDialog() {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            // set current Date
            btnBirthday.setText(String.format("%02d/%02d/%d", day, month + 1, year));

            View.OnClickListener birthdayListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            };
            btnBirthday.setOnClickListener(birthdayListener);
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
            if (v.getId() == R.id.btnData){
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
                birthDay = simpleDateFormat.parse(btnBirthday.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!name.equals("") & !surname.equals("") & comuneSelected != null) {
                CodiceFiscale codiceFiscale = new CodiceFiscale(name, surname, birthDay, gender, comuneSelected);
                String fiscalCode = codiceFiscale.calculateCF();

                tvRisultato.setText(fiscalCode);
            } else if(comuneSelected == null) {
                Toast.makeText(getApplicationContext(), "Selezionare un comune di nascita", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Completare tutti i campi", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            atComuni.getText().clear();
            if (swEstero.isChecked()){
                autocompleteLayout.setHint("Stato Estero di Nascita");
            }
            else{
                autocompleteLayout.setHint("Comune di Nascita");
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


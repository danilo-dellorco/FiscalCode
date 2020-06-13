package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "CodiceFiscale";
    private static final int THEME_DARK = 1;
    private static final int THEME_LIGHT = 0;
    public static CodiceFiscaleEntity codiceFiscaleEntity;
    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager prefs = new PreferenceManager(this);
        int theme = prefs.getTheme();
        if (theme == THEME_LIGHT){
            setTheme(R.style.LightTheme);
        }
        if (theme == THEME_DARK){
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        holder = new Holder();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class Holder implements View.OnClickListener,Switch.OnCheckedChangeListener {
        Parser parser;
        List<Comune> comuniList;
        List<Stato> statiList;
        AutoCompleteTextView atComuni;
        FloatingActionButton btnCalcola;
        Comune comuneSelected;
        Stato statoSelected;
        Toolbar toolbar;
        Switch swEstero;
        TextView tvRisultato;
        Button btnBirthday, button;
        EditText etName;
        EditText etSurname;
        RadioGroup rgGender;

        Button btnSaveDB;

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
            parser = new Parser(ProfileActivity.this);
            swEstero = findViewById(R.id.swEstero);
            swEstero.setOnCheckedChangeListener(this);

            comuniList = parser.parserComuni();
            statiList = parser.parserStati();

            toolbar = findViewById(R.id.toolbar);
            btnSaveDB = findViewById(R.id.btnSaveDB);
            btnSaveDB.setEnabled(false);
            btnSaveDB.setOnClickListener(this);
            autocompleteLayout = findViewById(R.id.autocompleteLayout);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            setUpDialogDate();
            setUpAutoCompleteTextView();
            codiceFiscaleEntity = AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getPersonalCode();

            if (codiceFiscaleEntity != null){
                etName.setText(codiceFiscaleEntity.getNome());
                etSurname.setText(codiceFiscaleEntity.getCognome());
                atComuni.setText(codiceFiscaleEntity.getComuneName(), false);
                btnBirthday.setText(codiceFiscaleEntity.getDataNascita());
                tvRisultato.setText(codiceFiscaleEntity.getFinalFiscalCode());
                if (codiceFiscaleEntity.getGenere().equals("M")){
                    rgGender.check(R.id.rbMale);
                }
            }

        }


        private void setUpAutoCompleteTextView() {
            if (!swEstero.isChecked()) {
                ArrayAdapter<Comune> comuneArrayAdapter = new ArrayAdapter<>(ProfileActivity.this,
                        android.R.layout.simple_dropdown_item_1line, comuniList);
                atComuni.setAdapter(comuneArrayAdapter);
            }else{
                ArrayAdapter<Stato> statoArrayAdapter = new ArrayAdapter<>(ProfileActivity.this,
                        android.R.layout.simple_dropdown_item_1line, statiList);
                atComuni.setAdapter(statoArrayAdapter);
            }

            onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (swEstero.isChecked()) {
                        statoSelected = (Stato) parent.getItemAtPosition(position);
                        hideKeyboard();
                    } else {
                        comuneSelected = (Comune) parent.getItemAtPosition(position);
                        hideKeyboard();
                        Log.d(TAG, comuneSelected.getCode() + " " + comuneSelected.getName());
                    }
                }
            };
            atComuni.setOnItemClickListener(onItemClickListener);
        }
        private void setUpDialogDate() {
            View.OnClickListener dateClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            };
            btnBirthday.setOnClickListener(dateClickListener);
        }

        private void showDatePickerDialog(View v) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCalcola) {
                btnSaveDB.setEnabled(true);         // schiarire colore pulsante quando non è abilitato
                hideKeyboard();
                computeCF();

            }
            if (v.getId() == R.id.btnData){
                showDatePickerDialog(v);
            }

            if (v.getId() == R.id.btnSaveDB & codiceFiscaleEntity != null){       // bisogna prima aver calcolato il codice fiscale
                if (AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getCode(codiceFiscaleEntity.getFinalFiscalCode()) != 0){
                    AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().setPersonal(codiceFiscaleEntity.getFinalFiscalCode());
                }
                else{
                    AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().saveNewCode(codiceFiscaleEntity);
                }
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
            if (!name.equals("") & !surname.equals("") & (comuneSelected != null || statoSelected != null)) {
                if (swEstero.isChecked()) {
                    codiceFiscaleEntity = new CodiceFiscaleEntity(name, surname, birthDay, gender, null, statoSelected,1);
                } else if (!swEstero.isChecked()){
                    codiceFiscaleEntity = new CodiceFiscaleEntity(name, surname, birthDay, gender, comuneSelected, null,1);
                }
                String fiscalCode = codiceFiscaleEntity.calculateCF();

                tvRisultato.setText(fiscalCode);
            }else{
                Toast.makeText(getApplicationContext(), "Completare tutti i campi", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            atComuni.getText().clear();
            if (swEstero.isChecked()){
                autocompleteLayout.setHint("Stato Estero di Nascita");
                setUpAutoCompleteTextView();
            }
            else{
                autocompleteLayout.setHint("Comune di Nascita");
                setUpAutoCompleteTextView();
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


package it.runningexamples.fiscalcode.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.runningexamples.fiscalcode.db.AppDatabase;
import it.runningexamples.fiscalcode.db.CodiceFiscaleEntity;
import it.runningexamples.fiscalcode.entity.Comune;
import it.runningexamples.fiscalcode.entity.Parser;
import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.entity.Stato;
import it.runningexamples.fiscalcode.tools.PreferenceManager;
import it.runningexamples.fiscalcode.tools.ThemeUtilities;

public class ProfileSettingsActivity extends AppCompatActivity {
    private static final String DATE_TAG = "datePicker"; //NON-NLS
    public static CodiceFiscaleEntity codiceFiscaleEntity;
    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtilities.applyActivityTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        holder = new Holder();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressWarnings("HardCodedStringLiteral")
    private class Holder implements View.OnClickListener,Switch.OnCheckedChangeListener,Toolbar.OnMenuItemClickListener {
        Parser parser;
        List<Comune> comuniList;
        List<Stato> statiList;
        AutoCompleteTextView atComuni;
        Comune comuneSelected;
        Stato statoSelected;
        Toolbar toolbar;
        Switch swEstero;
        TextView tvRisultato;
        Button btnBirthday, button;
        EditText etName, etSurname;
        RadioGroup rgGender;

        Button btnSaveProfile;

        com.google.android.material.textfield.TextInputLayout autocompleteLayout;

        AdapterView.OnItemClickListener onItemClickListener;

        public Holder() {
            tvRisultato = findViewById(R.id.tvRisultato);
            rgGender = findViewById(R.id.rgGender);
            btnBirthday = findViewById(R.id.btnData);
            etName = findViewById(R.id.etNome);
            etSurname = findViewById(R.id.etCognome);

            atComuni = findViewById(R.id.atComuni);
            parser = new Parser(ProfileSettingsActivity.this);
            swEstero = findViewById(R.id.swEstero);
            swEstero.setOnCheckedChangeListener(this);

            comuniList = parser.parserComuni();
            statiList = parser.parserStati();

            toolbar = findViewById(R.id.toolbar);
            btnSaveProfile = findViewById(R.id.btnSaveProfile);
            btnSaveProfile.setOnClickListener(this);
            autocompleteLayout = findViewById(R.id.autocompleteLayout);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setOnMenuItemClickListener(this);

            setUpDialogDate();
            setUpAutoCompleteTextView();
            codiceFiscaleEntity = AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getPersonalCode();

            if (codiceFiscaleEntity != null){
                etName.setText(codiceFiscaleEntity.getNome());
                etSurname.setText(codiceFiscaleEntity.getCognome());
                atComuni.setText(codiceFiscaleEntity.getLuogoNascita(), false);
                btnBirthday.setText(codiceFiscaleEntity.getDataNascita());
                tvRisultato.setText(codiceFiscaleEntity.getFinalFiscalCode());
                tvRisultato.setVisibility(View.VISIBLE);
                Context context = getApplicationContext();
                PreferenceManager prefs = new PreferenceManager(context);
                if (prefs.getTheme() == 0) {
                    btnBirthday.setTextColor(ContextCompat.getColor(context,R.color.colorTextNormalLight));
                }
                else{
                    btnBirthday.setTextColor(ContextCompat.getColor(context,R.color.colorTextNormalDark));
                }
                if (codiceFiscaleEntity.getGenere().equals("M")){
                    rgGender.check(R.id.rbMale);
                }
            }

        }


        private void setUpAutoCompleteTextView() {
            if (!swEstero.isChecked()) {
                ArrayAdapter<Comune> comuneArrayAdapter = new ArrayAdapter<>(ProfileSettingsActivity.this,
                        R.layout.autocomplete_layout, R.id.tvAutoCompleteItem,comuniList);
                atComuni.setAdapter(comuneArrayAdapter);
            }else{
                ArrayAdapter<Stato> statoArrayAdapter = new ArrayAdapter<>(ProfileSettingsActivity.this,
                        R.layout.autocomplete_layout, R.id.tvAutoCompleteItem, statiList);
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
            DialogFragment newFragment = new DatePickerFragment(getApplicationContext());
            newFragment.show(getSupportFragmentManager(), DATE_TAG);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnData){
                showDatePickerDialog(v);
            }

            if (v.getId() == R.id.btnSaveProfile) {
                hideKeyboard();
                if (computeCF() && codiceFiscaleEntity != null) {
                    Snackbar snackbar = Snackbar.make(v, getString(R.string.profileSaved), Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(getColor(R.color.greenSnackbar));
                    if (AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getCode(codiceFiscaleEntity.getFinalFiscalCode()) != 0) {
                        AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().setPersonal(codiceFiscaleEntity.getFinalFiscalCode());
                        snackbar.show();
                    } else {
                        AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().saveNewCode(codiceFiscaleEntity);
                        snackbar.show();
                    }
                }
            }
        }
        private boolean computeCF() {
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
                    if (codiceFiscaleEntity != null) {
                        AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().removePersonal(codiceFiscaleEntity.getFinalFiscalCode());
                    }
                    codiceFiscaleEntity = new CodiceFiscaleEntity(name, surname, birthDay, gender, null, statoSelected,1);
                } else if (!swEstero.isChecked()){
                    if (codiceFiscaleEntity != null) {
                        AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().removePersonal(codiceFiscaleEntity.getFinalFiscalCode());
                    }
                    codiceFiscaleEntity = new CodiceFiscaleEntity(name, surname, birthDay, gender, comuneSelected, null,1);
                }
                String fiscalCode = codiceFiscaleEntity.calculateCF();

                tvRisultato.setText(fiscalCode);
                tvRisultato.setVisibility(View.VISIBLE);

                return true;
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.fillForm), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            atComuni.getText().clear();
            if (swEstero.isChecked()){
                autocompleteLayout.setHint(getString(R.string.formStatoEstero));
                setUpAutoCompleteTextView();
            }
            else{
                autocompleteLayout.setHint(getString(R.string.formComune));
                setUpAutoCompleteTextView();
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (codiceFiscaleEntity!=null) {
                new AlertDialog.Builder(ProfileSettingsActivity.this)
                        .setMessage(getString(R.string.confirmDelete))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().removePersonal(codiceFiscaleEntity.getFinalFiscalCode());
                                holder.etName.getText().clear();
                                holder.etSurname.getText().clear();
                                holder.atComuni.getText().clear();
                                recreate();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
            else{
                Toast.makeText(getApplicationContext(),getString(R.string.noProfile),Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }



    private void hideKeyboard() {
        View view = this.getCurrentFocus(); // controlla se non ci sono focus attivi
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}


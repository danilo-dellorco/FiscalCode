/**
 * Activity che permette di modificare i dati del proprio Profilo Personale, oppure di impostare
 * un nuovo profilo.
 */

package it.runningexamples.fiscalcode.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import it.runningexamples.fiscalcode.tools.ThemeUtilities;

public class ProfileSettingsActivity extends AppCompatActivity {
    private static final String DATE_TAG = "datePicker"; //NON-NLS
    public static CodiceFiscaleEntity codiceFiscaleEntity;
    public Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtilities.applyActivityTheme(this);    //Applica il tema impostato nelle preferenze ad ogni avvio
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        holder = new Holder();
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
        Button btnBirthday;
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
            swEstero = findViewById(R.id.swEstero);
            toolbar = findViewById(R.id.toolbar);
            autocompleteLayout = findViewById(R.id.autocompleteLayout);
            btnSaveProfile = findViewById(R.id.btnSaveProfile);
            swEstero.setOnCheckedChangeListener(this);
            btnSaveProfile.setOnClickListener(this);

            //Imposta il parser per l'AutoComplete
            parser = new Parser(ProfileSettingsActivity.this);
            comuniList = parser.parserComuni();
            statiList = parser.parserStati();

            //Imposta la Toolbar come una ActionBar
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setOnMenuItemClickListener(this);

            //Esegue il tutorial al primo avvio
            setUpDialogDate();
            setUpAutoCompleteTextView();
            codiceFiscaleEntity = AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getPersonalCode();
            setUpProfile(codiceFiscaleEntity);
        }

        //Metodo per impostare l'autocompleteTextView relativa ai comuni
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
            onItemClickListener = (parent, view, position, id) -> {
                if (swEstero.isChecked()) {
                    statoSelected = (Stato) parent.getItemAtPosition(position);
                    hideKeyboard();
                } else {
                    comuneSelected = (Comune) parent.getItemAtPosition(position);
                    hideKeyboard();
                }
            };
            atComuni.setOnItemClickListener(onItemClickListener);
        }

        // Imposta il dialog relativo al DatePicker
        private void setUpDialogDate() {
            View.OnClickListener dateClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(v);
                }
            };
            btnBirthday.setOnClickListener(dateClickListener);
        }

        // Mostra il dialog per la scelta della data
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


        /*
        Metodo che calcola il codice fiscale tramite i dati inseriti nel form. Se tutti i campi
        sono inseriti correttamente viene calcolato il codice e ritorna true, altrimenti false.
        */
        private boolean computeCF() {
            String surname = etSurname.getText().toString();
            String name = etName.getText().toString();
            int radioID = rgGender.getCheckedRadioButtonId();

            String gender = (String) ((RadioButton) findViewById(radioID)).getText();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date birthDay = null;
            try {
                birthDay = simpleDateFormat.parse(btnBirthday.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!name.equals("") & !surname.equals("") & (comuneSelected != null || statoSelected != null) & birthDay!=null) {
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

        /*
        Metodo che cambia l'AutoCompleteTextView se si effettua lo switch da comune a stato estero
        e viceversa eliminando il testo inserito in precedenza
        */
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
                                resetProfile();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
            else{
                Toast.makeText(getApplicationContext(),getString(R.string.noProfile),Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        // Metodo che imposta i dati del form in base a quelli del profilo attualmente salvato
        private void setUpProfile(CodiceFiscaleEntity entityProfile){
            if (entityProfile != null){
                etName.setText(entityProfile.getNome());
                etSurname.setText(entityProfile.getCognome());
                atComuni.setText(entityProfile.getLuogoNascita(), false);
                btnBirthday.setText(entityProfile.getDataNascita());
                tvRisultato.setText(entityProfile.getFinalFiscalCode());
                tvRisultato.setVisibility(View.VISIBLE);
                Context context = getApplicationContext();
                ThemeUtilities.setDateTextColor(context,btnBirthday);
                if (entityProfile.getGenere().equals("M")){
                    rgGender.check(R.id.rbMale);
                }
            }
        }
    }

    //Metodo che nasconde la tastiera se nessuna view ha il focus
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //Metodo che elimina il profilo personale e resetta i campi del form
    public void resetProfile(){
        AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().removePersonal(codiceFiscaleEntity.getFinalFiscalCode());
        holder.etName.getText().clear();
        holder.etSurname.getText().clear();
        holder.atComuni.getText().clear();
        recreate();
    }

    //Imposta il menu della toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Imposto il back button della toolbar per eseguire l'azione onBackPressed()
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//TODO Copia bottone carview
//TODO Selezione multipla cardview
//TODO Hint vari ogni prima cosa che fai
//TODO Rifare icona
//TODO Splash activity aperture successive alla prima
//TODO Traduzioni
//TODO Stringhe non hardcoded
//TODO Ripulire codice
//TODO Organizzare in cartelle classi e drawable
//TODO Creare classi ausiliarie
//TODO Swipe left per scegliere codice come personale
//TODO uniform text cardview
//TODO schermate vuote
//TODO
//TODO undo anche per eliminazione multipla? :)
//TODO
//TODO
//TODO

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CodiceFiscale";
    public static CodiceFiscaleEntity codiceFiscaleEntity;
    Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtilities.applyActivityTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        holder = new Holder();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.askExit)
                .setCancelable(false)
                .setPositiveButton(R.string.positiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.negativeButton, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private class Holder implements View.OnClickListener,Switch.OnCheckedChangeListener, Toolbar.OnMenuItemClickListener {
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
        Button btnBirthday;
        EditText etName;
        EditText etSurname;
        RadioGroup rgGender;

        ImageButton btnSaveDB,btnCopy,btnShare;

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

            comuniList = parser.parserComuni();
            statiList = parser.parserStati();

            toolbar = findViewById(R.id.toolbar);
            btnSaveDB = findViewById(R.id.btnSaveDB);
            btnCopy = findViewById(R.id.btnCopy);
            btnShare = findViewById(R.id.btnShare);
            btnSaveDB.setOnClickListener(this);
            btnCopy.setOnClickListener(this);
            btnShare.setOnClickListener(this);
            autocompleteLayout = findViewById(R.id.autocompleteLayout);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toolbar.setOnMenuItemClickListener(this);

            setUpDialogDate();
            setUpAutoCompleteTextView();
        }


        private void setUpAutoCompleteTextView() {
            if (!swEstero.isChecked()) {
                ArrayAdapter<Comune> comuneArrayAdapter = new ArrayAdapter<>(MainActivity.this,
                        R.layout.autocomplete_layout, R.id.tvAutoCompleteItem,comuniList);
                    atComuni.setAdapter(comuneArrayAdapter);
            }else{
                ArrayAdapter<Stato> statoArrayAdapter = new ArrayAdapter<>(MainActivity.this,
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
            DialogFragment newFragment = new DatePickerFragment(getApplicationContext());
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCalcola) {
                hideKeyboard();
                if (computeCF()) {
                    btnSaveDB.setVisibility(View.VISIBLE);
                    btnCopy.setVisibility(View.VISIBLE);
                    btnShare.setVisibility(View.VISIBLE);
                }

            }
            if (v.getId() == R.id.btnData){
                showDatePickerDialog(v);
            }
            if (v.getId() == R.id.btnSaveDB) {       // bisogna prima aver calcolato il codice fiscale
                Snackbar sn;
                if (AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getCode(codiceFiscaleEntity.getFinalFiscalCode()) == 0) {
                    AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().saveNewCode(codiceFiscaleEntity);
                    sn = Snackbar.make(v, "Elemento salvato", Snackbar.LENGTH_LONG);
                    sn.getView().setBackgroundColor(getColor(R.color.greenSnackbar));
                    sn.show();
                } else {
                    sn = Snackbar.make(v, "Elemento già presente nella lista", Snackbar.LENGTH_LONG);
                    sn.getView().setBackgroundColor(getColor(R.color.colorOutlineRed));
                    sn.show();
                }
            }

            if (v.getId() == R.id.btnCopy){
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Codice Fiscale", tvRisultato.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),"Codice Fiscale copiato negli appunti",Toast.LENGTH_SHORT).show();
            }
            if (v.getId() == R.id.btnShare){
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, tvRisultato.getText());
                sharingIntent.setType("text/plain");
                startActivity(Intent.createChooser(sharingIntent, "Condividi tramite"));
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
                    codiceFiscaleEntity = new CodiceFiscaleEntity(name, surname, birthDay, gender, null, statoSelected,0);
                } else if (!swEstero.isChecked()){
                    codiceFiscaleEntity = new CodiceFiscaleEntity(name, surname, birthDay, gender, comuneSelected, null,0);
                }
                String fiscalCode = codiceFiscaleEntity.calculateCF();
                tvRisultato.setText(fiscalCode);
                return true;
            }else{
                Toast.makeText(getApplicationContext(), "Completare tutti i campi", Toast.LENGTH_LONG).show();
                return false;
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

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.menu_settings) {
                Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
            }
            if (item.getItemId() == R.id.menu_list){
                Intent intentList = new Intent(MainActivity.this, SavedActivity.class);
                startActivity(intentList);
            }
            if (item.getItemId() == R.id.menu_favorites){
                CodiceFiscaleEntity codicePersonale = AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getPersonalCode();
                if (codicePersonale == null){
                    Toast.makeText(getApplicationContext(),"Imposta un profilo personale",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ProfileSettingsActivity.class));
                }
                else{
                    Intent intentList = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intentList);
                }
            }
            return true;
        }
    }



    private void hideKeyboard() {
        // Check if no view has focus
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}


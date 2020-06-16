package it.runningexamples.fiscalcode.ui;

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

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.runningexamples.fiscalcode.db.AppDatabase;
import it.runningexamples.fiscalcode.db.CodiceFiscaleEntity;
import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.entity.Comune;
import it.runningexamples.fiscalcode.entity.Parser;
import it.runningexamples.fiscalcode.entity.Stato;
import it.runningexamples.fiscalcode.tools.PreferenceManager;
import it.runningexamples.fiscalcode.tools.ThemeUtilities;

//TODO Ripulire codice
//TODO Creare classi ausiliarie
//TODO Commentare codice

public class MainActivity extends AppCompatActivity {
    private static final String DATE_TAG = "datePicker"; //NON-NLS
    private static final String MAIN = "main"; //NON-NLS
    private static final String CALC = "calc"; //NON-NLS
    public static CodiceFiscaleEntity codiceFiscaleEntity;
    public PreferenceManager prefs;

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
                .setMessage(R.string.confirmExit)
                .setCancelable(false)
                .setPositiveButton(R.string.choicePositive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.choiceNegative, null)
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
            prefs = new PreferenceManager(getApplicationContext());
            if (prefs.isFirstActivity(MAIN)){
                firstTutorial();
            }
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
            if (v.getId() == R.id.btnCalcola) {
                hideKeyboard();
                if (computeCF()) {
                    if (prefs.isFirstActivity(CALC)){
                        calcTutorial();
                    }
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
                    sn = Snackbar.make(v, getString(R.string.savedElement), Snackbar.LENGTH_LONG);
                    sn.getView().setBackgroundColor(getColor(R.color.greenSnackbar));
                    sn.show();
                } else {
                    sn = Snackbar.make(v, getString(R.string.presentElement), Snackbar.LENGTH_LONG);
                    sn.getView().setBackgroundColor(getColor(R.color.colorOutlineRed));
                    sn.show();
                }
            }

            if (v.getId() == R.id.btnCopy){
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, tvRisultato.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),getString(R.string.clipboardCode),Toast.LENGTH_SHORT).show();
            }
            if (v.getId() == R.id.btnShare){
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, tvRisultato.getText());
                sharingIntent.setType("text/plain"); //NON-NLS
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.shareCode)));
            }

        }

        private boolean computeCF() {
            String surname = etSurname.getText().toString();
            String name = etName.getText().toString();
            int radioID = rgGender.getCheckedRadioButtonId();

            String gender = (String) ((RadioButton) findViewById(radioID)).getText();
            // Get birthday
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); //NON-NLS
            Date birthDay = null;
            try {
                birthDay = simpleDateFormat.parse(btnBirthday.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!name.equals("") & !surname.equals("") & (comuneSelected != null || statoSelected != null) & birthDay!=null) {
                if (swEstero.isChecked()) {
                    codiceFiscaleEntity = new CodiceFiscaleEntity(name, surname, birthDay, gender, null, statoSelected,0);
                } else if (!swEstero.isChecked()){
                    codiceFiscaleEntity = new CodiceFiscaleEntity(name, surname, birthDay, gender, comuneSelected, null,0);
                }
                String fiscalCode = codiceFiscaleEntity.calculateCF();
                tvRisultato.setText(fiscalCode);
                return true;
            }else{
                Toast.makeText(getApplicationContext(), R.string.fillForm, Toast.LENGTH_LONG).show();
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
            if (item.getItemId() == R.id.menu_settings) {
                Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
            }
            if (item.getItemId() == R.id.menu_list){
                Intent intentList = new Intent(MainActivity.this, SavedActivity.class);
                startActivity(intentList);
            }
            if (item.getItemId() == R.id.menu_favorites){
                Intent intentList = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intentList);
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

    private void firstTutorial(){
        BubbleShowCaseBuilder builder1 = new BubbleShowCaseBuilder(MainActivity.this);
        builder1.title(getString(R.string.bubbleMainNome));
        builder1.targetView(findViewById(R.id.etNome));

        BubbleShowCaseBuilder builder2 = new BubbleShowCaseBuilder(MainActivity.this);
        builder2.title(getString(R.string.bubbleMainFab));
        builder2.targetView(findViewById(R.id.btnCalcola));

        BubbleShowCaseSequence sequence = new BubbleShowCaseSequence();
        sequence.addShowCase(builder1);
        sequence.addShowCase(builder2);
        sequence.show();
        prefs.setFirstActivity(MAIN,false);
    }

    private void calcTutorial(){
        BubbleShowCaseBuilder builder1 = new BubbleShowCaseBuilder(MainActivity.this);
        builder1.title(getString(R.string.bubbleCalcRisultato));
        builder1.targetView(findViewById(R.id.tvRisultato));

        BubbleShowCaseBuilder builder2 = new BubbleShowCaseBuilder(MainActivity.this);
        builder2.title(getString(R.string.bubbleCalcSave));
        builder2.targetView(findViewById(R.id.btnSaveDB));

        BubbleShowCaseBuilder builder3 = new BubbleShowCaseBuilder(MainActivity.this);
        builder3.title(getString(R.string.bubbleCalcCopy));
        builder3.targetView(findViewById(R.id.btnCopy));

        BubbleShowCaseBuilder builder4 = new BubbleShowCaseBuilder(MainActivity.this);
        builder4.title(getString(R.string.bubbleCalcShare));
        builder4.targetView(findViewById(R.id.btnShare));

        BubbleShowCaseBuilder builder5 = new BubbleShowCaseBuilder(MainActivity.this);
        builder5.title(getString(R.string.bubbleCalcProfile));
        builder5.targetView(holder.toolbar.findViewById(R.id.menu_favorites));

        BubbleShowCaseBuilder builder6 = new BubbleShowCaseBuilder(MainActivity.this);
        builder6.title(getString(R.string.bubbleCalcList));
        builder6.targetView(holder.toolbar.findViewById(R.id.menu_list));

        BubbleShowCaseSequence sequence = new BubbleShowCaseSequence();
        sequence.addShowCase(builder1);
        sequence.addShowCase(builder2);
        sequence.addShowCase(builder3);
        sequence.addShowCase(builder4);
        sequence.addShowCase(builder5);
        sequence.addShowCase(builder6);
        sequence.show();
        prefs.setFirstActivity(CALC,false);
    }


}


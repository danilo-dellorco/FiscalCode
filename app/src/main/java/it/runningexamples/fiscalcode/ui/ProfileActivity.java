package it.runningexamples.fiscalcode.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.runningexamples.fiscalcode.db.AppDatabase;
import it.runningexamples.fiscalcode.db.CodiceFiscaleEntity;
import it.runningexamples.fiscalcode.entity.FiscalBarcode;
import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.tools.ThemeUtilities;

public class ProfileActivity extends AppCompatActivity{
    CodiceFiscaleEntity codice;
    Holder holder;
    HolderEmpty holderEmpty;

    public class HolderEmpty implements View.OnClickListener{
        FloatingActionButton btnNewProfile;

        public HolderEmpty(){
            btnNewProfile = findViewById(R.id.btnNewProfile);
            btnNewProfile.setOnClickListener(this);
            Toolbar toolbarProfile = findViewById(R.id.toolbarProfile);
            setSupportActionBar(toolbarProfile);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ProfileActivity.this, ProfileSettingsActivity.class);
            startActivity(intent);
        }
    }

    public class Holder implements View.OnClickListener, Toolbar.OnMenuItemClickListener{
        String codiceStringa;
        CodiceFiscaleEntity codice;
        ImageView ivBarcode;
        CardView personalCard;
        TextView nome;
        TextView cognome;
        TextView luogo;
        TextView data;
        TextView sesso;
        Button code;

        public Holder(){
            codice = AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getPersonalCode();
            ivBarcode = findViewById(R.id.ivBarcode);
            personalCard = findViewById(R.id.personal_layout);
            nome = personalCard.findViewById(R.id.tvDetailNome);
            cognome = personalCard.findViewById(R.id.tvDetailCognome);
            luogo = personalCard.findViewById(R.id.tvDetailLuogo);
            data = personalCard.findViewById(R.id.tvDetailData);
            sesso = personalCard.findViewById(R.id.tvDetailSesso);
            code = personalCard.findViewById(R.id.btnDetailCode);

            nome.setText(codice.getNome());
            cognome.setText(codice.getCognome());
            luogo.setText(codice.getLuogoNascita());
            data.setText(codice.getDataNascita());
            codiceStringa = codice.getFinalFiscalCode();
            code.setText(codiceStringa);
            if (codice.getGenere().equals("M")) { //NON-NLS
                sesso.setText(R.string.genereMaschio);
            } else {
                sesso.setText(R.string.genereFemmina);
            }
            personalCard.setOnClickListener(this);
            ivBarcode.setOnClickListener(this);
            code.setOnClickListener(this);
            FiscalBarcode barcode = new FiscalBarcode(codiceStringa);
            ivBarcode.setImageBitmap(barcode.generateBarcode());

            Toolbar toolbarProfile = findViewById(R.id.toolbarProfile);
            setSupportActionBar(toolbarProfile);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbarProfile.setOnMenuItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnDetailCode){
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, codiceStringa);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),getString(R.string.clipboardCode),Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(ProfileActivity.this, CFDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     // altrimenti non funziona :(
                intent.putExtra("CF", codice);      //NON-NLS
                startActivity(intent);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.menu_edit){
                startActivity(new Intent(ProfileActivity.this, ProfileSettingsActivity.class));
            }
            if (item.getItemId() == R.id.menu_share){
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, holder.codiceStringa);
                sharingIntent.setType("text/plain"); //NON-NLS
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.shareCode)));
            }
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtilities.applyActivityTheme(this);
        codice = AppDatabase.getInstance(this).codiceFiscaleDAO().getPersonalCode();
        super.onCreate(savedInstanceState);
        if (codice == null){
            setContentView(R.layout.layout_empty_profile);
            holderEmpty = new HolderEmpty();
        }
        else{
            setContentView(R.layout.activity_profile);
            holder = new Holder();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getPersonalCode() != null){
            setContentView(R.layout.activity_profile);
            codice = AppDatabase.getInstance(this).codiceFiscaleDAO().getPersonalCode();
            holder = new Holder();
        }
        else{
            setContentView(R.layout.layout_empty_profile);
            holderEmpty = new HolderEmpty();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (AppDatabase.getInstance(getApplicationContext()).codiceFiscaleDAO().getPersonalCode() != null){
            getMenuInflater().inflate(R.menu.top_bar_menu_profile, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
}

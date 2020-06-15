package it.runningexamples.fiscalcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    String codiceStringa;
    CodiceFiscaleEntity codice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtilities.applyActivityTheme(this);
        codice = AppDatabase.getInstance(this).codiceFiscaleDAO().getPersonalCode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView ivBarcode = findViewById(R.id.ivBarcode);
        CardView personalCard = findViewById(R.id.personal_layout);
        TextView nome = personalCard.findViewById(R.id.tvDetailNome);
        TextView cognome = personalCard.findViewById(R.id.tvDetailCognome);
        TextView luogo = personalCard.findViewById(R.id.tvDetailLuogo);
        TextView data = personalCard.findViewById(R.id.tvDetailData);
        TextView sesso = personalCard.findViewById(R.id.tvDetailSesso);
        Button code = personalCard.findViewById(R.id.btnDetailCode);


        nome.setText(codice.getNome());
        cognome.setText(codice.getCognome());
        luogo.setText(codice.getLuogoNascita());
        data.setText(codice.getDataNascita());
        codiceStringa = codice.getFinalFiscalCode();
        code.setText(codiceStringa);
        if (codice.getGenere().equals("M")) {
            sesso.setText(R.string.genereMaschio);
        } else {
            sesso.setText(R.string.genereFemmina);
        }

        Toolbar toolbarProfile = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarProfile.setOnMenuItemClickListener(this);

        personalCard.setOnClickListener(this);
        ivBarcode.setOnClickListener(this);

        FiscalBarcode barcode = new FiscalBarcode(codiceStringa);
        ivBarcode.setImageBitmap(barcode.generateBarcode());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_edit){
            startActivity(new Intent(ProfileActivity.this, ProfileSettingsActivity.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_share){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, codiceStringa);
            sharingIntent.setType("text/plain");
            startActivity(Intent.createChooser(sharingIntent, "Condividi Codice Tramite"));
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ProfileActivity.this, CFDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     // altrimenti non funziona :(
        intent.putExtra("CF", codice);      // PARCELABLE
        startActivity(intent);
    }
}

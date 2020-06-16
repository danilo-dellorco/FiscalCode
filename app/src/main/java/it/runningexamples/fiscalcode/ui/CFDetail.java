package it.runningexamples.fiscalcode.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import it.runningexamples.fiscalcode.db.CodiceFiscaleEntity;
import it.runningexamples.fiscalcode.entity.FiscalBarcode;
import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.tools.AppUtilities;

public class CFDetail extends AppCompatActivity {

    TextView tv;
    ImageView ivBarcode;
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String PAR_KEY = "CF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppUtilities.applyActivityTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_cfdetail);
        // Gestione luminosità schermo
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = 1F;
        getWindow().setAttributes(layout);

        tv = findViewById(R.id.tvBarcode);
        ivBarcode = findViewById(R.id.ivBarcode);

        CodiceFiscaleEntity cf = getIntent().getExtras().getParcelable(PAR_KEY);
        tv.setText(cf.getFinalFiscalCode());

        Bitmap fiscalBarcode = new FiscalBarcode(cf.getFinalFiscalCode()).generateBarcode();
        ivBarcode.setImageBitmap(fiscalBarcode);
        Toolbar toolbarDetail = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
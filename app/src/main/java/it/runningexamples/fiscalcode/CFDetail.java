package it.runningexamples.fiscalcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class CFDetail extends AppCompatActivity {

    TextView tv;
    ImageView ivBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtilities.applyActivityTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_cfdetail);
        // Luminosità
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = 1F;
        getWindow().setAttributes(layout);

        tv = findViewById(R.id.tvBarcode);
        ivBarcode = findViewById(R.id.ivBarcode);

        CodiceFiscaleEntity cf = getIntent().getExtras().getParcelable("CF");
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
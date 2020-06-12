package it.runningexamples.fiscalcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CFDetail extends AppCompatActivity {

    TextView tv;
    ImageView ivBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_cfdetail);
        tv = findViewById(R.id.tvBarcode);
        ivBarcode = findViewById(R.id.ivBarcode);

        CodiceFiscale cf = getIntent().getExtras().getParcelable("CF");
        tv.setText(cf.getFinalFiscalCode());

        Bitmap fiscalBarcode = new FiscalBarcode(cf.getFinalFiscalCode()).generateBarcode();
        ivBarcode.setImageBitmap(fiscalBarcode);

    }
}
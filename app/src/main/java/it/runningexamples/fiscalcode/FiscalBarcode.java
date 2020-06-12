package it.runningexamples.fiscalcode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class FiscalBarcode {
    String codiceFiscale;

    FiscalBarcode(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public Bitmap generateBarcode() {
        Code39Writer writer = new Code39Writer();
        BitMatrix bitMatrix = null;
        Bitmap bitmap = null;

        try {
            bitMatrix = writer.encode(codiceFiscale, BarcodeFormat.CODE_39, 1000, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

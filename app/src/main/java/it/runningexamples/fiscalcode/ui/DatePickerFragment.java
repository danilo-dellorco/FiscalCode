package it.runningexamples.fiscalcode.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import it.runningexamples.fiscalcode.R;
import it.runningexamples.fiscalcode.tools.ThemeUtilities;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener  {
    Context context;

    public DatePickerFragment(){

    }

    public DatePickerFragment(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog =  new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        return datePickerDialog;
    }

    @SuppressWarnings("HardCodedStringLiteral")
    // Imposta il testo del bottone in base alla data selezionata
    // Imposta il colore del testo da hint a normal
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
            Button btnData = getActivity().findViewById(R.id.btnData);
            btnData.setText(String.format("%02d/%02d/%d", day, month + 1, year));
            ThemeUtilities.setDateTextColor(context,btnData);
    }
}

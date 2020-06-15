package it.runningexamples.fiscalcode;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

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
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
            Button btnData = getActivity().findViewById(R.id.btnData);
            btnData.setText(String.format("%02d/%02d/%d", day, month + 1, year));
            PreferenceManager prefs = new PreferenceManager(context);
            if (prefs.getTheme() == 0) {
                btnData.setTextColor(Color.parseColor("#3C3C3C"));
            }
            else{
                btnData.setTextColor(Color.parseColor("#FFFFFF"));
            }
    }
}

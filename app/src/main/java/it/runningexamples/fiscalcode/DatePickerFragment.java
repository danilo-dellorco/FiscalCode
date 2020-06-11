package it.runningexamples.fiscalcode;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener  {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        CharSequence oldDate = ((Button) getActivity().findViewById(R.id.btnData)).getText();
        int year = Integer.parseInt(oldDate.subSequence(6, 10).toString());
        int month = Integer.parseInt(oldDate.subSequence(3, 5).toString()) - 1;  //
        int day = Integer.parseInt(oldDate.subSequence(0, 2).toString());

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
            ((Button) getActivity().findViewById(R.id.btnData)).setText(String.format("%02d/%02d/%d", day, month + 1, year));
    }
}

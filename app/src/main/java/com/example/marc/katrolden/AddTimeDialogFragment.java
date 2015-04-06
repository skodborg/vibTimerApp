package com.example.marc.katrolden;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class AddTimeDialogFragment extends DialogFragment {

    private final int MINS_TO_MILLISEC = 60000;
    private final int SECS_TO_MILLISEC = 1000;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.addtimer_dialog, null);

        final NumberPicker np_secs = (NumberPicker) view.findViewById(R.id.np_secs);
        np_secs.setMinValue(0);
        np_secs.setMaxValue(59);
        final NumberPicker np_mins = (NumberPicker) view.findViewById(R.id.np_mins);
        np_mins.setMinValue(0);
        np_mins.setMaxValue(99);

        builder.setView(view)
                .setMessage(R.string.addTimer_dialogTitle)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Add timer
                        int mins = np_mins.getValue();
                        int secs = np_secs.getValue();
                        int millisecs = 0;
                        millisecs += mins * MINS_TO_MILLISEC;
                        millisecs += secs * SECS_TO_MILLISEC;
                        MainActivity main_act = (MainActivity) getActivity();
                        main_act.addNewTimer(millisecs);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}

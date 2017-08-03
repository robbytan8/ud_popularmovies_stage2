package com.robby.popular_movies_stage_2a;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by Robby on 7/29/2017.
 * @author Robby Tan
 */

public class OpenOtherAppDialogFragment extends DialogFragment {

    private MyDialogListener listener;

    public static OpenOtherAppDialogFragment newInstance(MyDialogListener listener) {
        OpenOtherAppDialogFragment dialogFragment = new OpenOtherAppDialogFragment();
        dialogFragment.listener = listener;
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(getResources().getString(R.string.dialog_open_app_text));
        dialog.setPositiveButton(getResources().getString(R.string.dialog_open_app_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDialogPositiveClick(OpenOtherAppDialogFragment.this);
            }
        });
        dialog.setNegativeButton(getResources().getString(R.string.dialog_open_app_cancel), null);
        return dialog.create();
    }

    public interface MyDialogListener {
        void onDialogPositiveClick(DialogFragment dialogFragment);
    }
}

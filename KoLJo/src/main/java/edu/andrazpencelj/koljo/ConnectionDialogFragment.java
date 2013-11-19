package edu.andrazpencelj.koljo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Andraž on 13.11.2013.
 */
public class ConnectionDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.dialog_text_title)
                .setMessage(R.string.dialog_text_content)
                .setPositiveButton(R.string.button_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //prikazemo nastavitve za vklop povezave
                        Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(i);
                    }
                })
                .setNeutralButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog se zapre
                    }
                });
        return builder.create();
    }

}

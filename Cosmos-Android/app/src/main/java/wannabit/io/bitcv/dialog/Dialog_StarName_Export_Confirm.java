package wannabit.io.bitcv.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import wannabit.io.bitcv.activities.chains.starname.StarNameWalletConnectActivity;
import wannabit.io.bitcv.R;

public class Dialog_StarName_Export_Confirm extends DialogFragment {

    public static Dialog_StarName_Export_Confirm newInstance(Bundle bundle) {
        Dialog_StarName_Export_Confirm frag = new Dialog_StarName_Export_Confirm();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_starname_export_confirm, null);
        Button btn_negative = view.findViewById(R.id.btn_nega);
        Button btn_positive = view.findViewById(R.id.btn_posi);
        TextView exportMsg = view.findViewById(R.id.tv_export_msg);
        String msg = String.format(getString(R.string.str_starname_walletconnect_alert_msg2), getArguments().getString("msg"));
        exportMsg.setText(msg);


        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StarNameWalletConnectActivity)getActivity()).onExportAddresses(getArguments().getString("jsonData"));
                getDialog().dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }


}
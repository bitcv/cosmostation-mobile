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

import wannabit.io.bitcv.activities.HtlcResultActivity;
import wannabit.io.bitcv.R;

public class Dialog_Htlc_Error extends DialogFragment {

    public static Dialog_Htlc_Error newInstance(Bundle bundle) {
        Dialog_Htlc_Error frag = new Dialog_Htlc_Error();
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
        View view  = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_htlc_error, null);
        TextView msg = view.findViewById(R.id.dialog_msg);
        TextView error = view.findViewById(R.id.dialog_error);

        msg.setText(getArguments().getString("msg"));
        error.setText(getArguments().getString("error"));

        Button btn_negative = view.findViewById(R.id.btn_nega);
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTopActivity().onFinishWithError();
                getDialog().dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    private HtlcResultActivity getTopActivity() {
        return (HtlcResultActivity)getActivity();
    }
}
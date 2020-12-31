package wannabit.io.bitcv.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import wannabit.io.bitcv.activities.AppLockSetActivity;
import wannabit.io.bitcv.R;

public class Dialog_LockTime extends DialogFragment {

    public static Dialog_LockTime newInstance() {
        Dialog_LockTime frag = new Dialog_LockTime();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_locktime, null);

        LinearLayout lockTime_immediately = view.findViewById(R.id.lockTime_immediately);
        LinearLayout lockTime_10sec = view.findViewById(R.id.lockTime_10sec);
        LinearLayout lockTime_30sec = view.findViewById(R.id.lockTime_30sec);
        LinearLayout lockTime_60sec = view.findViewById(R.id.lockTime_60sec);

        lockTime_immediately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppLockSetActivity)getActivity()).onUpdateLockTime(0);
                getDialog().dismiss();

            }
        });

        lockTime_10sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppLockSetActivity)getActivity()).onUpdateLockTime(1);
                getDialog().dismiss();

            }
        });

        lockTime_30sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppLockSetActivity)getActivity()).onUpdateLockTime(2);
                getDialog().dismiss();
            }
        });

        lockTime_60sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppLockSetActivity)getActivity()).onUpdateLockTime(3);
                getDialog().dismiss();

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}

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

import wannabit.io.bitcv.activities.RestoreActivity;
import wannabit.io.bitcv.R;

public class Dialog_KavaRestorePath extends DialogFragment {

    private LinearLayout mOldPath, mNewPath;

    public static Dialog_KavaRestorePath newInstance(Bundle bundle) {
        Dialog_KavaRestorePath frag = new Dialog_KavaRestorePath();
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_kava_restore_path, null);

        mOldPath = view.findViewById(R.id.old_path);
        mNewPath = view.findViewById(R.id.new_path);

        mOldPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RestoreActivity)getActivity()).onUsingNewBip44(false);
                getDialog().dismiss();
            }
        });

        mNewPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RestoreActivity)getActivity()).onUsingNewBip44(true);
                getDialog().dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

}
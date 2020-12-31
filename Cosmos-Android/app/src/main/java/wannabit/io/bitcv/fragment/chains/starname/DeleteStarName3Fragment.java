package wannabit.io.bitcv.fragment.chains.starname;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;

import wannabit.io.bitcv.activities.chains.starname.DeleteStarNameActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseChain.IOV_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_TEST;
import static wannabit.io.bitcv.base.BaseConstant.IOV_MSG_TYPE_DELETE_DOMAIN;

public class DeleteStarName3Fragment extends BaseFragment implements View.OnClickListener {

    private Button mBeforeBtn, mConfirmBtn;
    private TextView mFeeAmount, mStarName, mExpireTime, mMemo;

    public static DeleteStarName3Fragment newInstance(Bundle bundle) {
        DeleteStarName3Fragment fragment = new DeleteStarName3Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delete_starname_3, container, false);
        mBeforeBtn      = rootView.findViewById(R.id.btn_before);
        mConfirmBtn     = rootView.findViewById(R.id.btn_confirm);
        mFeeAmount      = rootView.findViewById(R.id.tx_fee_amount);
        mStarName       = rootView.findViewById(R.id.to_delete_starname);
        mExpireTime     = rootView.findViewById(R.id.expire_time);
        mMemo           = rootView.findViewById(R.id.memo);

        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        BigDecimal feeAmount = new BigDecimal(getSActivity().mFee.amount.get(0).amount);

        if (getSActivity().mBaseChain.equals(IOV_MAIN) || getSActivity().mBaseChain.equals(IOV_TEST)) {
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 6, 6));

        }
        if (getSActivity().mDeleteType.equals(IOV_MSG_TYPE_DELETE_DOMAIN)) {
            mStarName.setText( "*" + getSActivity().mToDelDomain );
        } else {
            mStarName.setText( getSActivity().mToDelDomain + "*" + getSActivity().mToDelAccount );
        }
        mExpireTime.setText(WDp.getDpTime(getContext(), getSActivity().mValidTime * 1000));
        mMemo.setText(getSActivity().mMemo);
    }


    @Override
    public void onClick(View v) {
        if(v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            getSActivity().onDeleteStarName();
        }
    }


    private DeleteStarNameActivity getSActivity() {
        return (DeleteStarNameActivity)getBaseActivity();
    }
}

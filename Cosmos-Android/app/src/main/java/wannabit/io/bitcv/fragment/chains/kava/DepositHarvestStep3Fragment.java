package wannabit.io.bitcv.fragment.chains.kava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;

import wannabit.io.bitcv.activities.chains.kava.DepositHarvestActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseConstant.TOKEN_KAVA;

public class DepositHarvestStep3Fragment extends BaseFragment implements View.OnClickListener {

    private Button mBeforeBtn, mConfirmBtn;
    private TextView mDepositAmount, mDepositDenom;
    private TextView mFeesAmount, mFeesDenom;
    private TextView mDepositType, mMemo;

    public static DepositHarvestStep3Fragment newInstance(Bundle bundle) {
        DepositHarvestStep3Fragment fragment = new DepositHarvestStep3Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deposit_harvest_step3, container, false);
        mDepositAmount = rootView.findViewById(R.id.deposit_amount);
        mDepositDenom = rootView.findViewById(R.id.deposit_amount_denom);
        mFeesAmount = rootView.findViewById(R.id.fees_amount);
        mFeesDenom = rootView.findViewById(R.id.fees_denom);
        mDepositType = rootView.findViewById(R.id.deposit_type);
        mMemo = rootView.findViewById(R.id.memo);
        mBeforeBtn = rootView.findViewById(R.id.btn_before);
        mConfirmBtn = rootView.findViewById(R.id.btn_confirm);
        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        BigDecimal feeAmount = new BigDecimal(getSActivity().mFee.amount.get(0).amount);
        WDp.showCoinDp(getContext(), getSActivity().mHarvestCoin, mDepositDenom, mDepositAmount, getSActivity().mBaseChain);
        WDp.showCoinDp(getContext(), TOKEN_KAVA, feeAmount.toPlainString(), mFeesDenom, mFeesAmount, getSActivity().mBaseChain);
        mDepositType.setText("lp (Liquidity Provider)");
        mMemo.setText(getSActivity().mMemo);

    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            getSActivity().onStartDepositHarvest();

        }
    }

    private DepositHarvestActivity getSActivity() {
        return (DepositHarvestActivity)getBaseActivity();
    }
}
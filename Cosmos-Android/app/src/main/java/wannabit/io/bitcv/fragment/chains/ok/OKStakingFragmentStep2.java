package wannabit.io.bitcv.fragment.chains.ok;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import wannabit.io.bitcv.activities.chains.ok.OKStakingActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.model.type.Coin;
import wannabit.io.bitcv.model.type.Fee;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseChain.OK_TEST;
import static wannabit.io.bitcv.base.BaseConstant.FEE_OK_GAS_AMOUNT_STAKE_MUX;
import static wannabit.io.bitcv.base.BaseConstant.FEE_OK_GAS_RATE_AVERAGE;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_OK_TEST;

public class OKStakingFragmentStep2 extends BaseFragment implements View.OnClickListener {

    private RelativeLayout  mBtnGasType;
    private TextView        mTvGasType;

    private LinearLayout    mFeeLayer1;
    private TextView        mMinFeeAmount;
    private TextView        mMinFeePrice;

    private LinearLayout    mFeeLayer2;
    private TextView        mGasAmount;
    private TextView        mGasRate;
    private TextView        mGasFeeAmount;
    private TextView        mGasFeePrice;

    private LinearLayout    mFeeLayer3;
    private SeekBar         mSeekBarGas;

    private LinearLayout    mSpeedLayer;
    private ImageView       mSpeedImg;
    private TextView        mSpeedMsg;

    private Button mBeforeBtn, mNextBtn;

    private BigDecimal      mAvailable      = BigDecimal.ZERO;
    private BigDecimal      mFeeAmount      = BigDecimal.ZERO;
    private BigDecimal      mFeePrice       = BigDecimal.ZERO;
    private BigDecimal      mEstimateGasAmount  = BigDecimal.ZERO;


    public static OKStakingFragmentStep2 newInstance(Bundle bundle) {
        OKStakingFragmentStep2 fragment = new OKStakingFragmentStep2();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tx_step_fee, container, false);
        mBtnGasType     = rootView.findViewById(R.id.btn_gas_type);
        mTvGasType      = rootView.findViewById(R.id.gas_type);

        mFeeLayer1      = rootView.findViewById(R.id.fee_dp_layer1);
        mMinFeeAmount   = rootView.findViewById(R.id.min_fee_amount);
        mMinFeePrice    = rootView.findViewById(R.id.min_fee_price);

        mFeeLayer2      = rootView.findViewById(R.id.fee_dp_layer2);
        mGasAmount      = rootView.findViewById(R.id.gas_amount);
        mGasRate        = rootView.findViewById(R.id.gas_rate);
        mGasFeeAmount   = rootView.findViewById(R.id.gas_fee);
        mGasFeePrice    = rootView.findViewById(R.id.gas_fee_price);

        mFeeLayer3      = rootView.findViewById(R.id.fee_dp_layer3);
        mSeekBarGas     = rootView.findViewById(R.id.gas_price_seekbar);

        mSpeedLayer     = rootView.findViewById(R.id.speed_layer);
        mSpeedImg       = rootView.findViewById(R.id.speed_img);
        mSpeedMsg       = rootView.findViewById(R.id.speed_txt);

        mBeforeBtn      = rootView.findViewById(R.id.btn_before);
        mNextBtn        = rootView.findViewById(R.id.btn_next);
        mBeforeBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mTvGasType);

        if (getSActivity().mBaseChain.equals(OK_TEST)) {
            mFeeLayer1.setVisibility(View.GONE);
            mFeeLayer2.setVisibility(View.VISIBLE);
            mFeeLayer3.setVisibility(View.GONE);

            mSpeedImg.setImageDrawable(getResources().getDrawable(R.drawable.fee_img));
            mSpeedMsg.setText(getString(R.string.str_fee_speed_title_ok));

            int myValidatorCnt = 0;
            if (getBaseDao().mOkStaking != null && getBaseDao().mOkStaking.validator_address != null) {
                myValidatorCnt = getBaseDao().mOkStaking.validator_address.size();
            }
            mEstimateGasAmount = (new BigDecimal(FEE_OK_GAS_AMOUNT_STAKE_MUX).multiply(new BigDecimal(""+myValidatorCnt))).add(new BigDecimal(BaseConstant.FEE_OK_GAS_AMOUNT_STAKE));
            mGasAmount.setText(mEstimateGasAmount.toPlainString());
            mGasRate.setText(WDp.getDpString(FEE_OK_GAS_RATE_AVERAGE, 8));
            mFeeAmount = mEstimateGasAmount.multiply(new BigDecimal(FEE_OK_GAS_RATE_AVERAGE)).setScale(8);

            mGasFeeAmount.setText(mFeeAmount.toPlainString());
            mGasFeePrice.setText(WDp.getPriceApproximatelyDp(getSActivity(), BigDecimal.ZERO, getBaseDao().getCurrencySymbol(), getBaseDao().getCurrency()));

        }
        return rootView;
    }



    @Override
    public void onClick(View v) {
        if(v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mNextBtn)) {
            if (getSActivity().mBaseChain.equals(OK_TEST)) {
                Fee fee = new Fee();
                Coin gasCoin = new Coin();
                gasCoin.denom = TOKEN_OK_TEST;
                gasCoin.amount = mFeeAmount.toPlainString();
                ArrayList<Coin> amount = new ArrayList<>();
                amount.add(gasCoin);
                fee.amount = amount;
                fee.gas = mEstimateGasAmount.toPlainString();
                getSActivity().mFee = fee;

            }
            getSActivity().onNextStep();
        }
    }

    private OKStakingActivity getSActivity() {
        return (OKStakingActivity)getBaseActivity();
    }
}

package wannabit.io.bitcv.fragment.chains.ok;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;

import wannabit.io.bitcv.activities.chains.ok.OKVoteDirectActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.model.type.Validator;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseChain.OK_TEST;

public class DirectVoteFragment3 extends BaseFragment implements View.OnClickListener {

    private TextView            mFeeAmount;
    private TextView            mMemo;
    private Button              mBeforeBtn, mConfirmBtn;
    private TextView            mToVoteValidator, mFeeDenom;

    public static DirectVoteFragment3 newInstance(Bundle bundle) {
        DirectVoteFragment3 fragment = new DirectVoteFragment3();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_direct_vote_3, container, false);
        mFeeAmount          = rootView.findViewById(R.id.fees_amount);
        mFeeDenom           = rootView.findViewById(R.id.fees_denom);
        mToVoteValidator    = rootView.findViewById(R.id.to_vote_validators);
        mMemo               = rootView.findViewById(R.id.memo);
        mBeforeBtn          = rootView.findViewById(R.id.btn_before);
        mConfirmBtn         = rootView.findViewById(R.id.btn_confirm);

        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mFeeDenom);

        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onRefreshTab() {
        BigDecimal feeAmount = new BigDecimal(getSActivity().mFee.amount.get(0).amount);

        if (getSActivity().mBaseChain.equals(OK_TEST)) {
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 0, 8));

            String monikers = "";
            for (String valOp:getSActivity().mValAddesses) {
                for (Validator validator:getSActivity().mAllValidators) {
                    if (validator.operator_address.equals(valOp)) {
                        monikers = monikers + validator.description.moniker + "\n";
                    }
                }
            }
            mToVoteValidator.setText(monikers);

        }
        mMemo.setText(getSActivity().mMemo);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            getSActivity().onStartDirectVote();

        }
    }

    private OKVoteDirectActivity getSActivity() {
        return (OKVoteDirectActivity)getBaseActivity();
    }
}


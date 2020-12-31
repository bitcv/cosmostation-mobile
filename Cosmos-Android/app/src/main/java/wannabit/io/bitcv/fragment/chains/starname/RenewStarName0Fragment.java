package wannabit.io.bitcv.fragment.chains.starname;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;

import wannabit.io.bitcv.activities.chains.starname.ReNewStarNameActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseConstant.IOV_MSG_TYPE_RENEW_ACCOUNT;
import static wannabit.io.bitcv.base.BaseConstant.IOV_MSG_TYPE_RENEW_DOMAIN;

public class RenewStarName0Fragment extends BaseFragment implements View.OnClickListener {
    private Button mCancelBtn, mConfirmBtn;
    private TextView mStarName, mCurrentExpireTime, mToExpireTime, mStarnameFeeAmount;

    public static RenewStarName0Fragment newInstance(Bundle bundle) {
        RenewStarName0Fragment fragment = new RenewStarName0Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_renew_starname_0, container, false);
        mCancelBtn = rootView.findViewById(R.id.btn_cancel);
        mConfirmBtn = rootView.findViewById(R.id.btn_next);
        mStarName = rootView.findViewById(R.id.to_renew_starname);
        mCurrentExpireTime = rootView.findViewById(R.id.current_expire_time);
        mToExpireTime = rootView.findViewById(R.id.to_renew_expire_time);
        mStarnameFeeAmount  = rootView.findViewById(R.id.starname_fee_amount);
        mCancelBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);

        long extendTime = 0l;
        BigDecimal starnameFee = BigDecimal.ZERO;
        if (getSActivity().mRenewType.equals(IOV_MSG_TYPE_RENEW_DOMAIN)) {
            mStarName.setText( "*" + getSActivity().mToRenewDomain );
            extendTime = getBaseDao().mStarNameConfig.getRenewPeriod(IOV_MSG_TYPE_RENEW_DOMAIN);
            starnameFee = getBaseDao().mStarNameFee.getDomainRenewFee(getSActivity().mIsOpendomain);
        } else {
            mStarName.setText(  getSActivity().mToRenewAccount + "*" + getSActivity().mToRenewDomain );
            extendTime = getBaseDao().mStarNameConfig.getRenewPeriod(IOV_MSG_TYPE_RENEW_ACCOUNT);
            starnameFee = getBaseDao().mStarNameFee.getAccountRenewFee(getSActivity().mIsOpendomain);
        }
        mCurrentExpireTime.setText(WDp.getDpTime(getContext(), getSActivity().mValidTime * 1000));
        mToExpireTime.setText(WDp.getDpTime(getContext(), (getSActivity().mValidTime * 1000) + extendTime));
        mStarnameFeeAmount.setText(WDp.getDpAmount2(getContext(), starnameFee, 6, 6));
        return rootView;
    }


    private ReNewStarNameActivity getSActivity() {
        return (ReNewStarNameActivity)getBaseActivity();
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mCancelBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            getSActivity().onNextStep();
        }

    }
}

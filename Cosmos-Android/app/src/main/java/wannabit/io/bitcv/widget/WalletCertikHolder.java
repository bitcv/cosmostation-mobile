package wannabit.io.bitcv.widget;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import wannabit.io.bitcv.activities.MainActivity;
import wannabit.io.bitcv.activities.ValidatorListActivity;
import wannabit.io.bitcv.activities.VoteListActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseConstant.TOKEN_CERTIK;

public class WalletCertikHolder extends WalletHolder {
    private TextView        mTvCertikTotal, mTvCertikValue, mTvCertikAvailable, mTvCertikDelegated, mTvCertikUnBonding, mTvCertikRewards;
    private RelativeLayout  mBtnCertikStake, mBtnCertikVote;

    public WalletCertikHolder(@NonNull View itemView) {
        super(itemView);
        mTvCertikTotal      = itemView.findViewById(R.id.certik_amount);
        mTvCertikValue      = itemView.findViewById(R.id.certik_value);
        mTvCertikAvailable  = itemView.findViewById(R.id.certik_available);
        mTvCertikDelegated  = itemView.findViewById(R.id.certik_delegate);
        mTvCertikUnBonding  = itemView.findViewById(R.id.certik_unbonding);
        mTvCertikRewards    = itemView.findViewById(R.id.certik_reward);
        mBtnCertikStake     = itemView.findViewById(R.id.btn_certik_reward);
        mBtnCertikVote      = itemView.findViewById(R.id.btn_certik_vote);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final BigDecimal availableAmount = WDp.getAvailableCoin(mainActivity.mBalances, TOKEN_CERTIK);
        final BigDecimal delegateAmount = WDp.getAllDelegatedAmount(mainActivity.mBondings, mainActivity.mAllValidators, mainActivity.mBaseChain);
        final BigDecimal unbondingAmount = WDp.getUnbondingAmount(mainActivity.mUnbondings);
        final BigDecimal rewardAmount = WDp.getAllRewardAmount(mainActivity.mRewards, TOKEN_CERTIK);
        final BigDecimal totalAmount = availableAmount.add(delegateAmount).add(unbondingAmount).add(rewardAmount);

        mTvCertikTotal.setText(WDp.getDpAmount2(mainActivity, totalAmount, 6, 6));
        mTvCertikAvailable.setText(WDp.getDpAmount2(mainActivity, availableAmount, 6, 6));
        mTvCertikDelegated.setText(WDp.getDpAmount2(mainActivity, delegateAmount, 6, 6));
        mTvCertikUnBonding.setText(WDp.getDpAmount2(mainActivity, unbondingAmount, 6, 6));
        mTvCertikRewards.setText(WDp.getDpAmount2(mainActivity, rewardAmount, 6, 6));
        mTvCertikValue.setText(WDp.getValueOfCertik(mainActivity, mainActivity.getBaseDao(), totalAmount));

        mainActivity.getBaseDao().onUpdateLastTotalAccount(mainActivity.mAccount, totalAmount.toPlainString());

        mBtnCertikStake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent validators = new Intent(mainActivity, ValidatorListActivity.class);
                validators.putExtra("rewards", mainActivity.mRewards);
                mainActivity.startActivity(validators);
            }
        });
        mBtnCertikVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proposals = new Intent(mainActivity, VoteListActivity.class);
                mainActivity.startActivity(proposals);
            }
        });

    }
}

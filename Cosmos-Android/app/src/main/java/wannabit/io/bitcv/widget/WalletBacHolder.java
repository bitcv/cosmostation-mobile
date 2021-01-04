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

import static wannabit.io.bitcv.base.BaseConstant.TOKEN_IRIS_ATTO;

public class WalletBacHolder extends WalletHolder {
    public TextView mTvBacTotal, mTvBacValue, mTvBacAvailable, mTvBacDelegated, mTvBacUnBonding, mTvBacRewards;
    public RelativeLayout mBtnStake, mBtnVote;

    public WalletBacHolder(@NonNull View itemView) {
        super(itemView);
        mTvBacTotal        = itemView.findViewById(R.id.bac_amount);
        mTvBacValue        = itemView.findViewById(R.id.bac_value);
        mTvBacAvailable    = itemView.findViewById(R.id.bac_available);
        mTvBacDelegated    = itemView.findViewById(R.id.bac_delegate);
        mTvBacUnBonding    = itemView.findViewById(R.id.bac_unbonding);
        mTvBacRewards      = itemView.findViewById(R.id.bac_reward);
        mBtnStake           = itemView.findViewById(R.id.btn_bac_reward);
        mBtnVote            = itemView.findViewById(R.id.btn_bac_vote);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final BigDecimal availableAmount = WDp.getAvailableCoin(mainActivity.mBalances, TOKEN_IRIS_ATTO);
        final BigDecimal delegateAmount = WDp.getAllDelegatedAmount(mainActivity.mBondings, mainActivity.mAllValidators, mainActivity.mBaseChain);
        final BigDecimal unbondingAmount = WDp.getUnbondingAmount(mainActivity.mUnbondings);
        final BigDecimal rewardAmount =  BigDecimal.ZERO ;
        final BigDecimal totalAmount = availableAmount.add(delegateAmount).add(unbondingAmount).add(rewardAmount);

        mTvBacTotal.setText(WDp.getDpAmount2(mainActivity, totalAmount, 18, 6));
        mTvBacAvailable.setText(WDp.getDpAmount2(mainActivity, availableAmount, 18, 6));
        mTvBacDelegated.setText(WDp.getDpAmount2(mainActivity, delegateAmount, 18, 6));
        mTvBacUnBonding.setText(WDp.getDpAmount2(mainActivity, unbondingAmount, 18, 6));
        mTvBacRewards.setText(WDp.getDpAmount2(mainActivity, rewardAmount, 18, 6));
      //  mTvBacValue.setText(WDp.getValueOfBac(mainActivity, mainActivity.getBaseDao(), totalAmount));

        mainActivity.getBaseDao().onUpdateLastTotalAccount(mainActivity.mAccount, totalAmount.toPlainString());

        mBtnStake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent validators = new Intent(mainActivity, ValidatorListActivity.class);
                validators.putExtra("bacreward", BigDecimal.ZERO);
                mainActivity.startActivity(validators);
            }
        });
        mBtnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proposals = new Intent(mainActivity, VoteListActivity.class);
                mainActivity.startActivity(proposals);
            }
        });
    }
}

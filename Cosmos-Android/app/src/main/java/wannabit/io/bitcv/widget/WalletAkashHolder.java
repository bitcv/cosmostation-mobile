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

import static wannabit.io.bitcv.base.BaseConstant.TOKEN_AKASH;

public class WalletAkashHolder extends WalletHolder {
    private TextView        mTvAkashTotal, mTvAkashValue, mTvAkashAvailable, mTvAkashDelegated, mTvAkashUnBonding, mTvAkashRewards;
    private RelativeLayout  mBtnAkashStake, mBtnAkashVote;

    public WalletAkashHolder(@NonNull View itemView) {
        super(itemView);
        mTvAkashTotal       = itemView.findViewById(R.id.akash_amount);
        mTvAkashValue       = itemView.findViewById(R.id.akash_value);
        mTvAkashAvailable   = itemView.findViewById(R.id.akash_available);
        mTvAkashDelegated   = itemView.findViewById(R.id.akash_delegate);
        mTvAkashUnBonding   = itemView.findViewById(R.id.akash_unbonding);
        mTvAkashRewards     = itemView.findViewById(R.id.akash_reward);
        mBtnAkashStake      = itemView.findViewById(R.id.btn_akash_reward);
        mBtnAkashVote       = itemView.findViewById(R.id.btn_akash_vote);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final BigDecimal availableAmount = WDp.getAvailableCoin(mainActivity.mBalances, TOKEN_AKASH);
        final BigDecimal delegateAmount = WDp.getAllDelegatedAmount(mainActivity.mBondings, mainActivity.mAllValidators, mainActivity.mBaseChain);
        final BigDecimal unbondingAmount = WDp.getUnbondingAmount(mainActivity.mUnbondings);
        final BigDecimal rewardAmount = WDp.getAllRewardAmount(mainActivity.mRewards, TOKEN_AKASH);
        final BigDecimal totalAmount = availableAmount.add(delegateAmount).add(unbondingAmount).add(rewardAmount);

        mTvAkashTotal.setText(WDp.getDpAmount2(mainActivity, totalAmount, 6, 6));
        mTvAkashAvailable.setText(WDp.getDpAmount2(mainActivity, availableAmount, 6, 6));
        mTvAkashDelegated.setText(WDp.getDpAmount2(mainActivity, delegateAmount, 6, 6));
        mTvAkashUnBonding.setText(WDp.getDpAmount2(mainActivity, unbondingAmount, 6, 6));
        mTvAkashRewards.setText(WDp.getDpAmount2(mainActivity, rewardAmount, 6, 6));
        mTvAkashValue.setText(WDp.getValueOfAkash(mainActivity, mainActivity.getBaseDao(), totalAmount));

        mainActivity.getBaseDao().onUpdateLastTotalAccount(mainActivity.mAccount, totalAmount.toPlainString());

        mBtnAkashStake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent validators = new Intent(mainActivity, ValidatorListActivity.class);
                validators.putExtra("rewards", mainActivity.mRewards);
                mainActivity.startActivity(validators);
            }
        });
        mBtnAkashVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proposals = new Intent(mainActivity, VoteListActivity.class);
                mainActivity.startActivity(proposals);
            }
        });

    }
}

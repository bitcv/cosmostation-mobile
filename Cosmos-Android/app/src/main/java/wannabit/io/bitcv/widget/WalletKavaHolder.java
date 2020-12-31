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
import wannabit.io.bitcv.activities.chains.kava.DAppsListActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseConstant.TOKEN_KAVA;

public class WalletKavaHolder extends WalletHolder {
    public TextView         mTvKavaTotal, mTvKavaValue, mTvKavaAvailable, mTvKavaDelegated, mTvKavaUnBonding, mTvKavaRewards;
    public RelativeLayout   mKavaVestingLayer, mKavaDepositLayer, mKavaIncentiveLayer;
    public TextView         mTvKavaVesting, mTvKavaDeposit, mTvKavaIncentive;
    public RelativeLayout   mBtnStake, mBtnVote, mBtnKavaDapp;

    public WalletKavaHolder(@NonNull View itemView) {
        super(itemView);
        mTvKavaTotal        = itemView.findViewById(R.id.kava_amount);
        mTvKavaValue        = itemView.findViewById(R.id.kava_value);
        mTvKavaAvailable    = itemView.findViewById(R.id.kava_available);
        mTvKavaDelegated    = itemView.findViewById(R.id.kava_delegate);
        mTvKavaUnBonding    = itemView.findViewById(R.id.kava_unbonding);
        mTvKavaRewards      = itemView.findViewById(R.id.kava_reward);

        mKavaVestingLayer   = itemView.findViewById(R.id.kava_vesting_layer);
        mKavaDepositLayer   = itemView.findViewById(R.id.kava_deposit_layer);
        mKavaIncentiveLayer = itemView.findViewById(R.id.kava_incentive_layer);
        mTvKavaVesting      = itemView.findViewById(R.id.kava_vesting);
        mTvKavaDeposit      = itemView.findViewById(R.id.kava_harvest_deposited);
        mTvKavaIncentive    = itemView.findViewById(R.id.kava_unclaimed_incentive);

        mBtnStake           = itemView.findViewById(R.id.btn_kava_reward);
        mBtnVote            = itemView.findViewById(R.id.btn_kava_vote);
        mBtnKavaDapp        = itemView.findViewById(R.id.btn_kava_dapp);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        BigDecimal availableAmount = WDp.getAvailableCoin(mainActivity.mBalances, TOKEN_KAVA);
        BigDecimal delegateAmount = WDp.getAllDelegatedAmount(mainActivity.mBondings, mainActivity.mAllValidators, mainActivity.mBaseChain);
        BigDecimal unbondingAmount = WDp.getUnbondingAmount(mainActivity.mUnbondings);
        BigDecimal rewardAmount = WDp.getAllRewardAmount(mainActivity.mRewards, TOKEN_KAVA);
        BigDecimal vestingAmount = WDp.getLockedCoin(mainActivity.mBalances, TOKEN_KAVA);
        BigDecimal harvestDepositAmount = WDp.getHavestDepositAmount(mainActivity.getBaseDao(), TOKEN_KAVA);
        BigDecimal unclaimedIncentiveAmount = WDp.getUnclaimedIncentiveAmount(mainActivity.getBaseDao(), TOKEN_KAVA);
        BigDecimal totalAmount = WDp.getAllKava(mainActivity.getBaseDao(), mainActivity.mBalances, mainActivity.mBondings, mainActivity.mUnbondings, mainActivity.mRewards, mainActivity.mAllValidators);

        mTvKavaTotal.setText(WDp.getDpAmount2(mainActivity, totalAmount, 6, 6));
        mTvKavaAvailable.setText(WDp.getDpAmount2(mainActivity, availableAmount, 6, 6));
        mTvKavaDelegated.setText(WDp.getDpAmount2(mainActivity, delegateAmount, 6, 6));
        mTvKavaUnBonding.setText(WDp.getDpAmount2(mainActivity, unbondingAmount, 6, 6));
        mTvKavaRewards.setText(WDp.getDpAmount2(mainActivity, rewardAmount, 6, 6));
        mTvKavaVesting.setText(WDp.getDpAmount2(mainActivity, vestingAmount, 6, 6));
        mTvKavaDeposit.setText(WDp.getDpAmount2(mainActivity, harvestDepositAmount, 6, 6));
        mTvKavaIncentive.setText(WDp.getDpAmount2(mainActivity, unclaimedIncentiveAmount, 6, 6));
        mTvKavaValue.setText(WDp.getValueOfKava(mainActivity, mainActivity.getBaseDao(), totalAmount));

        if (!vestingAmount.equals(BigDecimal.ZERO)) { mKavaVestingLayer.setVisibility(View.VISIBLE);
        } else { mKavaVestingLayer.setVisibility(View.GONE); }
        if (!harvestDepositAmount.equals(BigDecimal.ZERO)) { mKavaDepositLayer.setVisibility(View.VISIBLE);
        } else { mKavaDepositLayer.setVisibility(View.GONE); }
        if (!unclaimedIncentiveAmount.equals(BigDecimal.ZERO)) { mKavaIncentiveLayer.setVisibility(View.VISIBLE);
        } else { mKavaIncentiveLayer.setVisibility(View.GONE); }

        mainActivity.getBaseDao().onUpdateLastTotalAccount(mainActivity.mAccount, totalAmount.toPlainString());

        mBtnStake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent validators = new Intent(mainActivity, ValidatorListActivity.class);
                validators.putExtra("rewards", mainActivity.mRewards);
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
        mBtnKavaDapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.startActivity(new Intent(mainActivity, DAppsListActivity.class));
            }
        });
    }
}

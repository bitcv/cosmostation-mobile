package wannabit.io.bitcv.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import wannabit.io.bitcv.activities.MainActivity;
import wannabit.io.bitcv.activities.SendActivity;
import wannabit.io.bitcv.activities.TokenDetailActivity;
import wannabit.io.bitcv.activities.ValidatorListActivity;
import wannabit.io.bitcv.activities.VoteListActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.dao.BacToken;
import wannabit.io.bitcv.dialog.Dialog_AccountShow;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseConstant.BAC_MAIN_DENOM;
import static wannabit.io.bitcv.base.BaseConstant.BAC_TOKEN_DECIMAL;
import static wannabit.io.bitcv.base.BaseConstant.BAC_TOKEN_SYMBOL;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_IRIS_ATTO;

public class WalletBacHolder extends WalletHolder {
    public TextView mTvBacTotal, mTvBacValue, mTvBacAvailable, mTvBacDelegated, mTvBacUnBonding, mTvBacRewards;
    public RelativeLayout mBtnReceive, mBtnSend;

    public WalletBacHolder(@NonNull View itemView) {
        super(itemView);
        mTvBacTotal        = itemView.findViewById(R.id.bac_amount);
        mTvBacValue        = itemView.findViewById(R.id.bac_value);
        mTvBacAvailable    = itemView.findViewById(R.id.bac_available);
        mTvBacDelegated    = itemView.findViewById(R.id.bac_delegate);
        mTvBacUnBonding    = itemView.findViewById(R.id.bac_unbonding);
        mTvBacRewards      = itemView.findViewById(R.id.bac_reward);
        mBtnReceive           = itemView.findViewById(R.id.btn_bac_receive);
        mBtnSend            = itemView.findViewById(R.id.btn_bac_send);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final BigDecimal availableAmount = WDp.getAvailableCoin(mainActivity.mBalances, BAC_MAIN_DENOM);
        final BigDecimal delegateAmount = WDp.getAllDelegatedAmount(mainActivity.mBondings, mainActivity.mAllValidators, mainActivity.mBaseChain);
        final BigDecimal unbondingAmount = WDp.getUnbondingAmount(mainActivity.mUnbondings);
        final BigDecimal rewardAmount =  BigDecimal.ZERO ;
        final BigDecimal totalAmount = availableAmount.add(delegateAmount).add(unbondingAmount).add(rewardAmount);

        mTvBacTotal.setText(WDp.getDpAmount2(mainActivity, totalAmount, BAC_TOKEN_DECIMAL, 6));
        mTvBacAvailable.setText(WDp.getDpAmount2(mainActivity, availableAmount, BAC_TOKEN_DECIMAL, 6));
        mTvBacDelegated.setText(WDp.getDpAmount2(mainActivity, delegateAmount, BAC_TOKEN_DECIMAL, 6));
        mTvBacUnBonding.setText(WDp.getDpAmount2(mainActivity, unbondingAmount, BAC_TOKEN_DECIMAL, 6));
        mTvBacRewards.setText(WDp.getDpAmount2(mainActivity, rewardAmount, BAC_TOKEN_DECIMAL, 6));
      //  mTvBacValue.setText(WDp.getValueOfBac(mainActivity, mainActivity.getBaseDao(), totalAmount));

        mainActivity.getBaseDao().onUpdateLastTotalAccount(mainActivity.mAccount, totalAmount.toPlainString());

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent validators = new Intent(mainActivity, ValidatorListActivity.class);
//                validators.putExtra("bacreward", BigDecimal.ZERO);
//                mainActivity.startActivity(validators);
//                Intent intent = new Intent(mainActivity, SendActivity.class);
//                intent.putExtra("bacToken", new BacToken(BAC_TOKEN_SYMBOL));
//                mainActivity.startActivity(intent);
                  mainActivity.onStartSendActivity();
            }
        });
        mBtnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent proposals = new Intent(mainActivity, VoteListActivity.class);
//                mainActivity.startActivity(proposals);

                Bundle bundle = new Bundle();
                bundle.putString("address", mainActivity.mAccount.address);
                if (TextUtils.isEmpty(mainActivity.mAccount.nickName))
                    bundle.putString("title", mainActivity.getString(R.string.str_my_wallet) + mainActivity.mAccount.id);
                else
                    bundle.putString("title", mainActivity.mAccount.nickName);
                Dialog_AccountShow show = Dialog_AccountShow.newInstance(bundle);
                show.setCancelable(true);
                mainActivity.getSupportFragmentManager().beginTransaction().add(show, "dialog").commitNowAllowingStateLoss();
            }
        });
    }
}

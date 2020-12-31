package wannabit.io.bitcv.widget;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;

import wannabit.io.bitcv.activities.MainActivity;
import wannabit.io.bitcv.activities.chains.ok.OKStakingActivity;
import wannabit.io.bitcv.activities.chains.ok.OKUnbondingActivity;
import wannabit.io.bitcv.activities.chains.ok.OKValidatorListActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.dao.Balance;
import wannabit.io.bitcv.dialog.Dialog_WatchMode;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseConstant.TOKEN_OK_TEST;

public class WalletOkexHolder extends WalletHolder {
    private TextView            mOkTotalAmount, mOkTotalValue, mOkAvailable, mOkLocked, mOkDeposit, mOkWithdrawing;
    private RelativeLayout      mBtnOkDeposit, mBtnOkWithdraw, mBtnOkVote;

    public WalletOkexHolder(@NonNull View itemView) {
        super(itemView);
        mOkTotalAmount      = itemView.findViewById(R.id.ok_total_amount);
        mOkTotalValue       = itemView.findViewById(R.id.ok_total_value);
        mOkAvailable        = itemView.findViewById(R.id.ok_available);
        mOkLocked           = itemView.findViewById(R.id.ok_locked);
        mOkDeposit          = itemView.findViewById(R.id.ok_deposit);
        mOkWithdrawing      = itemView.findViewById(R.id.ok_withdrawing);
        mBtnOkDeposit       = itemView.findViewById(R.id.btn_ok_deposit);
        mBtnOkWithdraw      = itemView.findViewById(R.id.btn_ok_withdraw);
        mBtnOkVote          = itemView.findViewById(R.id.btn_ok_vote);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        BigDecimal availableAmount = WDp.getAvailableCoin(mainActivity.mBalances, TOKEN_OK_TEST);
        BigDecimal lockedAmount = WDp.getLockedCoin(mainActivity.mBalances, TOKEN_OK_TEST);
        BigDecimal depositAmount = WDp.getOkDepositCoin(mainActivity.getBaseDao().mOkStaking);
        BigDecimal withdrawAmount = WDp.getOkWithdrawingCoin(mainActivity.getBaseDao().mOkUnbonding);
        BigDecimal totalAmount = availableAmount.add(lockedAmount).add(depositAmount).add(withdrawAmount);

        mOkTotalAmount.setText(WDp.getDpAmount2(mainActivity, totalAmount, 0, 6));
        mOkAvailable.setText(WDp.getDpAmount2(mainActivity, availableAmount, 0, 6));
        mOkLocked.setText(WDp.getDpAmount2(mainActivity, lockedAmount, 0, 6));
        mOkDeposit.setText(WDp.getDpAmount2(mainActivity, depositAmount, 0, 6));
        mOkWithdrawing.setText(WDp.getDpAmount2(mainActivity, withdrawAmount, 0, 6));
        mOkTotalValue.setText(WDp.getValueOfOk(mainActivity, mainActivity.getBaseDao(), totalAmount));

        mainActivity.getBaseDao().onUpdateLastTotalAccount(mainActivity.mAccount, totalAmount.toPlainString());

        mBtnOkDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.getBaseDao().mTopValidators == null && mainActivity.getBaseDao().mTopValidators.size() == 0) return;
                if (!mainActivity.mAccount.hasPrivateKey) {
                    Dialog_WatchMode add = Dialog_WatchMode.newInstance();
                    add.setCancelable(true);
                    mainActivity.getSupportFragmentManager().beginTransaction().add(add, "dialog").commitNowAllowingStateLoss();
                    return;
                }
                ArrayList<Balance> balances = mainActivity.getBaseDao().onSelectBalance(mainActivity.mAccount.id);
                boolean hasbalance = false;
                if (WDp.getAvailableCoin(balances, TOKEN_OK_TEST).compareTo(BigDecimal.ONE) > 0) {
                    hasbalance  = true;
                }
                if (!hasbalance) {
                    Toast.makeText(mainActivity, R.string.error_not_enough_to_deposit, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(mainActivity, OKStakingActivity.class);
                mainActivity.startActivity(intent);
            }
        });
        mBtnOkWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.getBaseDao().mTopValidators == null && mainActivity.getBaseDao().mTopValidators.size() == 0) return;
                if (!mainActivity.mAccount.hasPrivateKey) {
                    Dialog_WatchMode add = Dialog_WatchMode.newInstance();
                    add.setCancelable(true);
                    mainActivity.getSupportFragmentManager().beginTransaction().add(add, "dialog").commitNowAllowingStateLoss();
                    return;
                }
                ArrayList<Balance> balances = mainActivity.getBaseDao().onSelectBalance(mainActivity.mAccount.id);
                boolean hasbalance = false;
                if (WDp.getAvailableCoin(balances, TOKEN_OK_TEST).compareTo(BigDecimal.ONE) > 0) {
                    hasbalance  = true;
                }
                if (!hasbalance) {
                    Toast.makeText(mainActivity, R.string.error_not_enough_to_deposit, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (WDp.getOkDepositCoin(mainActivity.getBaseDao().mOkStaking).compareTo(BigDecimal.ZERO) <= 0) {
                    Toast.makeText(mainActivity, R.string.error_not_enough_to_withdraw, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(mainActivity, OKUnbondingActivity.class);
                mainActivity.startActivity(intent);
            }
        });
        mBtnOkVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, OKValidatorListActivity.class);
                mainActivity.startActivity(intent);
            }
        });
    }
}

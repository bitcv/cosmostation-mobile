package wannabit.io.bitcv.widget;

import android.Manifest;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;

import wannabit.io.bitcv.activities.MainActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.dialog.Dialog_WatchMode;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseChain.BNB_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BNB_TEST;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_BNB;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_HTLC_BINANCE_BNB;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_HTLC_BINANCE_TEST_BNB;

public class WalletBinanceHolder extends WalletHolder {
    public TextView         mTvBnbTotal, mTvBnbValue, mTvBnbBalance, mTvBnbLocked;
    public RelativeLayout   mBtnWalletConnect, mBtnBep3Send;

    public WalletBinanceHolder(@NonNull View itemView) {
        super(itemView);
        mTvBnbTotal         = itemView.findViewById(R.id.bnb_amount);
        mTvBnbValue         = itemView.findViewById(R.id.bnb_value);
        mTvBnbBalance       = itemView.findViewById(R.id.bnb_available);
        mTvBnbLocked        = itemView.findViewById(R.id.bnb_locked);
        mBtnWalletConnect   = itemView.findViewById(R.id.btn_wallet_connect);
        mBtnBep3Send        = itemView.findViewById(R.id.btn_bep3_send);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final BigDecimal availableAmount    = WDp.getAvailableCoin(mainActivity.mBalances, TOKEN_BNB);
        final BigDecimal lockedAmount       = WDp.getLockedCoin(mainActivity.mBalances, TOKEN_BNB);
        final BigDecimal totalAmount        = availableAmount.add(lockedAmount);

        mTvBnbTotal.setText(WDp.getDpAmount2(mainActivity, totalAmount, 0, 6));
        mTvBnbBalance.setText(WDp.getDpAmount2(mainActivity, availableAmount, 0, 6));
        mTvBnbLocked.setText(WDp.getDpAmount2(mainActivity, lockedAmount, 0, 6));
        mTvBnbValue.setText(WDp.getValueOfBnb(mainActivity, mainActivity.getBaseDao(), totalAmount));

        mainActivity.getBaseDao().onUpdateLastTotalAccount(mainActivity.mAccount, totalAmount.toPlainString());

        mBtnWalletConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mainActivity.mAccount.hasPrivateKey) {
                    Dialog_WatchMode dialog = Dialog_WatchMode.newInstance();
                    dialog.setCancelable(true);
                    mainActivity.getSupportFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();
                    return;
                }
                new TedPermission(mainActivity).setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        IntentIntegrator integrator = new IntentIntegrator(mainActivity);
                        integrator.setOrientationLocked(true);
                        integrator.initiateScan();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(mainActivity, R.string.error_permission, Toast.LENGTH_SHORT).show();
                    }
                })
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleMessage(mainActivity.getString(R.string.str_permission_qr))
                .check();
            }
        });
        mBtnBep3Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.mBaseChain.equals(BNB_MAIN)) {
                    mainActivity.onStartHTLCSendActivity(TOKEN_HTLC_BINANCE_BNB);
                } else if (mainActivity.mBaseChain.equals(BNB_TEST)) {
                    mainActivity.onStartHTLCSendActivity(TOKEN_HTLC_BINANCE_TEST_BNB);
                }
            }
        });
    }
}


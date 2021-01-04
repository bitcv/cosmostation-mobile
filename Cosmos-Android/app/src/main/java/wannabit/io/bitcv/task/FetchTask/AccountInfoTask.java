package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResBnbAccountInfo;
import wannabit.io.bitcv.network.res.ResLcdAccountInfo;
import wannabit.io.bitcv.network.res.ResLcdKavaAccountInfo;
import wannabit.io.bitcv.network.res.ResOkAccountInfo;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.utils.WUtil;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

import static wannabit.io.bitcv.base.BaseChain.AKASH_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BAC_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BAND_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BNB_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BNB_TEST;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_MAIN;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_TEST;
import static wannabit.io.bitcv.base.BaseChain.COSMOS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_TEST;
import static wannabit.io.bitcv.base.BaseChain.IRIS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_TEST;
import static wannabit.io.bitcv.base.BaseChain.OK_TEST;
import static wannabit.io.bitcv.base.BaseChain.SECRET_MAIN;

public class AccountInfoTask extends CommonTask {

    private Account mAccount;

    public AccountInfoTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mAccount           = account;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_ACCOUNT;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (BaseChain.getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getCosmosChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }
            } else if (BaseChain.getChain(mAccount.baseChain).equals(BAC_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getBacChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(IRIS_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getIrisChain(mApp).getBankInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BNB_MAIN)) {
                Response<ResBnbAccountInfo> response = ApiClient.getBnbChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromBnbLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromBnbLcd(mAccount.id, response.body()));
                } else {
                    mApp.getBaseDao().onDeleteBalance(""+mAccount.id);
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaChain(mApp).getAccountInfo(mAccount.address).execute();
                if (response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().mKavaAccount = response.body().result;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getBandChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BNB_TEST)) {
                Response<ResBnbAccountInfo> response = ApiClient.getBnbTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromBnbLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromBnbLcd(mAccount.id, response.body()));
                } else {
                    mApp.getBaseDao().onDeleteBalance(""+mAccount.id);
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if (response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().mKavaAccount = response.body().result;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getIovChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdAccountInfo> response = ApiClient.getIovTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(OK_TEST)) {
                Response<ResOkAccountInfo> response = ApiClient.getOkTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if (response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromOkLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().mOkAccountInfo = response.body();
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(CERTIK_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getCertikChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(CERTIK_TEST)) {
                Response<ResLcdAccountInfo> response = ApiClient.getCertikTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(SECRET_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getSecretChain(mApp).getAccountInfo(mAccount.address).execute();
                if (response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(AKASH_MAIN)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getAkashChain(mApp).getAccountInfo(mAccount.address).execute();
                if (response.isSuccessful()) {
                    mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                    mApp.getBaseDao().mKavaAccount = response.body().result;
                }

            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("AccountInfoTask Error " + e.getMessage());
            mApp.getBaseDao().onDeleteBalance(""+mAccount.id);

        }
        return mResult;
    }
}

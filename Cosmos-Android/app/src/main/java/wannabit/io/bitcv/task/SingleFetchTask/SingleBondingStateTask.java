package wannabit.io.bitcv.task.SingleFetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResLcdBonding;
import wannabit.io.bitcv.network.res.ResLcdSingleBonding;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.utils.WUtil;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

import static wannabit.io.bitcv.base.BaseChain.AKASH_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BAND_MAIN;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_MAIN;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_TEST;
import static wannabit.io.bitcv.base.BaseChain.COSMOS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_TEST;
import static wannabit.io.bitcv.base.BaseChain.IRIS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_TEST;
import static wannabit.io.bitcv.base.BaseChain.SECRET_MAIN;
import static wannabit.io.bitcv.base.BaseChain.getChain;

public class SingleBondingStateTask extends CommonTask {

    private Account mAccount;
    private String  mValidatorAddr;

    public SingleBondingStateTask(BaseApplication app, TaskListener listener, Account account, String validatorAddr) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_SINGLE_BONDING;
        this.mAccount = account;
        this.mValidatorAddr = validatorAddr;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (getChain(mAccount.baseChain).equals(COSMOS_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getCosmosChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, COSMOS_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(IRIS_MAIN)) {
                Response<ResLcdBonding> response = ApiClient.getIrisChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null){
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body(), IRIS_MAIN));
                    } else {
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                    }

                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(KAVA_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getKavaChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, KAVA_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(KAVA_TEST)) {
                Response<ResLcdSingleBonding> response = ApiClient.getKavaTestChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, KAVA_TEST));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(BAND_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getBandChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, BAND_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getIovChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, IOV_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdSingleBonding> response = ApiClient.getIovTestChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, IOV_TEST));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(CERTIK_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getCertikChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, CERTIK_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(CERTIK_TEST)) {
                Response<ResLcdSingleBonding> response = ApiClient.getCertikTestChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, CERTIK_TEST));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(SECRET_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getSecretChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, SECRET_MAIN));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            } else if (getChain(mAccount.baseChain).equals(AKASH_MAIN)) {
                Response<ResLcdSingleBonding> response = ApiClient.getAkashChain(mApp).getBonding(mAccount.address, mValidatorAddr).execute();
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().result != null)
                        mApp.getBaseDao().onUpdateBondingState(mAccount.id, WUtil.getBondingFromLcd(mAccount.id, response.body().result, CERTIK_TEST));
                    else
                        mApp.getBaseDao().onDeleteBondingStates(mAccount.id);
                }
                mResult.isSuccess = true;

            }



        } catch (Exception e) {
            WLog.w("SingleBondingStateTask Error " + e.getMessage());
        }
        return mResult;
    }
}

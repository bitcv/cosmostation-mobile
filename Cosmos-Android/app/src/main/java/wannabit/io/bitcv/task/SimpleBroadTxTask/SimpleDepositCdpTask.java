package wannabit.io.bitcv.task.SimpleBroadTxTask;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.ArrayList;

import retrofit2.Response;
import wannabit.io.bitcv.cosmos.MsgGenerator;
import wannabit.io.bitcv.crypto.CryptoHelper;
import wannabit.io.bitcv.model.type.Coin;
import wannabit.io.bitcv.model.type.Fee;
import wannabit.io.bitcv.model.type.Msg;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.req.ReqBroadCast;
import wannabit.io.bitcv.network.res.ResBroadTx;
import wannabit.io.bitcv.network.res.ResLcdKavaAccountInfo;
import wannabit.io.bitcv.utils.WKey;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.utils.WUtil;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.dao.Password;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

import static wannabit.io.bitcv.base.BaseConstant.TASK_GEN_TX_DEPOSIT_CDP;

public class SimpleDepositCdpTask extends CommonTask {

    private Account         mAccount;
    private Coin mCollateral;
    private String          mOwner;
    private String          mDepositor;
    private String          mMemo;
    private Fee mFees;
    private String          mCollateralType;

    public SimpleDepositCdpTask(BaseApplication app, TaskListener listener,
                                Account account, String owner, String depositor,
                                Coin collateral, String memo, Fee fees, String collateralType) {
        super(app, listener);
        this.mAccount = account;
        this.mCollateral = collateral;
        this.mOwner = owner;
        this.mDepositor = depositor;
        this.mMemo = memo;
        this.mFees = fees;
        this.mCollateralType = collateralType;
        this.mResult.taskType   = TASK_GEN_TX_DEPOSIT_CDP;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Password checkPw = mApp.getBaseDao().onSelectPassword();
            if(!CryptoHelper.verifyData(strings[0], checkPw.resource, mApp.getString(R.string.key_password))) {
                mResult.isSuccess = false;
                mResult.errorCode = BaseConstant.ERROR_CODE_INVALID_PASSWORD;
                return mResult;
            }

            if (!mAccount.address.equals(mDepositor)) {
                mResult.isSuccess = false;
                return mResult;
            }

            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_TEST)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            }

            String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
            DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mAccount.baseChain), entropy, Integer.parseInt(mAccount.path), mAccount.newBip44);

            Msg depositCdpMsg = MsgGenerator.genDepositCdpMsg(mOwner, mCollateral, mDepositor, mCollateralType, BaseChain.getChain(mAccount.baseChain));
            ArrayList<Msg> msgs= new ArrayList<>();
            msgs.add(depositCdpMsg);

            WLog.w("depositCdpMsg : " +  WUtil.prettyPrinter(depositCdpMsg));

            ReqBroadCast reqBroadCast = MsgGenerator.getBraodcaseReq(mAccount, msgs, mFees, mMemo, deterministicKey);
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getKavaChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_TEST)) {
                Response<ResBroadTx> response = ApiClient.getKavaTestChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }
            }

        } catch (Exception e) {
            if(BaseConstant.IS_SHOWLOG) e.printStackTrace();
        }
        return mResult;
    }
}

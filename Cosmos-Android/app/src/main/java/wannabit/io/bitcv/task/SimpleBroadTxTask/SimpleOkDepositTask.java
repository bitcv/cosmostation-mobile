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
import wannabit.io.bitcv.network.res.ResOkAccountInfo;
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

import static wannabit.io.bitcv.base.BaseChain.OK_TEST;
import static wannabit.io.bitcv.base.BaseChain.getChain;
import static wannabit.io.bitcv.base.BaseConstant.ERROR_CODE_BROADCAST;
import static wannabit.io.bitcv.base.BaseConstant.TASK_GEN_TX_OK_DEPOSIT;

public class SimpleOkDepositTask extends CommonTask {

    private Account     mAccount;
    private BaseChain   mBaseChain;
    private Coin mDepositCoin;
    private String      mMemo;
    private Fee mFees;

    public SimpleOkDepositTask(BaseApplication app, TaskListener listener, Account account, BaseChain chain,  Coin coin, String memo, Fee fees) {
        super(app, listener);
        this.mAccount = account;
        this.mBaseChain = chain;
        this.mDepositCoin = coin;
        this.mMemo = memo;
        this.mFees = fees;
        this.mResult.taskType = TASK_GEN_TX_OK_DEPOSIT;
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

            //yongjoo
            if (mBaseChain.equals(OK_TEST)) {
                Response<ResOkAccountInfo> accountResponse = ApiClient.getOkTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if (!accountResponse.isSuccessful()) {
                    mResult.errorCode = ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromOkLcd(mAccount.id, accountResponse.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            }

            String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
            DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mAccount.baseChain), entropy, Integer.parseInt(mAccount.path), mAccount.newBip44);

            Msg depositMsg = MsgGenerator.genOkDeposit(mAccount.address, mDepositCoin, mBaseChain);
            ArrayList<Msg> msgs= new ArrayList<>();
            msgs.add(depositMsg);

            if (getChain(mAccount.baseChain).equals(OK_TEST)) {
                ReqBroadCast reqBroadCast = MsgGenerator.getBraodcaseReq(mAccount, msgs, mFees, mMemo, deterministicKey);
                WLog.w("reqBroadCast : " +  WUtil.prettyPrinter(reqBroadCast));
                Response<ResBroadTx> response = ApiClient.getOkTestChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if (response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = ERROR_CODE_BROADCAST;
                }

            }

        } catch (Exception e) {
            if(BaseConstant.IS_SHOWLOG) e.printStackTrace();
        }

        return mResult;
    }
}

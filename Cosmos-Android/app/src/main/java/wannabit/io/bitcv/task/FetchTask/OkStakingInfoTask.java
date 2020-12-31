package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResOkStaking;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class OkStakingInfoTask extends CommonTask {

    private BaseChain mChain;
    private Account mAccount;

    public OkStakingInfoTask(BaseApplication app, TaskListener listener, Account account, BaseChain chain) {
        super(app, listener);
        this.mAccount           = account;
        this.mChain             = chain;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_OK_STAKING_INFO;

    }


    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.OK_TEST)) {
                Response<ResOkStaking> response = ApiClient.getOkTestChain(mApp).getDepositInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if(response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                }

            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("OkDepositTask Error " + e.getMessage());

        }
        return mResult;
    }

}

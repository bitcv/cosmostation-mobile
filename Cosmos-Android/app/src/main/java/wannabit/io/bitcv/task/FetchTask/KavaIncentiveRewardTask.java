package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResKavaIncentiveParam;
import wannabit.io.bitcv.network.res.ResKavaIncentiveReward;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class KavaIncentiveRewardTask extends CommonTask {

    private BaseChain mChain;
    private Account mAccount;
    private ResKavaIncentiveParam.IncentiveReward mRewardParam;

    public KavaIncentiveRewardTask(BaseApplication app, TaskListener listener, BaseChain chain, Account account, ResKavaIncentiveParam.IncentiveReward rewardParam) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_INCENTIVE_REWARD;
        this.mChain = chain;
        this.mAccount = account;
        this.mRewardParam = rewardParam;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaIncentiveReward> response = ApiClient.getKavaChain(mApp).getIncentive(mAccount.address, mRewardParam.collateral_type).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaIncentiveReward> response = ApiClient.getKavaTestChain(mApp).getIncentive(mAccount.address, mRewardParam.collateral_type).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }
            }

        } catch (Exception e) {
            WLog.w("KavaIncentiveRewardTask Error " + e.getMessage());
        }
        return mResult;
    }
}

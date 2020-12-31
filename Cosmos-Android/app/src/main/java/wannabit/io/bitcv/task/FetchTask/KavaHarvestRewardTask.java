package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResKavaHarvestReward;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_KAVA_HARVEST_REWARD;

public class KavaHarvestRewardTask extends CommonTask {

    private BaseChain mChain;
    private Account mAccount;

    public KavaHarvestRewardTask(BaseApplication app, TaskListener listener, BaseChain chain, Account account) {
        super(app, listener);
        this.mChain = chain;
        this.mAccount = account;
        this.mResult.taskType = TASK_FETCH_KAVA_HARVEST_REWARD;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaHarvestReward> response = ApiClient.getKavaChain(mApp).getHarvestReward(mAccount.address).execute();
                if(response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaHarvestRewardTask : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaHarvestReward> response = ApiClient.getKavaTestChain(mApp).getHarvestReward(mAccount.address).execute();
                if(response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaHarvestRewardTask : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaHarvestRewardTask Error " + e.getMessage());
        }
        return mResult;
    }
}

package wannabit.io.bitcv.task.FetchTask;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;
import wannabit.io.bitcv.model.type.Coin;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.dao.TotalReward;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class TotalRewardTask extends CommonTask {

    private ArrayList<Account> mAccounts;

    public TotalRewardTask(BaseApplication app, TaskListener listener, ArrayList<Account> accounts) {
        super(app, listener);
        this.mAccounts          = accounts;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_TOTAL_REWARDS;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        HashMap<Long, TotalReward>       mTotalRewards = new HashMap<>();
        try {
            for(Account account : mAccounts) {
                Response<ArrayList<Coin>> response = ApiClient.getCosmosChain(mApp).getTotalRewards(account.address).execute();
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    TotalReward totalReward = new TotalReward(account.id, response.body());
                    mTotalRewards.put(account.id, totalReward);
                }
            }
            mResult.resultData = mTotalRewards;
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("TotalRewardTask Error " + e.getMessage());
        }

        return mResult;
    }
}

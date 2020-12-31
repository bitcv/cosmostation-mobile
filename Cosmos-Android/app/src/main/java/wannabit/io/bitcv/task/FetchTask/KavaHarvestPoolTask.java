package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResKavaHarvestAccount;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_KAVA_HARVEST_ACCOUNT;

public class KavaHarvestPoolTask extends CommonTask {

    private BaseChain mChain;

    public KavaHarvestPoolTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType = TASK_FETCH_KAVA_HARVEST_ACCOUNT;
        this.mChain = chain;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaHarvestAccount> response = ApiClient.getKavaChain(mApp).getHarvestAccount().execute();
                if(response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaHarvestPoolTask : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaHarvestAccount> response = ApiClient.getKavaTestChain(mApp).getHarvestAccount().execute();
                if(response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaHarvestPoolTask : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaHarvestPoolTask Error " + e.getMessage());
        }
        return mResult;
    }
}

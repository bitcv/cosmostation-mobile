package wannabit.io.bitcv.task.FetchTask;

import java.util.ArrayList;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResBnbFee;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class BnbFeesTask extends CommonTask {

    private BaseChain mBaseChain;

    public BnbFeesTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mBaseChain = chain;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_BNB_FEES;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mBaseChain.equals(BaseChain.BNB_MAIN)) {
                Response<ArrayList<ResBnbFee>> response = ApiClient.getBnbChain(mApp).getFees().execute();
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    mResult.resultData = response.body();
                }

            } else if (mBaseChain.equals(BaseChain.BNB_TEST)) {
                Response<ArrayList<ResBnbFee>> response = ApiClient.getBnbTestChain(mApp).getFees().execute();
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    mResult.resultData = response.body();

                }
            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("BnbFeesTask Error " + e.getMessage());
        }
        return mResult;
    }
}
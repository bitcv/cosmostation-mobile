package wannabit.io.bitcv.task.FetchTask;

import java.util.ArrayList;

import retrofit2.Response;
import wannabit.io.bitcv.dao.BacToken;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.IrisToken;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class BacTokenListTask extends CommonTask {

    public BacTokenListTask(BaseApplication app, TaskListener listener) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_BAC_TOKENS;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<ArrayList<BacToken>> response = ApiClient.getBacChain(mApp).getTokens().execute();
            if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                mResult.resultData = response.body();
                mResult.isSuccess = true;

            } else {
                WLog.w("BacTokenList : NOk");
            }

        } catch (Exception e) {
            WLog.w("BacTokenList Error " + e.getMessage());
        }
        return mResult;
    }
}

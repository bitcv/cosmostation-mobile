package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.req.ReqMoonPayKey;
import wannabit.io.bitcv.network.res.ResMoonPaySignature;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class MoonPayTask extends CommonTask {

    private String mQuery;

    public MoonPayTask(BaseApplication app, TaskListener listener, String query) {
        super(app, listener);
        this.mQuery = query;
        this.mResult.taskType = BaseConstant.TASK_MOON_PAY_SIGNATURE;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            ReqMoonPayKey reqMoonPayKey = new ReqMoonPayKey();
            reqMoonPayKey.api_key = mQuery;
            Response<ResMoonPaySignature> response = ApiClient.getCosmostation(mApp).getMoonPay(reqMoonPayKey).execute();
            if (response.isSuccessful() && response.body() != null && response.body().signature != null) {
                mResult.isSuccess = true;
                mResult.resultData = response.body().signature;
            }

        } catch (Exception e) {
            if(BaseConstant.IS_SHOWLOG) e.printStackTrace();

        }
        return mResult;
    }
}
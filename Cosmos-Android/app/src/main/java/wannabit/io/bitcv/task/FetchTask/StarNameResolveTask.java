package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.req.ReqStarNameResolve;
import wannabit.io.bitcv.network.res.ResIovStarNameResolve;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_STARNAME_RESOLVE;

public class StarNameResolveTask extends CommonTask {

    private BaseChain   mBaseChain;
    private String      mStarName;

    public StarNameResolveTask(BaseApplication app, TaskListener listener, BaseChain basecahin, String starname) {
        super(app, listener);
        this.mBaseChain = basecahin;
        this.mStarName = starname;
        this.mResult.taskType = TASK_FETCH_STARNAME_RESOLVE;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        ReqStarNameResolve req = new ReqStarNameResolve(mStarName);
        try {
            if (mBaseChain.equals(BaseChain.IOV_MAIN)) {
                Response<ResIovStarNameResolve> response = ApiClient.getIovChain(mApp).getStarnameAddress(req).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.account != null) {
                    mResult.resultData = response.body().result.account;
                    mResult.isSuccess = true;
                }
            } else if (mBaseChain.equals(BaseChain.IOV_TEST)) {
                Response<ResIovStarNameResolve> response = ApiClient.getIovTestChain(mApp).getStarnameAddress(req).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.account != null) {
                    mResult.resultData = response.body().result.account;
                    mResult.isSuccess = true;
                }
            }

        } catch (Exception e) {
            WLog.w("StarNameResolveTask Error " + e.getMessage());
        }

        return mResult;
    }
}

package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResBandOracleStatus;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

import static wannabit.io.bitcv.base.BaseChain.BAND_MAIN;

public class BandOracleStatusTask extends CommonTask {
    private BaseChain mChain;

    public BandOracleStatusTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_BAND_ORACLE_STATUS;
        this.mChain = chain;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BAND_MAIN)) {
                Response<ResBandOracleStatus> response = ApiClient.getBandChain(mApp).getOracleStatus().execute();
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

        } catch (Exception e) {
            WLog.w("BandOracleStatusTask Error " + e.getMessage());
        }

        return mResult;
    }
}

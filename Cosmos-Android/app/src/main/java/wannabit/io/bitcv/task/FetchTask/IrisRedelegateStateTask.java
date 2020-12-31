package wannabit.io.bitcv.task.FetchTask;

import java.util.ArrayList;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResLcdIrisRedelegate;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class IrisRedelegateStateTask extends CommonTask {

    private Account mAccount;

    public IrisRedelegateStateTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_IRIS_REDELEGATE;
        this.mAccount = account;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<ArrayList<ResLcdIrisRedelegate>> response = ApiClient.getIrisChain(mApp).getRedelegateState(mAccount.address).execute();
            if(response.isSuccessful()) {
                if(response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                } else {
                    mResult.isSuccess = true;
                }
            }

        } catch (Exception e) {
            WLog.w("SingleUnBondingStateTask Error " + e.getMessage());
        }
        return mResult;
    }
}

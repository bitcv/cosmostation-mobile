package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.req.ReqPushAlarm;
import wannabit.io.bitcv.network.res.ResPushAlarm;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class PushUpdateTask extends CommonTask {

    private Account mAccount;
    private String mPushToken;
    private boolean mEnable;

    public PushUpdateTask(BaseApplication app, TaskListener listener, Account account, String token, boolean enable) {
        super(app, listener);
        this.mAccount = account;
        this.mPushToken = token;
        this.mEnable = enable;
        this.mResult.taskType = BaseConstant.TASK_PUSH_STATUS_UPDATE;
        this.mResult.resultData = enable;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {

            ReqPushAlarm reqPushAlarm = new ReqPushAlarm();
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.COSMOS_MAIN)) {
                reqPushAlarm.chain_id = 1;
            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.IRIS_MAIN)) {
                reqPushAlarm.chain_id = 2;
            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
                reqPushAlarm.chain_id = 3;
            }
            reqPushAlarm.device_type = "android";
            reqPushAlarm.address = mAccount.address;
            reqPushAlarm.alarm_token = mPushToken;
            reqPushAlarm.alarm_status = mEnable;

            Response<ResPushAlarm> response = ApiClient.getCosmostation(mApp).updateAlarm(reqPushAlarm).execute();
            if (response.isSuccessful() && response.body() != null && response.body().result == true) {
                mResult.isSuccess = true;
            }

        } catch (Exception e) {
            if(BaseConstant.IS_SHOWLOG) e.printStackTrace();

        }
        return mResult;
    }
}

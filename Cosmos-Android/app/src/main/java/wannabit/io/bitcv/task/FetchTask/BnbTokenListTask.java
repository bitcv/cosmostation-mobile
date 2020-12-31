package wannabit.io.bitcv.task.FetchTask;

import java.util.ArrayList;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.dao.BnbToken;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class BnbTokenListTask extends CommonTask {

    private Account mAccount;

    public BnbTokenListTask(BaseApplication app, TaskListener listener, Account account) {
        super(app, listener);
        this.mAccount           = account;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_BNB_TOKENS;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BNB_MAIN)) {
                Response<ArrayList<BnbToken>> response = ApiClient.getBnbChain(mApp).getTokens("3000").execute();
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    mResult.resultData = response.body();
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BNB_TEST)) {
                Response<ArrayList<BnbToken>> response = ApiClient.getBnbTestChain(mApp).getTokens("3000").execute();
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    mResult.resultData = response.body();

                }
            }
            mResult.isSuccess = true;

        } catch (Exception e) {
            WLog.w("BnbTokenList Error " + e.getMessage());
        }
        return mResult;
    }
}

package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResKavaHarvestDeposit;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_KAVA_HARVEST_DEPOSIT;

public class KavaHarvestDepositTask extends CommonTask {

    private BaseChain mChain;
    private Account mAccount;

    public KavaHarvestDepositTask(BaseApplication app, TaskListener listener, BaseChain chain, Account account) {
        super(app, listener);
        this.mChain = chain;
        this.mAccount = account;
        this.mResult.taskType = TASK_FETCH_KAVA_HARVEST_DEPOSIT;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaHarvestDeposit> response = ApiClient.getKavaChain(mApp).getHarvestDeposit(mAccount.address).execute();
                if(response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaHarvestDepositTask : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaHarvestDeposit> response = ApiClient.getKavaTestChain(mApp).getHarvestDeposit(mAccount.address).execute();
                if(response.isSuccessful() && response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaHarvestDepositTask : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaHarvestDepositTask Error " + e.getMessage());
        }
        return mResult;
    }
}

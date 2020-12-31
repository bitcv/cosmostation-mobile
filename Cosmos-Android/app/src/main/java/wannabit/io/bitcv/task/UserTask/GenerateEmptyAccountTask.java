package wannabit.io.bitcv.task.UserTask;

import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class GenerateEmptyAccountTask extends CommonTask {

    public GenerateEmptyAccountTask(BaseApplication app, TaskListener listener) {
        super(app, listener);
        this.mResult.taskType = BaseConstant.TASK_INIT_EMPTY_ACCOUNT;
    }

    /**
     *
     * @param strings
     *  strings[0] : chainType
     *  strings[1] : address
     * @return
     */
    @Override
    protected TaskResult doInBackground(String... strings) {

        long id = mApp.getBaseDao().onInsertAccount(onGenEmptyAccount(strings[0], strings[1]));
        if(id > 0) {
            mResult.isSuccess = true;
            mApp.getBaseDao().setLastUser(id);
        } else {
            mResult.errorMsg = "Already existed account";
            mResult.errorCode = 7001;
        }

        return mResult;
    }

    private Account onGenEmptyAccount(String chainType, String address) {
        Account newAccount          = Account.getNewInstance();
        newAccount.address          = address;
        newAccount.baseChain        = chainType;
        newAccount.hasPrivateKey    = false;
        newAccount.fromMnemonic     = false;
        newAccount.importTime       = System.currentTimeMillis();

        return newAccount;
    }
}

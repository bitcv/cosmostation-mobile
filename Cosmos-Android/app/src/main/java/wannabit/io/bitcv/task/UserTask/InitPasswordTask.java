package wannabit.io.bitcv.task.UserTask;

import wannabit.io.bitcv.crypto.CryptoHelper;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Password;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class InitPasswordTask extends CommonTask {

    public InitPasswordTask(BaseApplication app, TaskListener listener) {
        super(app, listener);
        this.mResult.taskType = BaseConstant.TASK_INIT_PW;
    }

    /**
     *
     * @param strings
     *  strings[0] : password
     *
     * @return
     */
    @Override
    protected TaskResult doInBackground(String... strings) {
        Password newPw = new Password(CryptoHelper.signData(strings[0], mApp.getString(R.string.key_password)));
        long insert = mApp.getBaseDao().onInsertPassword(newPw);
        if(insert > 0) {
            mResult.isSuccess = true;
        }
        return mResult;
    }
}

package wannabit.io.bitcv.task.FetchTask;

import java.util.ArrayList;

import retrofit2.Response;
import wannabit.io.bitcv.model.type.Vote;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class IrisVoteListTask extends CommonTask {

    private String proposal_id;


    public IrisVoteListTask(BaseApplication app, TaskListener listener, String id) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_IRIS_VOTE_LIST;
        this.proposal_id = id;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<ArrayList<Vote>> response = ApiClient.getIrisChain(mApp).getVoteList(proposal_id).execute();
            if(response.isSuccessful()) {
                mResult.resultData = response.body();
                mResult.isSuccess = true;

            } else {
                WLog.w("IrisTokenList : NOk");
            }

        } catch (Exception e) {
            WLog.w("IrisTokenList Error " + e.getMessage());
        }
        return mResult;
    }
}

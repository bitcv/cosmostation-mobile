package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.model.type.IrisProposal;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class IrisProposalDetailTask extends CommonTask {

    private String proposal_id;


    public IrisProposalDetailTask(BaseApplication app, TaskListener listener, String id) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_IRIS_PROPOSAL_DETAIL;
        this.proposal_id = id;
    }


    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Response<IrisProposal> response = ApiClient.getIrisChain(mApp).getProposalDetail(proposal_id).execute();
            if (response.isSuccessful()) {
                mResult.isSuccess = true;
                mResult.resultData = response.body();
            }

        } catch (Exception e) {
            WLog.w("AllProposalTask Error " + e.getMessage());
        }

        return mResult;
    }
}
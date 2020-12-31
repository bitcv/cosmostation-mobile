package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResLcdProposer;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class ProposalProposerTask extends CommonTask {

    private BaseChain mChain;
    private String mProposalId;

    public ProposalProposerTask(BaseApplication app, TaskListener listener, String proposalId, BaseChain chain) {
        super(app, listener);
        this.mProposalId = proposalId;
        this.mChain = chain;
        this.mResult.taskType   = BaseConstant.TASK_FETCH_PROPOSAL_PROPOSER;
    }


    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.COSMOS_MAIN)) {
                Response<ResLcdProposer> response = ApiClient.getCosmosChain(mApp).getProposer(mProposalId).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.proposer;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResLcdProposer> response = ApiClient.getKavaChain(mApp).getProposer(mProposalId).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.proposer;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.BAND_MAIN)) {
                Response<ResLcdProposer> response = ApiClient.getBandChain(mApp).getProposer(mProposalId).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.proposer;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.CERTIK_MAIN)) {
                Response<ResLcdProposer> response = ApiClient.getCertikChain(mApp).getProposer(mProposalId).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.proposer;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.CERTIK_TEST)) {
                Response<ResLcdProposer> response = ApiClient.getCertikTestChain(mApp).getProposer(mProposalId).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.proposer;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.IOV_MAIN)) {
                Response<ResLcdProposer> response = ApiClient.getIovChain(mApp).getProposer(mProposalId).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.proposer;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.SECRET_MAIN)) {
                Response<ResLcdProposer> response = ApiClient.getSecretChain(mApp).getProposer(mProposalId).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.proposer;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BaseChain.AKASH_MAIN)) {
                Response<ResLcdProposer> response = ApiClient.getAkashChain(mApp).getProposer(mProposalId).execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result.proposer;
                    mResult.isSuccess = true;
                }

            }

        } catch (Exception e) {
            WLog.w("ProposalProposerTask Error " + e.getMessage());
        }

        return mResult;
    }
}

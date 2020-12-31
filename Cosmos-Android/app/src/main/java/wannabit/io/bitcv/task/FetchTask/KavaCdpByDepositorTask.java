package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResCdpDepositStatus;
import wannabit.io.bitcv.network.res.ResCdpParam;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class KavaCdpByDepositorTask extends CommonTask {

    private BaseChain mChain;
    private String mAddress;

    private ResCdpParam.KavaCollateralParam mParam;

    public KavaCdpByDepositorTask(BaseApplication app, TaskListener listener, BaseChain chain, String address, ResCdpParam.KavaCollateralParam param) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_CDP_DEPOSIT;
        this.mChain = chain;
        this.mAddress = address;
        this.mParam = param;

    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResCdpDepositStatus> response = ApiClient.getKavaChain(mApp).getCdpDepositStatus(mAddress, mParam.type).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaCdpByDepositor : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResCdpDepositStatus> response = ApiClient.getKavaTestChain(mApp).getCdpDepositStatus(mAddress, mParam.type).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaCdpByDepositor : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaCdpByDepositor Error " + e.getMessage());
        }
        return mResult;
    }
}
package wannabit.io.bitcv.task.FetchTask;

import retrofit2.Response;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResKavaMarketPrice;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

public class KavaMarketPriceTask extends CommonTask {

    private BaseChain mChain;
    private String mMarket;

    public KavaMarketPriceTask(BaseApplication app, TaskListener listener, BaseChain chain, String market) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_KAVA_TOKEN_PRICE;
        this.mChain = chain;
        this.mMarket = market;

    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BaseChain.KAVA_MAIN)) {
                Response<ResKavaMarketPrice> response = ApiClient.getKavaChain(mApp).getPrice(mMarket).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaMarketPriceTask : NOk");
                }

            } else if (mChain.equals(BaseChain.KAVA_TEST)) {
                Response<ResKavaMarketPrice> response = ApiClient.getKavaTestChain(mApp).getPrice(mMarket).execute();
                if(response.isSuccessful() && response.body() != null && response.body().result != null) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;

                } else {
                    WLog.w("KavaMarketPriceTask : NOk");
                }
            }

        } catch (Exception e) {
            WLog.w("KavaMarketPriceTask Error " + e.getMessage());
        }
        return mResult;
    }
}
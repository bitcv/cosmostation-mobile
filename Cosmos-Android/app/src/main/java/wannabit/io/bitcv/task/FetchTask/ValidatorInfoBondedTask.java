package wannabit.io.bitcv.task.FetchTask;

import java.util.ArrayList;

import retrofit2.Response;
import wannabit.io.bitcv.model.type.Validator;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResLcdBacValidator;
import wannabit.io.bitcv.network.res.ResLcdValidators;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.base.BaseApplication;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.task.CommonTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;

import static wannabit.io.bitcv.base.BaseChain.AKASH_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BAC_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BAND_MAIN;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_MAIN;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_TEST;
import static wannabit.io.bitcv.base.BaseChain.COSMOS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_TEST;
import static wannabit.io.bitcv.base.BaseChain.IRIS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_TEST;
import static wannabit.io.bitcv.base.BaseChain.OK_TEST;
import static wannabit.io.bitcv.base.BaseChain.SECRET_MAIN;

public class ValidatorInfoBondedTask extends CommonTask {
    private BaseChain   mChain;

    public ValidatorInfoBondedTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mResult.taskType   = BaseConstant.TASK_FETCH_BONDEB_VALIDATOR;
        this.mChain = chain;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(COSMOS_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getCosmosChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(IRIS_MAIN)) {
                int page = 0;
                boolean needMore = true;
                ArrayList<Validator> allResult = new ArrayList<>();
                do {
                    page ++;
                    Response<ArrayList<Validator>> response = ApiClient.getIrisChain(mApp).getValidatorList(""+page, "100").execute();
                    if (!response.isSuccessful()) {
                        mResult.isSuccess = false;
                        mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                        needMore = false;
                    }

                    if (response.body() != null && response.body().size() > 0) {
                        if(response.body().size() == 100) {
                            allResult.addAll(response.body());

                        } else {
                            allResult.addAll(response.body());
                            mResult.isSuccess = true;
                            needMore = false;
                        }
                    }

                } while (needMore);
                mResult.resultData = allResult;

            } else if (mChain.equals(BAC_MAIN)) {
                Response<ArrayList<ResLcdBacValidator>> response = ApiClient.getBacChain(mApp).getBondedValidatorDetailList().execute();
                WLog.w(response.toString());
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().size()  > 0) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                }
            } else if (mChain.equals(KAVA_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getKavaChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(KAVA_TEST)) {
                Response<ResLcdValidators> response = ApiClient.getKavaTestChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(BAND_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getBandChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(IOV_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getIovChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(IOV_TEST)) {
                Response<ResLcdValidators> response = ApiClient.getIovTestChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(OK_TEST)) {
                Response<ArrayList<Validator>> response = ApiClient.getOkTestChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null) {
                    mResult.resultData = response.body();
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(CERTIK_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getCertikChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(CERTIK_TEST)) {
                Response<ResLcdValidators> response = ApiClient.getCertikTestChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(SECRET_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getSecretChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            } else if (mChain.equals(AKASH_MAIN)) {
                Response<ResLcdValidators> response = ApiClient.getAkashChain(mApp).getBondedValidatorDetailList().execute();
                if (!response.isSuccessful()) {
                    mResult.isSuccess = false;
                    mResult.errorCode = BaseConstant.ERROR_CODE_NETWORK;
                    return mResult;
                }

                if (response.body() != null && response.body().result != null && response.body().result.size() > 0) {
                    mResult.resultData = response.body().result;
                    mResult.isSuccess = true;
                }

            }


        } catch (Exception e) {
            WLog.w("AllValidatorInfo Error " + e.getMessage());
        }

        return mResult;
    }
}

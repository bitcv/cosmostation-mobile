package wannabit.io.bitcv.network;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import wannabit.io.bitcv.network.res.ResBnbAccountInfo;
import wannabit.io.bitcv.network.res.ResBnbFee;
import wannabit.io.bitcv.network.res.ResBnbHistories;
import wannabit.io.bitcv.network.res.ResBnbNodeInfo;
import wannabit.io.bitcv.network.res.ResBnbSwapInfo;
import wannabit.io.bitcv.network.res.ResBnbTic;
import wannabit.io.bitcv.network.res.ResBnbTxInfo;
import wannabit.io.bitcv.dao.BnbToken;

public interface BinanceChain {

    @GET("/api/v1/node-info")
    Call<ResBnbNodeInfo> getNodeInfo();

    @GET("/api/v1/account/{address}")
    Call<ResBnbAccountInfo> getAccountInfo(@Path("address") String address);

    @GET("/api/v1/transactions")
    Call<ResBnbHistories> getHistory(@Query("address") String address, @Query("startTime") String startTime, @Query("endTime") String endTime);

    @GET("/api/v1/transactions")
    Call<ResBnbHistories> getHistoryAsset(@Query("address") String address, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("txAsset") String txAsset);

    @GET("/api/v1/tokens")
    Call<ArrayList<BnbToken>> getTokens(@Query("limit") String limit);

    @GET("/api/v1/ticker/24hr")
    Call<ArrayList<ResBnbTic>> getTic(@Query("symbol") String symbol);

    @GET("/api/v1/tx/{hash}")
    Call<ResBnbTxInfo> getSearchTx(@Path("hash") String hash, @Query("format") String format);

    @GET("/api/v1/atomic-swaps/{swapId}")
    Call<ResBnbSwapInfo> getSwapById(@Path("swapId") String swapId);

    @GET("/claim/{address}")
    Call<JSONObject> getFaucet(@Path("address") String address);

    @GET("/api/v1/mini/tokens")
    Call<ArrayList<BnbToken>> getMiniTokens(@Query("limit") String limit);

    @GET("/api/v1/fees")
    Call<ArrayList<ResBnbFee>> getFees();
}

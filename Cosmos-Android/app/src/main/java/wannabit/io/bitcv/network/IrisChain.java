package wannabit.io.bitcv.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import wannabit.io.bitcv.network.req.ReqBroadCast;
import wannabit.io.bitcv.network.res.ResBroadTx;
import wannabit.io.bitcv.network.res.ResLcdAccountInfo;
import wannabit.io.bitcv.network.res.ResLcdBonding;
import wannabit.io.bitcv.network.res.ResLcdIrisPool;
import wannabit.io.bitcv.network.res.ResLcdIrisRedelegate;
import wannabit.io.bitcv.network.res.ResLcdIrisReward;
import wannabit.io.bitcv.network.res.ResLcdUnBonding;
import wannabit.io.bitcv.network.res.ResTxInfo;
import wannabit.io.bitcv.dao.IrisToken;
import wannabit.io.bitcv.model.type.IrisProposal;
import wannabit.io.bitcv.model.type.Vote;
import wannabit.io.bitcv.model.type.Validator;

public interface IrisChain {

    @GET("/stake/validators")
    Call<ArrayList<Validator>> getValidatorList(@Query("page") String page, @Query("size") String size);


    @GET("/bank/accounts/{address}")
    Call<ResLcdAccountInfo> getBankInfo(@Path("address") String address);

    @GET("/stake/delegators/{address}/delegations")
    Call<ArrayList<ResLcdBonding>> getBondingList(@Path("address") String address);

    @GET("/stake/delegators/{address}/unbonding-delegations")
    Call<ArrayList<ResLcdUnBonding>> getUnBondingList(@Path("address") String address);

    @GET("/distribution/{address}/rewards")
    Call<ResLcdIrisReward> getRewardsInfo(@Path("address") String address);

    @GET("/distribution/{address}/withdraw-address")
    Call<String> getWithdrawAddress(@Path("address") String address);

    @GET("/txs/{hash}")
    Call<ResTxInfo> getSearchTx(@Path("hash") String hash);


    //Broadcast Tx
    @POST("/tx/broadcast")
    Call<ResBroadTx> broadTx(@Body ReqBroadCast data);



    @GET("/stake/pool")
    Call<ResLcdIrisPool> getIrisPool();


    @GET("/gov/proposals")
    Call<ArrayList<IrisProposal>> getProposalList();

    //Validator details
    @GET("/stake/validators/{validatorAddr}")
    Call<Validator> getValidatorDetail(@Path("validatorAddr") String validatorAddr);

    @GET("/stake/delegators/{address}/delegations/{validatorAddr}")
    Call<ResLcdBonding> getBonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

    @GET("/stake/delegators/{address}/unbonding-delegations/{validatorAddr}")
    Call<ResLcdUnBonding> getUnbonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

    @GET("/stake/delegators/{delegatorAddr}/redelegations")
    Call<ArrayList<ResLcdIrisRedelegate>> getRedelegateState(@Path("delegatorAddr") String delegatorAddr);


    @GET("/asset/tokens")
    Call<ArrayList<IrisToken>> getTokens();

    @GET("gov/proposals/{proposal_id}/votes")
    Call<ArrayList<Vote>> getVoteList(@Path("proposal_id") String proposal_id);

    @GET("/gov/proposals/{proposal_id}")
    Call<IrisProposal> getProposalDetail(@Path("proposal_id") String proposal_id);
}

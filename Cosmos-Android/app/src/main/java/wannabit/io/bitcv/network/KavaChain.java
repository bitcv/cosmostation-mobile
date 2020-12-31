package wannabit.io.bitcv.network;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import wannabit.io.bitcv.network.req.ReqBroadCast;
import wannabit.io.bitcv.network.res.ResBroadTx;
import wannabit.io.bitcv.network.res.ResCdpDepositStatus;
import wannabit.io.bitcv.network.res.ResCdpList;
import wannabit.io.bitcv.network.res.ResCdpOwnerStatus;
import wannabit.io.bitcv.network.res.ResCdpParam;
import wannabit.io.bitcv.network.res.ResKavaBep3Param2;
import wannabit.io.bitcv.network.res.ResKavaHarvestAccount;
import wannabit.io.bitcv.network.res.ResKavaHarvestDeposit;
import wannabit.io.bitcv.network.res.ResKavaHarvestParam;
import wannabit.io.bitcv.network.res.ResKavaHarvestReward;
import wannabit.io.bitcv.network.res.ResKavaIncentiveParam;
import wannabit.io.bitcv.network.res.ResKavaIncentiveReward;
import wannabit.io.bitcv.network.res.ResKavaMarketPrice;
import wannabit.io.bitcv.network.res.ResKavaPriceFeedParam;
import wannabit.io.bitcv.network.res.ResKavaSupply;
import wannabit.io.bitcv.network.res.ResKavaSwapInfo;
import wannabit.io.bitcv.network.res.ResKavaSwapSupply2;
import wannabit.io.bitcv.network.res.ResLcdBondings;
import wannabit.io.bitcv.network.res.ResLcdInflation;
import wannabit.io.bitcv.network.res.ResLcdKavaAccountInfo;
import wannabit.io.bitcv.network.res.ResLcdProposal;
import wannabit.io.bitcv.network.res.ResLcdProposalTally;
import wannabit.io.bitcv.network.res.ResLcdProposalVoted;
import wannabit.io.bitcv.network.res.ResLcdProposals;
import wannabit.io.bitcv.network.res.ResLcdProposer;
import wannabit.io.bitcv.network.res.ResLcdRedelegate;
import wannabit.io.bitcv.network.res.ResLcdRewardFromVal;
import wannabit.io.bitcv.network.res.ResLcdSingleBonding;
import wannabit.io.bitcv.network.res.ResLcdSingleUnBonding;
import wannabit.io.bitcv.network.res.ResLcdSingleValidator;
import wannabit.io.bitcv.network.res.ResLcdUnBondings;
import wannabit.io.bitcv.network.res.ResLcdValidators;
import wannabit.io.bitcv.network.res.ResLcdWithDrawAddress;
import wannabit.io.bitcv.network.res.ResMintParam;
import wannabit.io.bitcv.network.res.ResMyVote;
import wannabit.io.bitcv.network.res.ResProvisions;
import wannabit.io.bitcv.network.res.ResStakingPool;
import wannabit.io.bitcv.network.res.ResTxInfo;

public interface KavaChain {

    @GET("txs/{hash}")
    Call<ResTxInfo> getSearchTx(@Path("hash") String hash);

    @GET("auth/accounts/{address}")
    Call<ResLcdKavaAccountInfo> getAccountInfo(@Path("address") String address);

    @GET("staking/validators?status=bonded")
    Call<ResLcdValidators> getBondedValidatorDetailList();

    @GET("staking/validators?status=unbonding")
    Call<ResLcdValidators> getUnBondingValidatorDetailList();

    @GET("staking/validators?status=unbonded")
    Call<ResLcdValidators> getUnBondedValidatorDetailList();

    @GET("staking/delegators/{address}/delegations")
    Call<ResLcdBondings> getBondingList(@Path("address") String address);

    @GET("staking/delegators/{address}/unbonding_delegations")
    Call<ResLcdUnBondings> getUnBondingList(@Path("address") String address);

    @GET("distribution/delegators/{delegatorAddr}/rewards/{validatorAddr}")
    Call<ResLcdRewardFromVal> getRewardFromValidator(@Path("delegatorAddr") String delegatorAddr, @Path("validatorAddr") String validatorAddr);

    @GET("/minting/parameters")
    Call<ResMintParam> getMintParam();

    @GET("minting/inflation")
    Call<ResLcdInflation> getInflation();

    @GET("minting/annual-provisions")
    Call<ResProvisions> getProvisions();

    @GET("staking/pool")
    Call<ResStakingPool> getStakingPool();

    @GET("staking/validators/{validatorAddr}")
    Call<ResLcdSingleValidator> getValidatorDetail(@Path("validatorAddr") String validatorAddr);

    @GET("staking/delegators/{address}/delegations/{validatorAddr}")
    Call<ResLcdSingleBonding> getBonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

    @GET("staking/delegators/{address}/unbonding_delegations/{validatorAddr}")
    Call<ResLcdSingleUnBonding> getUnbonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

    @GET("staking/redelegations")
    Call<ResLcdRedelegate> getRedelegateAllHistory(@Query("delegator") String delegator, @Query("validator_from") String validator_from, @Query("validator_to") String validator_to);

    @GET("staking/redelegations")
    Call<ResLcdRedelegate> getRedelegateHistory(@Query("delegator") String delegator, @Query("validator_to") String validator_to);

    @GET("distribution/delegators/{address}/withdraw_address")
    Call<ResLcdWithDrawAddress> getWithdrawAddress(@Path("address") String address);

    @GET("gov/proposals")
    Call<ResLcdProposals> getProposalList();

    @GET("gov/proposals/{proposalId}")
    Call<ResLcdProposal> getProposalDetail(@Path("proposalId") String proposalId);

    @GET("gov/proposals/{proposalId}/tally")
    Call<ResLcdProposalTally> getTally(@Path("proposalId") String proposalId);

    @GET("gov/proposals/{proposalId}/votes")
    Call<ResLcdProposalVoted> getVotedList(@Path("proposalId") String proposalId);

    @GET("gov/proposals/{proposalId}/votes/{address}")
    Call<ResMyVote> getMyVote(@Path("proposalId") String proposalId, @Path("address") String address);

    @GET("gov/proposals/{proposalId}/proposer")
    Call<ResLcdProposer> getProposer(@Path("proposalId") String proposalId);

    @GET("supply/total")
    Call<ResKavaSupply> getSupply();

    @POST("txs")
    Call<ResBroadTx> broadTx(@Body ReqBroadCast data);



    @GET("cdp/parameters")
    Call<ResCdpParam> getCdpParams();

    @GET("cdp/cdps/cdp/{address}/{denom}")
    Call<ResCdpOwnerStatus> getCdpStatusByOwner(@Path("address") String address, @Path("denom") String denom);

    @GET("cdp/cdps/cdp/deposits/{address}/{denom}")
    Call<ResCdpDepositStatus> getCdpDepositStatus(@Path("address") String address, @Path("denom") String denom);

    @GET("cdp/cdps/denom/{denom}")
    Call<ResCdpList> getCdpListByDenom(@Path("denom") String denom);

    @GET("cdp/cdps/ratio/{denom}/{ratio}")
    Call<ResCdpList> getCdpCoinRate(@Path("denom") String denom, @Path("ratio") String ratio);


    @GET("pricefeed/parameters")
    Call<ResKavaPriceFeedParam> getPriceParam();

    @GET("pricefeed/price/{market}")
    Call<ResKavaMarketPrice> getPrice(@Path("market") String market);



    @GET("bep3/swap/{swapId}")
    Call<ResKavaSwapInfo> getSwapById(@Path("swapId") String swapId);

    @GET("bep3/swaps")
    Call<String> getSwaps();

    @GET("bep3/parameters")
    Call<ResKavaBep3Param2> getSwapParams2();

    @GET("bep3/supplies")
    Call<ResKavaSwapSupply2> getSupplies2();

    @GET("incentive/parameters")
    Call<ResKavaIncentiveParam> getIncentiveParams();

    @GET("incentive/claims/{address}/{denom}")
    Call<ResKavaIncentiveReward> getIncentive(@Path("address") String address, @Path("denom") String denom);

    @GET("harvest/parameters")
    Call<ResKavaHarvestParam> getHarvestParam();

    @GET("harvest/deposits")
    Call<ResKavaHarvestDeposit> getHarvestDeposit(@Query("owner") String owner);

    @GET("harvest/claims")
    Call<ResKavaHarvestReward> getHarvestReward(@Query("owner") String owner);

    @GET("harvest/accounts")
    Call<ResKavaHarvestAccount> getHarvestAccount();



    @GET("faucet/{address}")
    Call<JSONObject> getFaucet(@Path("address") String address);










}

package wannabit.io.bitcv.network;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import wannabit.io.bitcv.network.req.ReqBroadCast;
import wannabit.io.bitcv.network.req.ReqStarNameAccountInDomain;
import wannabit.io.bitcv.network.req.ReqStarNameByOwner;
import wannabit.io.bitcv.network.req.ReqStarNameDomainInfo;
import wannabit.io.bitcv.network.req.ReqStarNameResolve;
import wannabit.io.bitcv.network.res.ResBroadTx;
import wannabit.io.bitcv.network.res.ResIovConfig;
import wannabit.io.bitcv.network.res.ResIovFee;
import wannabit.io.bitcv.network.res.ResIovStarNameAccount;
import wannabit.io.bitcv.network.res.ResIovStarNameAccountInDomain;
import wannabit.io.bitcv.network.res.ResIovStarNameDomain;
import wannabit.io.bitcv.network.res.ResIovStarNameDomainInfo;
import wannabit.io.bitcv.network.res.ResIovStarNameResolve;
import wannabit.io.bitcv.network.res.ResLcdAccountInfo;
import wannabit.io.bitcv.network.res.ResLcdBondings;
import wannabit.io.bitcv.network.res.ResLcdInflation;
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

public interface IovChain {
        //new version for IOV
        @GET("/auth/accounts/{address}")
        Call<ResLcdAccountInfo> getAccountInfo(@Path("address") String address);

        @GET("/txs/{hash}")
        Call<ResTxInfo> getSearchTx(@Path("hash") String hash);

        @GET("/staking/validators?status=bonded")
        Call<ResLcdValidators> getBondedValidatorDetailList();

        @GET("/staking/validators?status=unbonding")
        Call<ResLcdValidators> getUnBondingValidatorDetailList();

        @GET("/staking/validators?status=unbonded")
        Call<ResLcdValidators> getUnBondedValidatorDetailList();

        @GET("/staking/delegators/{address}/delegations")
        Call<ResLcdBondings> getBondingList(@Path("address") String address);

        @GET("/staking/delegators/{address}/unbonding_delegations")
        Call<ResLcdUnBondings> getUnBondingList(@Path("address") String address);

        @GET("/distribution/delegators/{delegatorAddr}/rewards/{validatorAddr}")
        Call<ResLcdRewardFromVal> getRewardFromValidator(@Path("delegatorAddr") String delegatorAddr, @Path("validatorAddr") String validatorAddr);

        @GET("/minting/parameters")
        Call<ResMintParam> getMintParam();

        @GET("/minting/inflation")
        Call<ResLcdInflation> getInflation();

        @GET("/minting/annual-provisions")
        Call<ResProvisions> getProvisions();

        @GET("/staking/pool")
        Call<ResStakingPool> getStakingPool();

        @GET("/distribution/delegators/{address}/withdraw_address")
        Call<ResLcdWithDrawAddress> getWithdrawAddress(@Path("address") String address);


        @GET("/staking/validators/{validatorAddr}")
        Call<ResLcdSingleValidator> getValidatorDetail(@Path("validatorAddr") String validatorAddr);

        @GET("/staking/delegators/{address}/delegations/{validatorAddr}")
        Call<ResLcdSingleBonding> getBonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

        @GET("/staking/delegators/{address}/unbonding_delegations/{validatorAddr}")
        Call<ResLcdSingleUnBonding> getUnbonding(@Path("address") String address, @Path("validatorAddr") String validatorAddr);

        @GET("/staking/redelegations")
        Call<ResLcdRedelegate> getRedelegateHistory(@Query("delegator") String delegator, @Query("validator_to") String validator_to);

        @GET("/staking/redelegations")
        Call<ResLcdRedelegate> getRedelegateAllHistory(@Query("delegator") String delegator, @Query("validator_from") String validator_from, @Query("validator_to") String validator_to);


        //Broadcast Tx
        @POST("/txs")
        Call<ResBroadTx> broadTx(@Body ReqBroadCast data);

        //Proposals
        @GET("/gov/proposals")
        Call<ResLcdProposals> getProposalList();

        @GET("/gov/proposals/{proposalId}/proposer")
        Call<ResLcdProposer> getProposer(@Path("proposalId") String proposalId);

        @GET("/gov/proposals/{proposalId}")
        Call<ResLcdProposal> getProposalDetail(@Path("proposalId") String proposalId);

        @GET("/gov/proposals/{proposalId}/votes")
        Call<ResLcdProposalVoted> getVotedList(@Path("proposalId") String proposalId);

        @GET("/gov/proposals/{proposalId}/tally")
        Call<ResLcdProposalTally> getTally(@Path("proposalId") String proposalId);

        @GET("/gov/proposals/{proposalId}/votes/{address}")
        Call<ResMyVote> getMyVote(@Path("proposalId") String proposalId, @Path("address") String address);


        //Check Starname
        @POST("/starname/query/domainInfo")
        Call<ResIovStarNameDomainInfo> getStarnameDomainInfo(@Body ReqStarNameDomainInfo data);

        @POST("/starname/query/accountsInDomain")
        Call<ResIovStarNameAccountInDomain> getAccountInDomain(@Body ReqStarNameAccountInDomain data);

        @POST("/starname/query/accountsWithOwner")
        Call<ResIovStarNameAccount> getStarnameAccount(@Body ReqStarNameByOwner data);

        @POST("/starname/query/domainsWithOwner")
        Call<ResIovStarNameDomain> getStarnameDomain(@Body ReqStarNameByOwner data);

        @POST("/starname/query/resolve")
        Call<ResIovStarNameResolve> getStarnameAddress(@Body ReqStarNameResolve data);

        @POST("/configuration/query/configuration")
        Call<ResIovConfig> getConfiguration();

        @POST("/configuration/query/fees")
        Call<ResIovFee> getFee();


        @GET("/credit")
        Call<JSONObject> getFaucet(@Query("address") String address);
}

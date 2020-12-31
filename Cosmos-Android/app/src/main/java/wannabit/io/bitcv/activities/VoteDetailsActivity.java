package wannabit.io.bitcv.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseActivity;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.dialog.Dialog_WatchMode;
import wannabit.io.bitcv.model.type.Coin;
import wannabit.io.bitcv.model.type.IrisProposal;
import wannabit.io.bitcv.model.type.Proposal;
import wannabit.io.bitcv.model.type.Tally;
import wannabit.io.bitcv.model.type.Vote;
import wannabit.io.bitcv.task.FetchTask.IrisProposalDetailTask;
import wannabit.io.bitcv.task.FetchTask.IrisVoteListTask;
import wannabit.io.bitcv.task.FetchTask.MyVoteCheckTask;
import wannabit.io.bitcv.task.FetchTask.ProposalDetailTask;
import wannabit.io.bitcv.task.FetchTask.ProposalProposerTask;
import wannabit.io.bitcv.task.FetchTask.ProposalTallyTask;
import wannabit.io.bitcv.task.FetchTask.ProposalVotedListTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;
import wannabit.io.bitcv.utils.WDp;
import wannabit.io.bitcv.utils.WUtil;

import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_IRIS_VOTE_LIST;
import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_MY_VOTE;
import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_PROPOSAL_DETAIL;
import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_PROPOSAL_PROPOSER;
import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_PROPOSAL_TALLY;
import static wannabit.io.bitcv.base.BaseConstant.TASK_FETCH_PROPOSAL_VOTED;
import static wannabit.io.bitcv.base.BaseConstant.TASK_IRIS_PROPOSAL_DETAIL;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_AKASH;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_CERTIK;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_IOV;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_IRIS_ATTO;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_SECRET;
import static wannabit.io.bitcv.model.type.Proposal.PROPOSAL_VOTING;

public class VoteDetailsActivity extends BaseActivity implements View.OnClickListener, TaskListener {

    private Toolbar             mToolbar;
    private SwipeRefreshLayout  mSwipeRefreshLayout;
    private RecyclerView        mRecyclerView;
    private RelativeLayout      mLoadingLayer;
    private Button              mVoteBtn;

    private VoteDetailsAdapter  mVoteDetailsAdapter;


    private String              mProposalId;
    private Proposal            mProposal;
    private String              mProposer;
    private IrisProposal        mIrisProposal;
    private Tally               mTally;
    private ArrayList<Vote>     mVotes = new ArrayList<>();
    private Vote                mMyVote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_details);
        mToolbar            = findViewById(R.id.tool_bar);
        mSwipeRefreshLayout = findViewById(R.id.layer_refresher);
        mRecyclerView       = findViewById(R.id.recycler);
        mLoadingLayer       = findViewById(R.id.loadingLayer);
        mVoteBtn            = findViewById(R.id.btn_action);
        mVoteBtn.setOnClickListener(this);

        mProposalId = getIntent().getStringExtra("proposalId");
        mAccount    = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
        mBaseChain  = BaseChain.getChain(mAccount.baseChain);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onFetch();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mVoteDetailsAdapter = new VoteDetailsAdapter();
        mRecyclerView.setAdapter(mVoteDetailsAdapter);

        onFetch();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onUpdateView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mVoteDetailsAdapter.notifyDataSetChanged();
        mLoadingLayer.setVisibility(View.GONE);
        mVoteBtn.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }



    @Override
    public void onClick(View v) {
        if (v.equals(mVoteBtn)) {
            if (!mAccount.hasPrivateKey) {
                Dialog_WatchMode add = Dialog_WatchMode.newInstance();
                add.setCancelable(true);
                getSupportFragmentManager().beginTransaction().add(add, "dialog").commitNowAllowingStateLoss();
                return;
            }

            mBondings = getBaseDao().onSelectBondingStates(mAccount.id);
            if (mBaseChain.equals(BaseChain.COSMOS_MAIN) || mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.BAND_MAIN)) {
                if (!mProposal.proposal_status.equals(PROPOSAL_VOTING)) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_not_voting_period), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mBondings.size() == 0) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_no_bonding_no_vote), Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mBaseChain.equals(BaseChain.IRIS_MAIN)) {
                if (!mIrisProposal.value.basicProposal.proposal_status.equals(PROPOSAL_VOTING)) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_not_voting_period), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mBondings.size() == 0) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_no_bonding_no_vote), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mMyVote != null) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_iris_already_voted), Toast.LENGTH_SHORT).show();
                    return;
                }

                mBalances = getBaseDao().onSelectBalance(mAccount.id);
                if (WDp.getAvailableCoin(mBalances, TOKEN_IRIS_ATTO).compareTo(new BigDecimal("80000000000000000")) <= 0) {
                    Toast.makeText(getBaseContext(), R.string.error_not_enough_budget, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mBaseChain.equals(BaseChain.CERTIK_MAIN) || mBaseChain.equals(BaseChain.CERTIK_TEST)) {
                if (!mProposal.proposal_status.equals(PROPOSAL_VOTING)) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_not_voting_period), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mBondings.size() == 0) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_no_bonding_no_vote), Toast.LENGTH_SHORT).show();
                    return;
                }

                mBalances = getBaseDao().onSelectBalance(mAccount.id);
                if (WDp.getAvailableCoin(mBalances, TOKEN_CERTIK).compareTo(new BigDecimal("5000")) <= 0) {
                    Toast.makeText(getBaseContext(), R.string.error_not_enough_budget, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mBaseChain.equals(BaseChain.IOV_MAIN)) {
                if (!mProposal.proposal_status.equals(PROPOSAL_VOTING)) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_not_voting_period), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mBondings.size() == 0) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_no_bonding_no_vote), Toast.LENGTH_SHORT).show();
                    return;
                }

                mBalances = getBaseDao().onSelectBalance(mAccount.id);
                if (WDp.getAvailableCoin(mBalances, TOKEN_IOV).compareTo(new BigDecimal("100000")) <= 0) {
                    Toast.makeText(getBaseContext(), R.string.error_not_enough_budget, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mBaseChain.equals(BaseChain.SECRET_MAIN)) {
                if (!mProposal.proposal_status.equals(PROPOSAL_VOTING)) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_not_voting_period), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mBondings.size() == 0) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_no_bonding_no_vote), Toast.LENGTH_SHORT).show();
                    return;
                }

                mBalances = getBaseDao().onSelectBalance(mAccount.id);
                if (WDp.getAvailableCoin(mBalances, TOKEN_SECRET).compareTo(new BigDecimal("25000")) <= 0) {
                    Toast.makeText(getBaseContext(), R.string.error_not_enough_budget, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mBaseChain.equals(BaseChain.AKASH_MAIN)) {
                if (!mProposal.proposal_status.equals(PROPOSAL_VOTING)) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_not_voting_period), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mBondings.size() == 0) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_no_bonding_no_vote), Toast.LENGTH_SHORT).show();
                    return;
                }

                mBalances = getBaseDao().onSelectBalance(mAccount.id);
                if (WDp.getAvailableCoin(mBalances, TOKEN_AKASH).compareTo(new BigDecimal("2500")) <= 0) {
                    Toast.makeText(getBaseContext(), R.string.error_not_enough_budget, Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            Intent intent = new Intent(VoteDetailsActivity.this, VoteActivity.class);
            intent.putExtra("proposalId", mProposalId);
            intent.putExtra("title", getProposalTitle());
            intent.putExtra("proposer", getProposer());
            startActivity(intent);
        }
    }

    private String getProposalTitle() {
        if (mBaseChain.equals(BaseChain.COSMOS_MAIN) || mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.BAND_MAIN) || mBaseChain.equals(BaseChain.CERTIK_MAIN) ||
                mBaseChain.equals(BaseChain.CERTIK_TEST) || mBaseChain.equals(BaseChain.IOV_MAIN) || mBaseChain.equals(BaseChain.AKASH_MAIN) || mBaseChain.equals(BaseChain.SECRET_MAIN)) {
            return mProposal.getTitle();

        } else if (mBaseChain.equals(BaseChain.IRIS_MAIN)) {
            return mIrisProposal.getTitle();
        }
        return "";
    }

    private String getProposer() {
        if (mBaseChain.equals(BaseChain.COSMOS_MAIN) || mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.BAND_MAIN) || mBaseChain.equals(BaseChain.CERTIK_MAIN) ||
                mBaseChain.equals(BaseChain.CERTIK_TEST) || mBaseChain.equals(BaseChain.IOV_MAIN) || mBaseChain.equals(BaseChain.AKASH_MAIN) || mBaseChain.equals(BaseChain.SECRET_MAIN)) {
            return mProposer;

        } else if (mBaseChain.equals(BaseChain.IRIS_MAIN)) {
            return mIrisProposal.value.basicProposal.proposer;
        }
        return "";

    }


    public void onFetch() {
        if (mBaseChain.equals(BaseChain.COSMOS_MAIN) || mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.BAND_MAIN) || mBaseChain.equals(BaseChain.CERTIK_MAIN) ||
                mBaseChain.equals(BaseChain.CERTIK_TEST)|| mBaseChain.equals(BaseChain.IOV_MAIN) || mBaseChain.equals(BaseChain.AKASH_MAIN) || mBaseChain.equals(BaseChain.SECRET_MAIN)) {
            this.mTaskCount = 5;
            new ProposalDetailTask(getBaseApplication(), this, mProposalId, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new ProposalTallyTask(getBaseApplication(), this, mProposalId, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new ProposalVotedListTask(getBaseApplication(), this, mProposalId, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new MyVoteCheckTask(getBaseApplication(), this, mProposalId, mAccount.address, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new ProposalProposerTask(getBaseApplication(), this, mProposalId, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else if (mBaseChain.equals(BaseChain.IRIS_MAIN)) {
            this.mTaskCount = 2;
            new IrisProposalDetailTask(getBaseApplication(), this, mProposalId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new IrisVoteListTask(getBaseApplication(), this, mProposalId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    @Override
    public void onTaskResponse(TaskResult result) {
        mTaskCount--;
        if (result.taskType == TASK_FETCH_PROPOSAL_DETAIL) {
            mProposal = (Proposal)result.resultData;

        } else if (result.taskType == TASK_FETCH_PROPOSAL_TALLY) {
            mTally = (Tally) result.resultData;

        } else if (result.taskType == TASK_FETCH_PROPOSAL_VOTED) {
            mVotes = (ArrayList<Vote>)result.resultData;

        } else if (result.taskType == TASK_FETCH_MY_VOTE) {
            mMyVote = (Vote)result.resultData;

        } else if (result.taskType == TASK_FETCH_PROPOSAL_PROPOSER) {
            mProposer = (String)result.resultData;

        } else if (result.taskType == TASK_IRIS_PROPOSAL_DETAIL) {
            mIrisProposal = (IrisProposal)result.resultData;

        } else if (result.taskType == TASK_FETCH_IRIS_VOTE_LIST) {
            mVotes = (ArrayList<Vote>)result.resultData;
            mMyVote = WUtil.getMyVote(mVotes, mAccount.address);
        }

        if (mTaskCount == 0) {
            onUpdateView();
        }
    }



    private class VoteDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_TX_INFO = 0;
        private static final int TYPE_TX_TALLY = 1;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if (viewType == TYPE_TX_INFO) {
                return new VoteInfoHolder(getLayoutInflater().inflate(R.layout.item_vote_info, viewGroup, false));
            } else {
                return new VoteTallyHolder(getLayoutInflater().inflate(R.layout.item_vote_tally, viewGroup, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            if (position == 0) {
                onBindVoteInfo(viewHolder);
            } else if (position == 1) {
                onBindVoteTally(viewHolder);
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_TX_INFO;
            } else {
                return TYPE_TX_TALLY;
            }
        }

        private void onBindVoteInfo(RecyclerView.ViewHolder viewHolder) {
            final VoteInfoHolder holder = (VoteInfoHolder)viewHolder;
            if ((mBaseChain.equals(BaseChain.COSMOS_MAIN) || mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.BAND_MAIN) || mBaseChain.equals(BaseChain.CERTIK_MAIN) ||
                    mBaseChain.equals(BaseChain.CERTIK_TEST) || mBaseChain.equals(BaseChain.IOV_MAIN) || mBaseChain.equals(BaseChain.AKASH_MAIN) || mBaseChain.equals(BaseChain.SECRET_MAIN)) && mProposal != null) {
                holder.itemStatusImg.setImageDrawable(mProposal.getStatusImg(getBaseContext()));
                holder.itemStatusTxt.setText(mProposal.proposal_status);
                holder.itemTitle.setText(mProposal.getTitle());
                holder.itemProposer.setText(mProposer);
                holder.itemType.setText(mProposal.getType());
                holder.itemStartTime.setText(mProposal.getStartTime(getBaseContext()));
                holder.itemFinishTime.setText(mProposal.getEndTime(getBaseContext()));
                holder.itemMsg.setText(mProposal.content.value.description);
                if (mProposal.content.value.amount != null) {
                    ArrayList<Coin> requestCoin = mProposal.content.value.amount;
                    WDp.showCoinDp(getBaseContext(), requestCoin.get(0), holder.itemRequestAmountDenom, holder.itemRequestAmount, mBaseChain);
                    holder.itemRequestLayer.setVisibility(View.VISIBLE);
                }

            } else if (mBaseChain.equals(BaseChain.IRIS_MAIN) && mIrisProposal != null) {
                holder.itemStatusImg.setImageDrawable(mIrisProposal.getStatusImg(getBaseContext()));
                holder.itemStatusTxt.setText(mIrisProposal.value.basicProposal.proposal_status);
                holder.itemTitle.setText(mIrisProposal.getTitle());
                holder.itemProposer.setText(mIrisProposal.value.basicProposal.proposer);
                holder.itemType.setText(mIrisProposal.getType());
                holder.itemStartTime.setText(mIrisProposal.getStartTime(getBaseContext()));
                holder.itemFinishTime.setText(mIrisProposal.getEndTime(getBaseContext()));
                holder.itemMsg.setText(mIrisProposal.value.basicProposal.description);

            }
            holder.itemWebBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onExplorerLink();
                }
            });

            holder.itemExpendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.itemMsg.getMaxLines() == 500) {
                        holder.itemMsg.setMaxLines(3);
                        holder.itemExpendBtn.setImageDrawable(getDrawable(R.drawable.arrow_down_gr));

                    } else {
                        holder.itemMsg.setMaxLines(500);
                        holder.itemExpendBtn.setImageDrawable(getDrawable(R.drawable.arrow_up_gr));
                    }
                    mVoteDetailsAdapter.notifyDataSetChanged();
                }
            });

        }

        private void onBindVoteTally(RecyclerView.ViewHolder viewHolder) {
            final VoteTallyHolder holder = (VoteTallyHolder)viewHolder;
            Tally tally = null;

            if ((mBaseChain.equals(BaseChain.COSMOS_MAIN) || mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.BAND_MAIN) || mBaseChain.equals(BaseChain.CERTIK_MAIN) ||
                    mBaseChain.equals(BaseChain.CERTIK_TEST) || mBaseChain.equals(BaseChain.IOV_MAIN) || mBaseChain.equals(BaseChain.AKASH_MAIN) || mBaseChain.equals(BaseChain.SECRET_MAIN)) && mTally != null) {
                tally = mTally;
                holder.itemYesProgress.setProgress(tally.getYesPer().intValue());
                holder.itemNoProgress.setProgress(tally.getNoPer().intValue());
                holder.itemVetoProgress.setProgress(tally.getVetoPer().intValue());
                holder.itemAbstainProgress.setProgress(tally.getAbstainPer().intValue());

                holder.itemYesRate.setText(WDp.getDpString(tally.getYesPer().toPlainString() + "%", 3));
                holder.itemNoRate.setText(WDp.getDpString(tally.getNoPer().toPlainString() + "%", 3));
                holder.itemVetoRate.setText(WDp.getDpString(tally.getVetoPer().toPlainString() + "%", 3));
                holder.itemAbstainRate.setText(WDp.getDpString(tally.getAbstainPer().toPlainString() + "%", 3));

                if (mProposal != null && mProposal.proposal_status.equals(PROPOSAL_VOTING)) {
                    onDisplayVote(holder);
                    holder.itemTurnoutLayer.setVisibility(View.VISIBLE);
                    holder.itemTurnout.setText(WDp.getDpString(tally.getTurnout(getBaseDao().mStakingPool).toPlainString() + "%", 3));
                }

            } else if (mBaseChain.equals(BaseChain.IRIS_MAIN) && mIrisProposal != null && mIrisProposal.hasTally()) {
                tally = mIrisProposal.value.basicProposal.tally_result ;
                holder.itemYesProgress.setProgress(tally.getYesPer().intValue());
                holder.itemNoProgress.setProgress(tally.getNoPer().intValue());
                holder.itemVetoProgress.setProgress(tally.getVetoPer().intValue());
                holder.itemAbstainProgress.setProgress(tally.getAbstainPer().intValue());

                holder.itemYesRate.setText(WDp.getDpString(tally.getYesPer().toPlainString() + "%", 3));
                holder.itemNoRate.setText(WDp.getDpString(tally.getNoPer().toPlainString() + "%", 3));
                holder.itemVetoRate.setText(WDp.getDpString(tally.getVetoPer().toPlainString() + "%", 3));
                holder.itemAbstainRate.setText(WDp.getDpString(tally.getAbstainPer().toPlainString() + "%", 3));
                onDisplayVote(holder);
            }

            if (mMyVote != null) {
                holder.itemYesCard.setBackground(getDrawable(R.drawable.box_vote_quorum));
                holder.itemNoCard.setBackground(getDrawable(R.drawable.box_vote_quorum));
                holder.itemVetoCard.setBackground(getDrawable(R.drawable.box_vote_quorum));
                holder.itemAbstainCard.setBackground(getDrawable(R.drawable.box_vote_quorum));

                if (mMyVote.option.equals(Vote.OPTION_YES)) {
                    holder.itemYesDone.setVisibility(View.VISIBLE);
                    holder.itemYesCard.setBackground(getDrawable(R.drawable.box_vote_voted));

                } else if (mMyVote.option.equals(Vote.OPTION_NO)) {
                    holder.itemNoDone.setVisibility(View.VISIBLE);
                    holder.itemNoCard.setBackground(getDrawable(R.drawable.box_vote_voted));

                } else if (mMyVote.option.equals(Vote.OPTION_VETO)) {
                    holder.itemVetoDone.setVisibility(View.VISIBLE);
                    holder.itemVetoCard.setBackground(getDrawable(R.drawable.box_vote_voted));

                } else if (mMyVote.option.equals(Vote.OPTION_ABSTAIN)) {
                    holder.itemAbstainDone.setVisibility(View.VISIBLE);
                    holder.itemAbstainCard.setBackground(getDrawable(R.drawable.box_vote_voted));

                }
            }

        }

        private void onExplorerLink() {
            Intent webintent = new Intent(VoteDetailsActivity.this, WebActivity.class);
            webintent.putExtra("voteId", mProposalId);
            webintent.putExtra("chain", mAccount.baseChain);
            startActivity(webintent);
        }

        private void onDisplayVote(VoteTallyHolder holder) {
            holder.itemYesCnt.setText(""+ WUtil.getVoterTypeCnt(mVotes, Vote.OPTION_YES));
            holder.itemNoCnt.setText(""+WUtil.getVoterTypeCnt(mVotes, Vote.OPTION_NO));
            holder.itemVetoCnt.setText(""+WUtil.getVoterTypeCnt(mVotes, Vote.OPTION_VETO));
            holder.itemAbstainCnt.setText(""+WUtil.getVoterTypeCnt(mVotes, Vote.OPTION_ABSTAIN));
            holder.itemYesCnt.setVisibility(View.VISIBLE);
            holder.itemNoCnt.setVisibility(View.VISIBLE);
            holder.itemVetoCnt.setVisibility(View.VISIBLE);
            holder.itemAbstainCnt.setVisibility(View.VISIBLE);
            holder.itemYesCntImg.setVisibility(View.VISIBLE);
            holder.itemNoCntImg.setVisibility(View.VISIBLE);
            holder.itemVetoCntImg.setVisibility(View.VISIBLE);
            holder.itemAbstainCntImg.setVisibility(View.VISIBLE);
        }



        public class VoteInfoHolder extends RecyclerView.ViewHolder {
            private ImageView itemStatusImg;
            private TextView itemStatusTxt;
            private ImageView itemWebBtn;
            private TextView itemTitle, itemProposer, itemType, itemStartTime, itemFinishTime, itemMsg;
            private ImageView itemExpendBtn;
            private RelativeLayout itemRequestLayer;
            private TextView itemRequestAmount, itemRequestAmountDenom;

            public VoteInfoHolder(@NonNull View itemView) {
                super(itemView);
                itemStatusImg = itemView.findViewById(R.id.vote_status_img);
                itemStatusTxt = itemView.findViewById(R.id.vote_status);
                itemWebBtn = itemView.findViewById(R.id.vote_detail);
                itemTitle = itemView.findViewById(R.id.vote_title);
                itemProposer = itemView.findViewById(R.id.vote_proposer);
                itemType = itemView.findViewById(R.id.vote_type);
                itemStartTime = itemView.findViewById(R.id.vote_startTime);
                itemFinishTime = itemView.findViewById(R.id.vote_endTime);
                itemMsg = itemView.findViewById(R.id.vote_msg);
                itemExpendBtn = itemView.findViewById(R.id.vote_btn_expend);
                itemRequestLayer = itemView.findViewById(R.id.request_amount_layer);
                itemRequestAmount = itemView.findViewById(R.id.request_amount);
                itemRequestAmountDenom = itemView.findViewById(R.id.request_amount_denom);
            }
        }

        public class VoteTallyHolder extends RecyclerView.ViewHolder {
            private RelativeLayout itemYesCard, itemNoCard, itemVetoCard, itemAbstainCard;
            private ImageView itemYesDone, itemNoDone, itemVetoDone, itemAbstainDone;
            private ProgressBar itemYesProgress, itemNoProgress, itemVetoProgress, itemAbstainProgress;
            private TextView itemYesRate, itemYesCnt, itemNoRate, itemNoCnt, itemVetoRate, itemVetoCnt, itemAbstainRate, itemAbstainCnt;
            private ImageView itemYesCntImg, itemNoCntImg, itemVetoCntImg, itemAbstainCntImg;
            private RelativeLayout itemTurnoutLayer;
            private TextView itemTurnout;

            public VoteTallyHolder(@NonNull View itemView) {
                super(itemView);
                itemYesCard = itemView.findViewById(R.id.card_yes);
                itemNoCard = itemView.findViewById(R.id.card_no);
                itemVetoCard = itemView.findViewById(R.id.card_veto);
                itemAbstainCard = itemView.findViewById(R.id.card_abstain);
                itemYesDone = itemView.findViewById(R.id.vote_yes_voted);
                itemNoDone = itemView.findViewById(R.id.vote_no_voted);
                itemVetoDone = itemView.findViewById(R.id.vote_veto_voted);
                itemAbstainDone = itemView.findViewById(R.id.vote_abstain_voted);
                itemYesProgress = itemView.findViewById(R.id.vote_yes_progress);
                itemNoProgress = itemView.findViewById(R.id.vote_no_progress);
                itemVetoProgress = itemView.findViewById(R.id.vote_veto_progress);
                itemAbstainProgress = itemView.findViewById(R.id.vote_abstain_progress);
                itemYesRate = itemView.findViewById(R.id.vote_yes_rate);
                itemYesCnt = itemView.findViewById(R.id.vote_yes_cnt);
                itemNoRate = itemView.findViewById(R.id.vote_no_rate);
                itemNoCnt = itemView.findViewById(R.id.vote_no_cnt);
                itemVetoRate = itemView.findViewById(R.id.vote_vetos_rate);
                itemVetoCnt = itemView.findViewById(R.id.vote_veto_cnt);
                itemAbstainRate = itemView.findViewById(R.id.vote_abstain_rate);
                itemAbstainCnt = itemView.findViewById(R.id.vote_abstain_cnt);
                itemYesCntImg = itemView.findViewById(R.id.vote_yes_cnt_img);
                itemNoCntImg = itemView.findViewById(R.id.vote_no_cnt_img);
                itemVetoCntImg = itemView.findViewById(R.id.vote_veto_cnt_img);
                itemAbstainCntImg = itemView.findViewById(R.id.vote_abstain_cnt_img);
                itemTurnoutLayer = itemView.findViewById(R.id.turnout_layer);
                itemTurnout = itemView.findViewById(R.id.current_turnout);
            }
        }
    }

}

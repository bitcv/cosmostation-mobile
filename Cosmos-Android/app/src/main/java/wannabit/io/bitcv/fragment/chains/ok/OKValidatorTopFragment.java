package wannabit.io.bitcv.fragment.chains.ok;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import wannabit.io.bitcv.activities.chains.ok.OKValidatorListActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.model.type.Validator;
import wannabit.io.bitcv.network.res.ResOkStaking;
import wannabit.io.bitcv.utils.WDp;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.utils.WUtil;

public class OKValidatorTopFragment extends BaseFragment {

    private SwipeRefreshLayout          mSwipeRefreshLayout;
    private RecyclerView                mRecyclerView;
    private OKTopValidatorAdapter       mOKTopValidatorAdapter;
    private TextView                    mValidatorSize;

    private ArrayList<Validator>        mTopValidators = new ArrayList<>();
    private ResOkStaking mOkDeposit;

    public static OKValidatorTopFragment newInstance(Bundle bundle) {
        OKValidatorTopFragment fragment = new OKValidatorTopFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_validator_other, container, false);
        mSwipeRefreshLayout     = rootView.findViewById(R.id.layer_refresher);
        mRecyclerView           = rootView.findViewById(R.id.recycler);
        mValidatorSize          = rootView.findViewById(R.id.validator_cnt);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSActivity().onFetchAllData();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(50);
        mRecyclerView.setDrawingCacheEnabled(true);
        mOKTopValidatorAdapter = new OKTopValidatorAdapter();
        mRecyclerView.setAdapter(mOKTopValidatorAdapter);

        onRefreshTab();

        return rootView;
    }

    @Override
    public void onRefreshTab() {
        if(!isAdded()) return;
        mTopValidators  = getBaseDao().mTopValidators;
        mOkDeposit      = getBaseDao().mOkStaking;
        WLog.w("mTopValidators "+ mTopValidators.size());

        mValidatorSize.setText(""+mTopValidators.size());
        onSortValidator();

        mOKTopValidatorAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBusyFetch() {
        if(!isAdded()) return;
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private OKValidatorListActivity getSActivity() {
        return (OKValidatorListActivity)getBaseActivity();
    }



    public class OKTopValidatorAdapter extends RecyclerView.Adapter<OKTopValidatorAdapter.OKTopValidatorHolder> {

        @NonNull
        @Override
        public OKTopValidatorHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new OKTopValidatorHolder(getLayoutInflater().inflate(R.layout.item_ok_validator, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull OKTopValidatorHolder holder, int position) {
            final Validator validator  = mTopValidators.get(position);
            if (getSActivity().mBaseChain.equals(BaseChain.OK_TEST)) {
                holder.itemTvMoniker.setText(validator.description.moniker);
                holder.itemTvVotingPower.setText(WDp.getDpAmount2(getContext(), new BigDecimal(validator.delegator_shares), 0, 8));
                holder.itemTvCommission.setText(WDp.getCommissionRate(validator.commission.commission_rates.rate));

                String imgUrl = validator.description.identity;
                if (!TextUtils.isEmpty(imgUrl) && imgUrl.startsWith("logo|||")) {
                    imgUrl = imgUrl.replace("logo|||" , "");
                    try {
                        Picasso.get().load(imgUrl)
                                .fit().placeholder(R.drawable.validator_none_img).error(R.drawable.validator_none_img)
                                .into(holder.itemAvatar);
                    } catch (Exception e){}
                } else {
                    holder.itemAvatar.setImageDrawable(getResources().getDrawable(R.drawable.validator_none_img));
                }

                if(validator.jailed) {
                    holder.itemAvatar.setBorderColor(getResources().getColor(R.color.colorRed));
                    holder.itemRevoked.setVisibility(View.VISIBLE);
                } else {
                    holder.itemAvatar.setBorderColor(getResources().getColor(R.color.colorGray3));
                    holder.itemRevoked.setVisibility(View.GONE);
                }

                if (checkIsMyValidator(validator.operator_address)) {
                    holder.itemRoot.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgOkex));
                }

            }
        }

        @Override
        public int getItemCount() {
            return mTopValidators.size();
        }

        public class OKTopValidatorHolder extends RecyclerView.ViewHolder {
            CardView            itemRoot;
            CircleImageView     itemAvatar;
            ImageView           itemRevoked;
            ImageView           itemFree;
            TextView            itemTvMoniker;
            TextView            itemTvVotingPower;
            TextView            itemTvCommission;

            public OKTopValidatorHolder(@NonNull View itemView) {
                super(itemView);
                itemRoot            = itemView.findViewById(R.id.card_validator);
                itemAvatar          = itemView.findViewById(R.id.avatar_validator);
                itemRevoked         = itemView.findViewById(R.id.avatar_validator_revoke);
                itemFree            = itemView.findViewById(R.id.avatar_validator_free);
                itemTvMoniker       = itemView.findViewById(R.id.moniker_validator);
                itemTvVotingPower = itemView.findViewById(R.id.delegate_power_validator);
                itemTvCommission    = itemView.findViewById(R.id.delegate_commission_validator);
            }
        }
    }


    private boolean checkIsMyValidator(String valAddress){
        boolean myVal = false;
        if (mOkDeposit == null || mOkDeposit.validator_address == null) {
            return myVal;
        }
        for (String val:mOkDeposit.validator_address) {
            if (val.equals(valAddress)){
                return true;
            }
        }
        return myVal;
    }

    public void onSortValidator() {
        WUtil.onSortByOKValidatorPower(mTopValidators);
    }
}

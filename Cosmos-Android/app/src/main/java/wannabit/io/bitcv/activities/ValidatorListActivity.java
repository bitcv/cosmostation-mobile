package wannabit.io.bitcv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseActivity;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.dao.Balance;
import wannabit.io.bitcv.dao.BondingState;
import wannabit.io.bitcv.dialog.Dialog_WatchMode;
import wannabit.io.bitcv.fragment.ValidatorAllFragment;
import wannabit.io.bitcv.fragment.ValidatorMyFragment;
import wannabit.io.bitcv.fragment.ValidatorOtherFragment;
import wannabit.io.bitcv.model.type.Validator;
import wannabit.io.bitcv.utils.FetchCallBack;
import wannabit.io.bitcv.utils.WDp;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.utils.WUtil;

import static wannabit.io.bitcv.base.BaseChain.AKASH_MAIN;
import static wannabit.io.bitcv.base.BaseChain.SECRET_MAIN;
import static wannabit.io.bitcv.base.BaseConstant.FEE_AKASH_GAS_RATE_AVERAGE;
import static wannabit.io.bitcv.base.BaseConstant.FEE_CERTIK_GAS_RATE_AVERAGE;
import static wannabit.io.bitcv.base.BaseConstant.FEE_IOV_GAS_RATE_AVERAGE;
import static wannabit.io.bitcv.base.BaseConstant.SECRET_GAS_FEE_RATE_AVERAGE;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_AKASH;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_ATOM;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_BAND;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_CERTIK;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_IOV;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_IOV_TEST;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_KAVA;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_SECRET;

public class ValidatorListActivity extends BaseActivity implements FetchCallBack {

    private Toolbar                     mToolbar;
    private TextView                    mToolbarTitle;
    private ViewPager                   mValidatorPager;
    private TabLayout                   mValidatorTapLayer;
    private ValidatorPageAdapter        mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator_list);
        mToolbar            = findViewById(R.id.tool_bar);
        mToolbarTitle       = findViewById(R.id.toolbar_title);
        mValidatorTapLayer  = findViewById(R.id.validator_tab);
        mValidatorPager     = findViewById(R.id.validator_view_pager);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAccount = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
        mBaseChain = BaseChain.getChain(mAccount.baseChain);

        mAllValidators = getBaseDao().mAllValidators;
        mTopValidators = getBaseDao().mTopValidators;
        mOtherValidators = getBaseDao().mOtherValidators;
        mMyValidators = getBaseDao().mMyValidators;
        mStakingPool = getBaseDao().mStakingPool;
        mIrisStakingPool = getBaseDao().mIrisStakingPool;
        mProvisions = getBaseDao().mProvisions;
        mInflation = getBaseDao().mInflation;


        mRewards = getIntent().getParcelableArrayListExtra("rewards");
        mIrisReward = getIntent().getParcelableExtra("irisreward");
        mBondings = getBaseDao().onSelectBondingStates(mAccount.id);
        WLog.w("mBondings "+mBondings.size());

        mPageAdapter = new ValidatorPageAdapter(getSupportFragmentManager());
        mValidatorPager.setAdapter(mPageAdapter);
        mValidatorTapLayer.setupWithViewPager(mValidatorPager);
        mValidatorTapLayer.setTabRippleColor(null);

        View tab0 = LayoutInflater.from(this).inflate(R.layout.view_tab_myvalidator, null);
        TextView tabItemText0 = tab0.findViewById(R.id.tabItemText);
        tabItemText0.setText(R.string.str_my_validators);
        tabItemText0.setTextColor(WDp.getTabColor(this, mBaseChain));
        mValidatorTapLayer.getTabAt(0).setCustomView(tab0);

        View tab1 = LayoutInflater.from(this).inflate(R.layout.view_tab_myvalidator, null);
        TextView tabItemText1 = tab1.findViewById(R.id.tabItemText);
        tabItemText1.setTextColor(WDp.getTabColor(this, mBaseChain));
        tabItemText1.setText(R.string.str_top_100_validators);
        mValidatorTapLayer.getTabAt(1).setCustomView(tab1);

        View tab2 = LayoutInflater.from(this).inflate(R.layout.view_tab_myvalidator, null);
        TextView tabItemText2 = tab2.findViewById(R.id.tabItemText);
        tabItemText2.setTextColor(WDp.getTabColor(this, mBaseChain));
        tabItemText2.setText(R.string.str_other_validators);
        mValidatorTapLayer.getTabAt(2).setCustomView(tab2);

        mValidatorTapLayer.setTabIconTint(WDp.getChainTintColor(this, mBaseChain));
        mValidatorTapLayer.setSelectedTabIndicatorColor(WDp.getChainColor(this, mBaseChain));

        mValidatorPager.setOffscreenPageLimit(3);
        mValidatorPager.setCurrentItem(0, false);

        mValidatorPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageScrollStateChanged(int i) { }

            @Override
            public void onPageSelected(int i) {
                mPageAdapter.mFragments.get(i).onRefreshTab();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(mAccount == null) finish();
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

    public void onStartValidatorDetail(Validator validator) {
        Intent intent = new Intent(ValidatorListActivity.this, ValidatorActivity.class);
        intent.putExtra("validator", validator);
        startActivity(intent);
    }

    public void onStartRewardAll() {
        if(!mAccount.hasPrivateKey) {
            Dialog_WatchMode add = Dialog_WatchMode.newInstance();
            add.setCancelable(true);
            getSupportFragmentManager().beginTransaction().add(add, "dialog").commitNowAllowingStateLoss();
            return;
        }

        ArrayList<Validator> toClaimValidators = new ArrayList<>();

        if (mBaseChain.equals(BaseChain.COSMOS_MAIN)) {
            if (mRewards == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }

            BigDecimal rewardSum = BigDecimal.ZERO;
            for (BondingState bond:mBondings) {
                if (WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_ATOM).compareTo(BigDecimal.ONE) >= 0) {
                    if (WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress) != null) {
                        toClaimValidators.add(WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress));
                        rewardSum = rewardSum.add(WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_ATOM));
                    }
                }
            }

            if (toClaimValidators.size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }

            WUtil.onSortByOnlyReward(toClaimValidators, mRewards, TOKEN_ATOM);
            if (toClaimValidators.size() >= 17) {
                toClaimValidators = new ArrayList<>(mMyValidators.subList(0,16));
                Toast.makeText(getBaseContext(), R.string.str_multi_reward_max_16, Toast.LENGTH_SHORT).show();
            }

            if (rewardSum.compareTo(BigDecimal.ONE) <= 0) {
                Toast.makeText(getBaseContext(), R.string.error_small_reward, Toast.LENGTH_SHORT).show();
                return;
            }


        } else if (mBaseChain.equals(BaseChain.IRIS_MAIN)) {
            BigDecimal estimateReward = BigDecimal.ZERO;

            if (mIrisReward == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }

            for (Validator validator:mAllValidators) {
                if (mIrisReward.getPerValReward(validator.operator_address).compareTo(BigDecimal.ONE) >= 0) {
                    toClaimValidators.add(validator);
                }
            }

            if (toClaimValidators.size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }

//            WUtil.onSortIrisOnlyByReward(mMyValidators, mIrisReward);

            for (Validator validator:toClaimValidators) {
                estimateReward = estimateReward.add(mIrisReward.getPerValReward(validator.operator_address));
            }

            BigDecimal estimateGasAmount = (new BigDecimal(BaseConstant.FEE_IRIS_GAS_AMOUNT_REWARD_MUX).multiply(new BigDecimal(""+toClaimValidators.size()))).add(new BigDecimal(BaseConstant.FEE_IRIS_GAS_AMOUNT_REWARD_BASE));
            BigDecimal estimateFee = estimateGasAmount.multiply(new BigDecimal(BaseConstant.FEE_IRIS_GAS_RATE_AVERAGE)).movePointRight(18).setScale(0);

//            WLog.w("estimateReward " + estimateReward);
//            WLog.w("estimateGasAmount " + estimateGasAmount);
//            WLog.w("estimateFee " + estimateFee);

            ArrayList<Balance> balances = getBaseDao().onSelectBalance(mAccount.id);
            boolean hasbalance = false;
            for (Balance balance:balances) {
                if(balance.symbol.equals(BaseConstant.TOKEN_IRIS_ATTO) && ((balance.balance.compareTo(estimateFee)) > 0)) {
                    hasbalance  = true;
                }
            }
            if(!hasbalance){
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward_all, Toast.LENGTH_SHORT).show();
                return;
            }

            if (estimateReward.compareTo(estimateFee) <= 0) {
                Toast.makeText(getBaseContext(), R.string.error_small_reward, Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.KAVA_TEST)) {
            if (mRewards == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }

            BigDecimal rewardSum = BigDecimal.ZERO;
            for (BondingState bond:mBondings) {
                if (WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_KAVA).compareTo(BigDecimal.ONE) >= 0) {
                    if (WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress) != null) {
                        toClaimValidators.add(WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress));
                        rewardSum = rewardSum.add(WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_KAVA));
                    }
                }
            }

            if (toClaimValidators.size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }

            WUtil.onSortByOnlyReward(toClaimValidators, mRewards, TOKEN_KAVA);
            if (toClaimValidators.size() >= 17) {
                toClaimValidators = new ArrayList<>(mMyValidators.subList(0,16));
                Toast.makeText(getBaseContext(), R.string.str_multi_reward_max_16, Toast.LENGTH_SHORT).show();
            }

            if (rewardSum.compareTo(BigDecimal.ONE) <= 0) {
                Toast.makeText(getBaseContext(), R.string.error_small_reward, Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (mBaseChain.equals(BaseChain.BAND_MAIN)) {
            if (mRewards == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }

            BigDecimal rewardSum = BigDecimal.ZERO;
            for (BondingState bond:mBondings) {
                if (WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_BAND).compareTo(BigDecimal.ONE) >= 0) {
                    if (WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress) != null) {
                        toClaimValidators.add(WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress));
                        rewardSum = rewardSum.add(WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_BAND));
                    }
                }
            }
            if (toClaimValidators.size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            WUtil.onSortByOnlyReward(toClaimValidators, mRewards, TOKEN_BAND);
            if (toClaimValidators.size() >= 17) {
                toClaimValidators = new ArrayList<>(mMyValidators.subList(0,16));
                Toast.makeText(getBaseContext(), R.string.str_multi_reward_max_16, Toast.LENGTH_SHORT).show();
            }

            if (rewardSum.compareTo(BigDecimal.ONE) <= 0) {
                Toast.makeText(getBaseContext(), R.string.error_small_reward, Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (mBaseChain.equals(BaseChain.IOV_MAIN)) {
            //only collect over 0.15 iov
            if (mRewards == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            BigDecimal rewardSum = BigDecimal.ZERO;
            for (BondingState bond:mBondings) {
                if (WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_IOV).compareTo(new BigDecimal("150000")) >= 0) {
                    if (WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress) != null) {
                        toClaimValidators.add(WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress));
                        rewardSum = rewardSum.add(WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_IOV));
                    }
                }
            }
            if (toClaimValidators.size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            WUtil.onSortByOnlyReward(toClaimValidators, mRewards, TOKEN_IOV);
            if (toClaimValidators.size() >= 17) {
                toClaimValidators = new ArrayList<>(mMyValidators.subList(0,16));
                Toast.makeText(getBaseContext(), R.string.str_multi_reward_max_16, Toast.LENGTH_SHORT).show();
            }

            ArrayList<String> rewardGasFees = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.gas_multi_reward_kava)));
            BigDecimal estimateGasAmount = new BigDecimal(rewardGasFees.get(toClaimValidators.size() - 1));
            BigDecimal estimateFeeAmount = estimateGasAmount.multiply(new BigDecimal(FEE_IOV_GAS_RATE_AVERAGE)).setScale(0);
            BigDecimal available = mAccount.getIovBalance();

            if (available.compareTo(estimateFeeAmount) <= 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (mBaseChain.equals(BaseChain.IOV_TEST)) {
            //only collect over 0.15 iov
            if (mRewards == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            BigDecimal rewardSum = BigDecimal.ZERO;
            for (BondingState bond:mBondings) {
                if (WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_IOV_TEST).compareTo(new BigDecimal("150000")) >= 0) {
                    if (WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress) != null) {
                        toClaimValidators.add(WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress));
                        rewardSum = rewardSum.add(WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_IOV_TEST));
                    }
                }
            }
            if (toClaimValidators.size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            WUtil.onSortByOnlyReward(toClaimValidators, mRewards, TOKEN_IOV_TEST);
            if (toClaimValidators.size() >= 17) {
                toClaimValidators = new ArrayList<>(mMyValidators.subList(0,16));
                Toast.makeText(getBaseContext(), R.string.str_multi_reward_max_16, Toast.LENGTH_SHORT).show();
            }

            ArrayList<String> rewardGasFees = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.gas_multi_reward_kava)));
            BigDecimal estimateGasAmount = new BigDecimal(rewardGasFees.get(toClaimValidators.size() - 1));
            BigDecimal estimateFeeAmount = estimateGasAmount.multiply(new BigDecimal(FEE_IOV_GAS_RATE_AVERAGE)).setScale(0);
            BigDecimal available = mAccount.getIovBalance();

            if (available.compareTo(estimateFeeAmount) <= 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (mBaseChain.equals(BaseChain.CERTIK_MAIN) || mBaseChain.equals(BaseChain.CERTIK_TEST)) {
            if (mRewards == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            BigDecimal rewardSum = BigDecimal.ZERO;
            for (BondingState bond:mBondings) {
                if (WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_CERTIK).compareTo(new BigDecimal("7500")) >= 0) {
                    if (WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress) != null) {
                        toClaimValidators.add(WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress));
                        rewardSum = rewardSum.add(WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_CERTIK));
                    }
                }
            }
            if (toClaimValidators.size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            WUtil.onSortByOnlyReward(toClaimValidators, mRewards, TOKEN_CERTIK);
            if (toClaimValidators.size() >= 17) {
                toClaimValidators = new ArrayList<>(mMyValidators.subList(0,16));
                Toast.makeText(getBaseContext(), R.string.str_multi_reward_max_16, Toast.LENGTH_SHORT).show();
            }

            ArrayList<String> rewardGasFees = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.gas_multi_reward_kava)));
            BigDecimal estimateGasAmount = new BigDecimal(rewardGasFees.get(toClaimValidators.size() - 1));
            BigDecimal estimateFeeAmount = estimateGasAmount.multiply(new BigDecimal(FEE_CERTIK_GAS_RATE_AVERAGE)).setScale(0);
            BigDecimal available = mAccount.getTokenBalance(TOKEN_CERTIK);

            if (available.compareTo(estimateFeeAmount) <= 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (mBaseChain.equals(SECRET_MAIN)) {
            if (mRewards == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            BigDecimal rewardSum = BigDecimal.ZERO;
            for (BondingState bond:mBondings) {
                if (WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_SECRET).compareTo(new BigDecimal("37500")) >= 0) {
                    if (WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress) != null) {
                        toClaimValidators.add(WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress));
                        rewardSum = rewardSum.add(WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_SECRET));
                    }
                }
            }
            if (toClaimValidators.size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            WUtil.onSortByOnlyReward(toClaimValidators, mRewards, TOKEN_SECRET);
            if (toClaimValidators.size() >= 17) {
                toClaimValidators = new ArrayList<>(mMyValidators.subList(0,16));
                Toast.makeText(getBaseContext(), R.string.str_multi_reward_max_16, Toast.LENGTH_SHORT).show();
            }

            ArrayList<String> rewardGasFees = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.gas_multi_reward_kava)));
            BigDecimal estimateGasAmount = new BigDecimal(rewardGasFees.get(toClaimValidators.size() - 1));
            BigDecimal estimateFeeAmount = estimateGasAmount.multiply(new BigDecimal(SECRET_GAS_FEE_RATE_AVERAGE)).setScale(0);
            BigDecimal available = mAccount.getTokenBalance(TOKEN_SECRET);

            if (available.compareTo(estimateFeeAmount) <= 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (mBaseChain.equals(AKASH_MAIN)) {
            if (mRewards == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            BigDecimal rewardSum = BigDecimal.ZERO;
            for (BondingState bond:mBondings) {
                if (WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_AKASH).compareTo(new BigDecimal("3750")) >= 0) {
                    if (WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress) != null) {
                        toClaimValidators.add(WUtil.selectValidatorByAddr(mMyValidators, bond.validatorAddress));
                        rewardSum = rewardSum.add(WDp.getValidatorReward(mRewards, bond.validatorAddress, TOKEN_AKASH));
                    }
                }
            }
            if (toClaimValidators.size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_reward, Toast.LENGTH_SHORT).show();
                return;
            }
            WUtil.onSortByOnlyReward(toClaimValidators, mRewards, TOKEN_AKASH);
            if (toClaimValidators.size() >= 17) {
                toClaimValidators = new ArrayList<>(mMyValidators.subList(0,16));
                Toast.makeText(getBaseContext(), R.string.str_multi_reward_max_16, Toast.LENGTH_SHORT).show();
            }

            ArrayList<String> rewardGasFees = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.gas_multi_reward_kava)));
            BigDecimal estimateGasAmount = new BigDecimal(rewardGasFees.get(toClaimValidators.size() - 1));
            BigDecimal estimateFeeAmount = estimateGasAmount.multiply(new BigDecimal(FEE_AKASH_GAS_RATE_AVERAGE)).setScale(0);
            BigDecimal available = mAccount.getTokenBalance(TOKEN_AKASH);

            if (available.compareTo(estimateFeeAmount) <= 0) {
                Toast.makeText(getBaseContext(), R.string.error_not_enough_fee, Toast.LENGTH_SHORT).show();
                return;
            }

        } else {
            Toast.makeText(getBaseContext(), R.string.error_not_yet, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent claimReward = new Intent(ValidatorListActivity.this, ClaimRewardActivity.class);
        claimReward.putExtra("opAddresses", toClaimValidators);
        startActivity(claimReward);
    }

    public void onFetchAllData() {
        onFetchAccountInfo(this);
    }

    @Override
    public void fetchFinished() {
        if (!isFinishing()) {
            mAllValidators = getBaseDao().mAllValidators;
            mTopValidators = getBaseDao().mTopValidators;
            mOtherValidators = getBaseDao().mOtherValidators;
            mMyValidators = getBaseDao().mMyValidators;
            mStakingPool = getBaseDao().mStakingPool;
            mIrisStakingPool = getBaseDao().mIrisStakingPool;
            mProvisions = getBaseDao().mProvisions;
            mInflation = getBaseDao().mInflation;

            onHideWaitDialog();
            mPageAdapter.mCurrentFragment.onRefreshTab();
        }

    }

    @Override
    public void fetchBusy() {
        if(!isFinishing()) {
            onHideWaitDialog();
            mPageAdapter.mCurrentFragment.onBusyFetch();
        }

    }

    private class ValidatorPageAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public ValidatorPageAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(ValidatorMyFragment.newInstance(null));
            mFragments.add(ValidatorAllFragment.newInstance(null));
            mFragments.add(ValidatorOtherFragment.newInstance(null));
        }

        @Override
        public BaseFragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((BaseFragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        public BaseFragment getCurrentFragment() {
            return mCurrentFragment;
        }

        public ArrayList<BaseFragment> getFragments() {
            return mFragments;
        }
    }
}

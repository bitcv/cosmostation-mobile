package wannabit.io.bitcv.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseActivity;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.dao.BondingState;
import wannabit.io.bitcv.dialog.Dialog_VestingAccount;
import wannabit.io.bitcv.fragment.RedelegateStep0Fragment;
import wannabit.io.bitcv.fragment.RedelegateStep1Fragment;
import wannabit.io.bitcv.fragment.RedelegateStep2Fragment;
import wannabit.io.bitcv.fragment.RedelegateStep3Fragment;
import wannabit.io.bitcv.fragment.RedelegateStep4Fragment;
import wannabit.io.bitcv.model.type.Coin;
import wannabit.io.bitcv.model.type.Fee;
import wannabit.io.bitcv.model.type.Validator;
import wannabit.io.bitcv.network.res.ResLcdIrisRedelegate;
import wannabit.io.bitcv.task.FetchTask.ValidatorInfoBondedTask;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;
import wannabit.io.bitcv.utils.WDp;
import wannabit.io.bitcv.utils.WUtil;

import static wannabit.io.bitcv.base.BaseChain.KAVA_MAIN;
import static wannabit.io.bitcv.base.BaseConstant.CONST_PW_TX_SIMPLE_REDELEGATE;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_KAVA;

public class RedelegateActivity extends BaseActivity implements TaskListener {

    private ImageView                       mChainBg;
    private Toolbar                         mToolbar;
    private TextView                        mTitle;
    private ImageView                       mIvStep;
    private TextView                        mTvStep;
    private ViewPager                       mViewPager;
    private RedelegatePageAdapter           mPageAdapter;


    public ArrayList<Validator>             mToValidators = new ArrayList<>();
    public BondingState                     mBondingState;
    public Validator                        mFromValidator;
    public Validator                        mToValidator;
    public Coin                             mReDelegateAmount;
    public String                           mReDelegateMemo;
    public Fee                              mReDelegateFee;

    public ArrayList<ResLcdIrisRedelegate>  mIrisRedelegateState;

    private int                             mTaskCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        mChainBg            = findViewById(R.id.chain_bg);
        mToolbar            = findViewById(R.id.tool_bar);
        mTitle              = findViewById(R.id.toolbar_title);
        mIvStep             = findViewById(R.id.send_step);
        mTvStep             = findViewById(R.id.send_step_msg);
        mViewPager          = findViewById(R.id.view_pager);
        mTitle.setText(getString(R.string.str_redelegate_c));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIvStep.setImageDrawable(getDrawable(R.drawable.step_1_img));
        mTvStep.setText(getString(R.string.str_redelegate_step_0));

        mAccount = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
        mBaseChain = BaseChain.getChain(mAccount.baseChain);

        mStakingPool = getBaseDao().mStakingPool;
        mIrisStakingPool = getBaseDao().mIrisStakingPool;
        mProvisions = getBaseDao().mProvisions;

        mFromValidator          = getIntent().getParcelableExtra("validator");
        mIrisRedelegateState    = getIntent().getParcelableArrayListExtra("irisReState");
        mBondingState           = getBaseDao().onSelectBondingState(mAccount.id, mFromValidator.operator_address);

        mPageAdapter = new RedelegatePageAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                if(i == 0) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_1_img));
                    mTvStep.setText(getString(R.string.str_redelegate_step_0));
                } else if (i == 1 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_2_img));
                    mTvStep.setText(getString(R.string.str_redelegate_step_1));
                    mPageAdapter.mCurrentFragment.onRefreshTab();
                } else if (i == 2 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_3_img));
                    mTvStep.setText(getString(R.string.str_redelegate_step_2));
                } else if (i == 3 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_4_img));
                    mTvStep.setText(getString(R.string.str_redelegate_step_3));
                    mPageAdapter.mCurrentFragment.onRefreshTab();
                } else if (i == 4 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_5_img));
                    mTvStep.setText(getString(R.string.str_redelegate_step_4));
                    mPageAdapter.mCurrentFragment.onRefreshTab();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });
        mViewPager.setCurrentItem(0);
        onFetchValidtors();

        if (mBaseChain.equals(KAVA_MAIN) && (WDp.getVestedCoin(mAccount.balances, TOKEN_KAVA).compareTo(BigDecimal.ZERO) > 0)) {
            Dialog_VestingAccount dialog = Dialog_VestingAccount.newInstance(null);
            dialog.setCancelable(true);
            getSupportFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAccount == null) finish();
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

    @Override
    public void onBackPressed() {
        onHideKeyboard();
        if(mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        } else {
            super.onBackPressed();
        }
    }

    public void onNextStep() {
        if(mViewPager.getCurrentItem() < mViewPager.getChildCount()) {
            onHideKeyboard();
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        }
    }

    public void onBeforeStep() {
        if(mViewPager.getCurrentItem() > 0) {
            onHideKeyboard();
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onCancelWithVesting() {
        onBackPressed();
    }

    public void onStartRedelegate() {
        Intent intent = new Intent(RedelegateActivity.this, PasswordCheckActivity.class);
        intent.putExtra(BaseConstant.CONST_PW_PURPOSE, CONST_PW_TX_SIMPLE_REDELEGATE);
        intent.putExtra("fromValidator", mFromValidator);
        intent.putExtra("toValidator", mToValidator);
        intent.putExtra("rAmount", mReDelegateAmount);
        intent.putExtra("memo", mReDelegateMemo);
        intent.putExtra("fee", mReDelegateFee);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);

    }


    private void onFetchValidtors() {
        if(mTaskCount > 0) return;
        mTaskCount = 1;
        new ValidatorInfoBondedTask(getBaseApplication(), this, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onTaskResponse(TaskResult result) {
        mTaskCount--;
        if(isFinishing()) return;
        if (result.taskType == BaseConstant.TASK_FETCH_BONDEB_VALIDATOR) {
            mToValidators.clear();
            ArrayList<Validator> temp = (ArrayList<Validator>)result.resultData;
            if(temp != null) {
                for (Validator val:temp) {
                    if(!val.operator_address.equals(mFromValidator.operator_address)) {
                        mToValidators.add(val);
                    }
                }
                WUtil.onSortByValidatorPower(mToValidators);
            }
            if(!result.isSuccess) { Toast.makeText(getBaseContext(), R.string.error_network_error, Toast.LENGTH_SHORT).show(); }
        }

        if(mTaskCount == 0) {
            mPageAdapter.mCurrentFragment.onRefreshTab();
        }
    }


    private class RedelegatePageAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public RedelegatePageAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(RedelegateStep0Fragment.newInstance(null));
            mFragments.add(RedelegateStep1Fragment.newInstance(null));
            mFragments.add(RedelegateStep2Fragment.newInstance(null));
            mFragments.add(RedelegateStep3Fragment.newInstance(null));
            mFragments.add(RedelegateStep4Fragment.newInstance(null));
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

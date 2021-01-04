package wannabit.io.bitcv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseActivity;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.dao.BacToken;
import wannabit.io.bitcv.dao.BnbToken;
import wannabit.io.bitcv.dao.IrisToken;
import wannabit.io.bitcv.fragment.SendStep0Fragment;
import wannabit.io.bitcv.fragment.SendStep1Fragment;
import wannabit.io.bitcv.fragment.SendStep2Fragment;
import wannabit.io.bitcv.fragment.SendStep3Fragment;
import wannabit.io.bitcv.fragment.SendStep4Fragment;
import wannabit.io.bitcv.model.type.Coin;
import wannabit.io.bitcv.model.type.Fee;
import wannabit.io.bitcv.network.res.ResBnbTic;

public class SendActivity extends BaseActivity {

    private ImageView               mChainBg;
    private Toolbar                 mToolbar;
    private TextView                mTitle;
    private ImageView               mIvStep;
    private TextView                mTvStep;
    private ViewPager               mViewPager;
    private SendPageAdapter         mPageAdapter;

    public String                   mStarName;
    public String                   mTagetAddress;
    public ArrayList<Coin>          mTargetCoins;
    public String                   mTargetMemo;
    public Fee                      mTargetFee;

    public IrisToken                    mIrisToken;
    public BnbToken                     mBnbToken;
    public BacToken                      mBacToken;

    public HashMap<String, ResBnbTic>   mBnbTics = new HashMap<>();
    public String                       mKavaDenom;
    public String                       mIovDenom;
    public String                       mOkDenom;
    public String                       mCertikDenom;
    public String                       mAkashDenom;
    public String                       mSecretDenom;

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
        mTitle.setText(getString(R.string.str_send_c));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIrisToken = getIntent().getParcelableExtra("irisToken");
        mBnbToken = getIntent().getParcelableExtra("bnbToken");
        mBacToken = getIntent().getParcelableExtra("bacToken");

        mBnbTics = (HashMap<String, ResBnbTic>)getIntent().getSerializableExtra("bnbTics");
        mKavaDenom = getIntent().getStringExtra("kavaDenom");
        mIovDenom = getIntent().getStringExtra("iovDenom");
        mOkDenom = getIntent().getStringExtra("okDenom");
        mCertikDenom = getIntent().getStringExtra("certikDenom");
        mAkashDenom = getIntent().getStringExtra("akashDenom");
        mSecretDenom = getIntent().getStringExtra("secretDenom");

        mTvStep.setText(getString(R.string.str_send_step_0));

        mAccount = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
        mBaseChain = BaseChain.getChain(mAccount.baseChain);
        if (mBaseChain.equals(BaseChain.COSMOS_MAIN) || mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.BAND_MAIN)) {
        } else if (mBaseChain.equals(BaseChain.IRIS_MAIN)) {
            if (mIrisToken == null) onBackPressed();
        } else if (mBaseChain.equals(BaseChain.BNB_MAIN)) {
            if (mBnbToken == null) onBackPressed();
        } else if (mBaseChain.equals(BaseChain.BAC_MAIN)) {
            if (mBacToken == null) onBackPressed();
        } else if (mBaseChain.equals(BaseChain.IOV_MAIN) || mBaseChain.equals(BaseChain.IOV_TEST)) {
            if (TextUtils.isEmpty(mIovDenom)) onBackPressed();
        } else if (mBaseChain.equals(BaseChain.KAVA_MAIN) || mBaseChain.equals(BaseChain.KAVA_TEST)) {
            if (TextUtils.isEmpty(mKavaDenom)) onBackPressed();
        } else if (mBaseChain.equals(BaseChain.OK_TEST)) {
            if (TextUtils.isEmpty(mOkDenom)) onBackPressed();
        } else if (mBaseChain.equals(BaseChain.CERTIK_MAIN) || mBaseChain.equals(BaseChain.CERTIK_TEST)) {
            if (TextUtils.isEmpty(mCertikDenom)) onBackPressed();
        } else if (mBaseChain.equals(BaseChain.SECRET_MAIN)) {
            if (TextUtils.isEmpty(mSecretDenom)) onBackPressed();
        } else if (mBaseChain.equals(BaseChain.AKASH_MAIN)) {
            if (TextUtils.isEmpty(mAkashDenom)) onBackPressed();
        }

        mPageAdapter = new SendPageAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                if(i == 0) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_1_img));
                    mTvStep.setText(getString(R.string.str_send_step_0));
                } else if (i == 1 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_2_img));
                    mTvStep.setText(getString(R.string.str_send_step_1));
                    mPageAdapter.mCurrentFragment.onRefreshTab();
                } else if (i == 2 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_3_img));
                    mTvStep.setText(getString(R.string.str_send_step_2));
                } else if (i == 3 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_4_img));
                    mTvStep.setText(getString(R.string.str_send_step_3));
                    mPageAdapter.mCurrentFragment.onRefreshTab();
                } else if (i == 4 ) {
                    mIvStep.setImageDrawable(getDrawable(R.drawable.step_5_img));
                    mTvStep.setText(getString(R.string.str_send_step_4));
                    mPageAdapter.mCurrentFragment.onRefreshTab();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });
        mViewPager.setCurrentItem(0);

//        if (mBaseChain.equals(BaseChain.KAVA_MAIN) && (WDp.getVestedCoin(mAccount.balances, TOKEN_KAVA).compareTo(BigDecimal.ZERO) > 0)) {
//            Dialog_VestingAccount dialog = Dialog_VestingAccount.newInstance(null);
//            dialog.setCancelable(true);
//            getSupportFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAccount == null) finish();
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
        onHideKeyboard();
        if(mViewPager.getCurrentItem() < 4) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        }
    }

    public void onBeforeStep() {
        onHideKeyboard();
        if(mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onCancelWithVesting() {
        onBackPressed();
    }


    public void onStartSend() {
        Intent intent = new Intent(SendActivity.this, PasswordCheckActivity.class);
        intent.putExtra(BaseConstant.CONST_PW_PURPOSE, BaseConstant.CONST_PW_TX_SIMPLE_SEND);
        intent.putExtra("toAddress", mTagetAddress);
        intent.putParcelableArrayListExtra("amount", mTargetCoins);
        intent.putExtra("memo", mTargetMemo);
        intent.putExtra("fee", mTargetFee);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
    }




    private class SendPageAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public SendPageAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(SendStep0Fragment.newInstance(null));
            mFragments.add(SendStep1Fragment.newInstance(null));
            mFragments.add(SendStep2Fragment.newInstance(null));
            mFragments.add(SendStep3Fragment.newInstance(null));
            mFragments.add(SendStep4Fragment.newInstance(null));
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

package wannabit.io.bitcv.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import wannabit.io.bitcv.activities.ClaimRewardActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.dao.Reward;
import wannabit.io.bitcv.dialog.Dialog_Reward_Small;
import wannabit.io.bitcv.model.type.Validator;
import wannabit.io.bitcv.utils.WDp;

import static wannabit.io.bitcv.base.BaseChain.AKASH_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BAND_MAIN;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_MAIN;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_TEST;
import static wannabit.io.bitcv.base.BaseChain.COSMOS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_TEST;
import static wannabit.io.bitcv.base.BaseChain.IRIS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_TEST;
import static wannabit.io.bitcv.base.BaseChain.SECRET_MAIN;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_AKASH;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_CERTIK;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_SECRET;

public class RewardStep3Fragment extends BaseFragment implements View.OnClickListener {

    private TextView        mTvRewardAmount;
    private TextView        mFeeAmount;
    private TextView        mTvFromValidators;
    private LinearLayout    mTvGoalLayer;
    private TextView        mTvGoalAddress, mMemo;
    private LinearLayout    mExpectedLayer;
    private TextView        mExpectedAmount, mExpectedPrice;
    private Button          mBeforeBtn, mConfirmBtn;
    private TextView        mDenomRewardAmount, mDenomFeeType, mDenomResultAmount;

    public static RewardStep3Fragment newInstance(Bundle bundle) {
        RewardStep3Fragment fragment = new RewardStep3Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reward_step3, container, false);
        mTvRewardAmount     = rootView.findViewById(R.id.reward_amount);
        mFeeAmount          = rootView.findViewById(R.id.reward_fees);
        mTvFromValidators   = rootView.findViewById(R.id.reward_moniker);
        mTvGoalLayer        = rootView.findViewById(R.id.reward_receive_layer);
        mTvGoalAddress      = rootView.findViewById(R.id.reward_receive_address);
        mMemo               = rootView.findViewById(R.id.memo);
        mExpectedLayer      = rootView.findViewById(R.id.expected_Layer);
        mExpectedAmount     = rootView.findViewById(R.id.expected_amount);
        mExpectedPrice      = rootView.findViewById(R.id.expected_price);
        mBeforeBtn          = rootView.findViewById(R.id.btn_before);
        mConfirmBtn         = rootView.findViewById(R.id.btn_confirm);
        mDenomRewardAmount  = rootView.findViewById(R.id.reward_amount_title);
        mDenomFeeType       = rootView.findViewById(R.id.reward_fees_type);
        mDenomResultAmount  = rootView.findViewById(R.id.expected_amount_title);

        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mDenomRewardAmount);
        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mDenomFeeType);
        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mDenomResultAmount);

        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onRefreshTab() {
        BigDecimal rewardSum    = BigDecimal.ZERO;
        BigDecimal feeAmount    = new BigDecimal(getSActivity().mRewardFee.amount.get(0).amount);
        if (getSActivity().mBaseChain.equals(COSMOS_MAIN)) {
            for (Reward reward:getSActivity().mRewards) {
                rewardSum = rewardSum.add(new BigDecimal(reward.amount.get(0).amount).setScale(0, BigDecimal.ROUND_DOWN));
            }
            mTvRewardAmount.setText(WDp.getDpAmount2(getContext(), rewardSum, 6, 6));
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 6, 6));
            if(getSActivity().mWithdrawAddress.equals(getSActivity().mAccount.address)) {
                mTvGoalLayer.setVisibility(View.GONE);
                mExpectedLayer.setVisibility(View.VISIBLE);

                BigDecimal currentAtom      = getSActivity().mAccount.getAtomBalance();
                BigDecimal expectedAtom     = currentAtom.add(rewardSum).subtract(feeAmount);
                mExpectedAmount.setText(WDp.getDpAmount2(getContext(), expectedAtom, 6, 6));
                BigDecimal expectedPrice = BigDecimal.ZERO;
                if(getBaseDao().getCurrency() != 5) {
                    expectedPrice = expectedAtom.multiply(new BigDecimal(""+getBaseDao().getLastAtomTic())).divide(new BigDecimal("1000000"), 2, RoundingMode.DOWN);
                } else {
                    expectedPrice = expectedAtom.multiply(new BigDecimal(""+getBaseDao().getLastAtomTic())).divide(new BigDecimal("1000000"), 8, RoundingMode.DOWN);
                }
                mExpectedPrice.setText(WDp.getPriceApproximatelyDp(getSActivity(), expectedPrice, getBaseDao().getCurrencySymbol(), getBaseDao().getCurrency()));

            } else {
                mTvGoalLayer.setVisibility(View.VISIBLE);
                mExpectedLayer.setVisibility(View.GONE);
            }

        } else if (getSActivity().mBaseChain.equals(IRIS_MAIN)) {
            mTvRewardAmount.setText(WDp.getDpAmount2(getContext(), getSActivity().getIrisRewardSum(), 18, 18));
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 18, 18));
            if(getSActivity().mWithdrawAddress.equals(getSActivity().mAccount.address)) {
                mTvGoalLayer.setVisibility(View.GONE);
            } else {
                mTvGoalLayer.setVisibility(View.VISIBLE);
            }
            mExpectedLayer.setVisibility(View.GONE);

        } else if (getSActivity().mBaseChain.equals(KAVA_MAIN) || getSActivity().mBaseChain.equals(KAVA_TEST)) {
            for (Reward reward:getSActivity().mRewards) {
                rewardSum = rewardSum.add(new BigDecimal(reward.amount.get(0).amount).setScale(0, BigDecimal.ROUND_DOWN));
            }
            mTvRewardAmount.setText(WDp.getDpAmount2(getContext(), rewardSum, 6, 6));
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 6, 6));
            if(getSActivity().mWithdrawAddress.equals(getSActivity().mAccount.address)) {
                mTvGoalLayer.setVisibility(View.GONE);
                mExpectedLayer.setVisibility(View.VISIBLE);

                BigDecimal currentKava      = getSActivity().mAccount.getKavaBalance();
                BigDecimal expectedKava     = currentKava.add(rewardSum).subtract(feeAmount);
                mExpectedAmount.setText(WDp.getDpAmount2(getContext(), expectedKava, 6, 6));
                BigDecimal expectedPrice = BigDecimal.ZERO;
                if(getBaseDao().getCurrency() != 5) {
                    expectedPrice = expectedKava.multiply(new BigDecimal(""+getBaseDao().getLastKavaTic())).divide(new BigDecimal("1000000"), 2, RoundingMode.DOWN);
                } else {
                    expectedPrice = expectedKava.multiply(new BigDecimal(""+getBaseDao().getLastKavaTic())).divide(new BigDecimal("1000000"), 8, RoundingMode.DOWN);
                }
                mExpectedPrice.setText(WDp.getPriceApproximatelyDp(getSActivity(), expectedPrice, getBaseDao().getCurrencySymbol(), getBaseDao().getCurrency()));

            } else {
                mTvGoalLayer.setVisibility(View.VISIBLE);
                mExpectedLayer.setVisibility(View.GONE);
            }

        } else if (getSActivity().mBaseChain.equals(BAND_MAIN)) {
            for (Reward reward:getSActivity().mRewards) {
                rewardSum = rewardSum.add(new BigDecimal(reward.amount.get(0).amount).setScale(0, BigDecimal.ROUND_DOWN));
            }
            mTvRewardAmount.setText(WDp.getDpAmount2(getContext(), rewardSum, 6, 6));
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 6, 6));
            if(getSActivity().mWithdrawAddress.equals(getSActivity().mAccount.address)) {
                mTvGoalLayer.setVisibility(View.GONE);
                mExpectedLayer.setVisibility(View.VISIBLE);

                BigDecimal currentBand     = getSActivity().mAccount.getBandBalance();
                BigDecimal expectedBand    = currentBand.add(rewardSum).subtract(feeAmount);
                mExpectedAmount.setText(WDp.getDpAmount2(getContext(), expectedBand, 6, 6));
                BigDecimal expectedPrice = BigDecimal.ZERO;
                if(getBaseDao().getCurrency() != 5) {
                    expectedPrice = expectedBand.multiply(new BigDecimal(""+getBaseDao().getLastBandTic())).divide(new BigDecimal("1000000"), 2, RoundingMode.DOWN);
                } else {
                    expectedPrice = expectedBand.multiply(new BigDecimal(""+getBaseDao().getLastBandTic())).divide(new BigDecimal("1000000"), 8, RoundingMode.DOWN);
                }
                mExpectedPrice.setText(WDp.getPriceApproximatelyDp(getSActivity(), expectedPrice, getBaseDao().getCurrencySymbol(), getBaseDao().getCurrency()));

            } else {
                mTvGoalLayer.setVisibility(View.VISIBLE);
                mExpectedLayer.setVisibility(View.GONE);
            }

        } else if (getSActivity().mBaseChain.equals(IOV_MAIN) || getSActivity().mBaseChain.equals(IOV_TEST)) {
            for (Reward reward:getSActivity().mRewards) {
                rewardSum = rewardSum.add(new BigDecimal(reward.amount.get(0).amount).setScale(0, BigDecimal.ROUND_DOWN));
            }
            mTvRewardAmount.setText(WDp.getDpAmount2(getContext(), rewardSum, 6, 6));
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 6, 6));
            if(getSActivity().mWithdrawAddress.equals(getSActivity().mAccount.address)) {
                mTvGoalLayer.setVisibility(View.GONE);
                mExpectedLayer.setVisibility(View.VISIBLE);

                BigDecimal currentIov     = getSActivity().mAccount.getIovBalance();
                BigDecimal expectedIov    = currentIov.add(rewardSum).subtract(feeAmount);
                mExpectedAmount.setText(WDp.getDpAmount2(getContext(), expectedIov, 6, 6));
                BigDecimal expectedPrice = BigDecimal.ZERO;
                if(getBaseDao().getCurrency() != 5) {
                    expectedPrice = expectedIov.multiply(new BigDecimal(""+getBaseDao().getLastIovTic())).divide(new BigDecimal("1000000"), 2, RoundingMode.DOWN);
                } else {
                    expectedPrice = expectedIov.multiply(new BigDecimal(""+getBaseDao().getLastIovTic())).divide(new BigDecimal("1000000"), 8, RoundingMode.DOWN);
                }
                mExpectedPrice.setText(WDp.getPriceApproximatelyDp(getSActivity(), expectedPrice, getBaseDao().getCurrencySymbol(), getBaseDao().getCurrency()));

            } else {
                mTvGoalLayer.setVisibility(View.VISIBLE);
                mExpectedLayer.setVisibility(View.GONE);
            }

        } else if (getSActivity().mBaseChain.equals(CERTIK_MAIN) || getSActivity().mBaseChain.equals(CERTIK_TEST)) {
            for (Reward reward:getSActivity().mRewards) {
                rewardSum = rewardSum.add(new BigDecimal(reward.amount.get(0).amount).setScale(0, BigDecimal.ROUND_DOWN));
            }
            mTvRewardAmount.setText(WDp.getDpAmount2(getContext(), rewardSum, 6, 6));
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 6, 6));
            if(getSActivity().mWithdrawAddress.equals(getSActivity().mAccount.address)) {
                mTvGoalLayer.setVisibility(View.GONE);
                mExpectedLayer.setVisibility(View.VISIBLE);

                BigDecimal currentCertik     = getSActivity().mAccount.getTokenBalance(TOKEN_CERTIK);
                BigDecimal expectedCertik    = currentCertik.add(rewardSum).subtract(feeAmount);
                mExpectedAmount.setText(WDp.getDpAmount2(getContext(), expectedCertik, 6, 6));
                BigDecimal expectedPrice = BigDecimal.ZERO;
                if(getBaseDao().getCurrency() != 5) {
                    expectedPrice = expectedCertik.multiply(new BigDecimal(""+getBaseDao().getLastCertikTic())).divide(new BigDecimal("1000000"), 2, RoundingMode.DOWN);
                } else {
                    expectedPrice = expectedCertik.multiply(new BigDecimal(""+getBaseDao().getLastCertikTic())).divide(new BigDecimal("1000000"), 8, RoundingMode.DOWN);
                }
                mExpectedPrice.setText(WDp.getPriceApproximatelyDp(getSActivity(), expectedPrice, getBaseDao().getCurrencySymbol(), getBaseDao().getCurrency()));

            } else {
                mTvGoalLayer.setVisibility(View.VISIBLE);
                mExpectedLayer.setVisibility(View.GONE);
            }

        } else if (getSActivity().mBaseChain.equals(SECRET_MAIN)) {
            for (Reward reward:getSActivity().mRewards) {
                rewardSum = rewardSum.add(new BigDecimal(reward.amount.get(0).amount).setScale(0, BigDecimal.ROUND_DOWN));
            }
            mTvRewardAmount.setText(WDp.getDpAmount2(getContext(), rewardSum, 6, 6));
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 6, 6));
            if(getSActivity().mWithdrawAddress.equals(getSActivity().mAccount.address)) {
                mTvGoalLayer.setVisibility(View.GONE);
                mExpectedLayer.setVisibility(View.VISIBLE);

                BigDecimal currentScrt     = getSActivity().mAccount.getTokenBalance(TOKEN_SECRET);
                BigDecimal expectedScrt    = currentScrt.add(rewardSum).subtract(feeAmount);
                mExpectedAmount.setText(WDp.getDpAmount2(getContext(), expectedScrt, 6, 6));
                BigDecimal expectedPrice = BigDecimal.ZERO;
                if(getBaseDao().getCurrency() != 5) {
                    expectedPrice = expectedScrt.multiply(new BigDecimal(""+getBaseDao().getLastSecretTic())).divide(new BigDecimal("1000000"), 2, RoundingMode.DOWN);
                } else {
                    expectedPrice = expectedScrt.multiply(new BigDecimal(""+getBaseDao().getLastSecretTic())).divide(new BigDecimal("1000000"), 8, RoundingMode.DOWN);
                }
                mExpectedPrice.setText(WDp.getPriceApproximatelyDp(getSActivity(), expectedPrice, getBaseDao().getCurrencySymbol(), getBaseDao().getCurrency()));

            } else {
                mTvGoalLayer.setVisibility(View.VISIBLE);
                mExpectedLayer.setVisibility(View.GONE);
            }

        } else if (getSActivity().mBaseChain.equals(AKASH_MAIN)) {
            for (Reward reward:getSActivity().mRewards) {
                rewardSum = rewardSum.add(new BigDecimal(reward.amount.get(0).amount).setScale(0, BigDecimal.ROUND_DOWN));
            }
            mTvRewardAmount.setText(WDp.getDpAmount2(getContext(), rewardSum, 6, 6));
            mFeeAmount.setText(WDp.getDpAmount2(getContext(), feeAmount, 6, 6));
            if(getSActivity().mWithdrawAddress.equals(getSActivity().mAccount.address)) {
                mTvGoalLayer.setVisibility(View.GONE);
                mExpectedLayer.setVisibility(View.VISIBLE);

                BigDecimal currentAkt     = getSActivity().mAccount.getTokenBalance(TOKEN_AKASH);
                BigDecimal expectedAkt    = currentAkt.add(rewardSum).subtract(feeAmount);
                mExpectedAmount.setText(WDp.getDpAmount2(getContext(), expectedAkt, 6, 6));
                BigDecimal expectedPrice = BigDecimal.ZERO;
                if(getBaseDao().getCurrency() != 5) {
                    expectedPrice = expectedAkt.multiply(new BigDecimal(""+getBaseDao().getLastAkashTic())).divide(new BigDecimal("1000000"), 2, RoundingMode.DOWN);
                } else {
                    expectedPrice = expectedAkt.multiply(new BigDecimal(""+getBaseDao().getLastAkashTic())).divide(new BigDecimal("1000000"), 8, RoundingMode.DOWN);
                }
                mExpectedPrice.setText(WDp.getPriceApproximatelyDp(getSActivity(), expectedPrice, getBaseDao().getCurrencySymbol(), getBaseDao().getCurrency()));

            } else {
                mTvGoalLayer.setVisibility(View.VISIBLE);
                mExpectedLayer.setVisibility(View.GONE);
            }

        }

        String monikers = "";
        for (Validator validator:getSActivity().mValidators) {
            if(TextUtils.isEmpty(monikers)) {
                monikers = validator.description.moniker;
            } else {
                monikers = monikers + ",    " + validator.description.moniker;
            }
        }
        mTvFromValidators.setText(monikers);
        mTvGoalAddress.setText(getSActivity().mWithdrawAddress);
        mMemo.setText(getSActivity().mRewardMemo);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            if(onCheckValidateRewardAndFee()) {
                getSActivity().onStartReward();

            } else {
                Dialog_Reward_Small dialog = Dialog_Reward_Small.newInstance();
                dialog.setCancelable(true);
                dialog.show(getFragmentManager().beginTransaction(), "dialog");
            }
        }
    }

    private boolean onCheckValidateRewardAndFee() {
        if (getSActivity().mBaseChain.equals(COSMOS_MAIN) || getSActivity().mBaseChain.equals(KAVA_MAIN) || getSActivity().mBaseChain.equals(KAVA_TEST) ||
                getSActivity().mBaseChain.equals(BAND_MAIN) || getSActivity().mBaseChain.equals(IOV_MAIN) || getSActivity().mBaseChain.equals(IOV_TEST) ||
                getSActivity().mBaseChain.equals(CERTIK_MAIN) || getSActivity().mBaseChain.equals(CERTIK_TEST) || getSActivity().mBaseChain.equals(AKASH_MAIN) || getSActivity().mBaseChain.equals(SECRET_MAIN)) {
            BigDecimal rewardSum    = BigDecimal.ZERO;
            BigDecimal feeAmount    = new BigDecimal(getSActivity().mRewardFee.amount.get(0).amount);
            for (Reward reward:getSActivity().mRewards) {
                rewardSum = rewardSum.add(new BigDecimal(reward.amount.get(0).amount).setScale(0, BigDecimal.ROUND_DOWN));
            }
            return feeAmount.compareTo(rewardSum) < 0;

        } else if (getSActivity().mBaseChain.equals(IRIS_MAIN)) {
            BigDecimal rewardSum    = getSActivity().getIrisRewardSum();
            BigDecimal feeAmount    = new BigDecimal(getSActivity().mRewardFee.amount.get(0).amount);
            return feeAmount.compareTo(rewardSum) < 0;
        }
        return false;
    }


    private ClaimRewardActivity getSActivity() {
        return (ClaimRewardActivity)getBaseActivity();
    }
}
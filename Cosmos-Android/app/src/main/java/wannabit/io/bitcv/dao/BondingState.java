package wannabit.io.bitcv.dao;

import java.math.BigDecimal;

import wannabit.io.bitcv.model.type.Validator;

public class BondingState {
    public Long         accountId;
    public String       validatorAddress;
    public BigDecimal   shares;
    public Long         fetchTime;

    public BondingState() {
    }

    public BondingState(Long accountId, String validatorAddress, BigDecimal shares, Long fetchTime) {
        this.accountId = accountId;
        this.validatorAddress = validatorAddress;
        this.shares = shares;
        this.fetchTime = fetchTime;
    }


    public BigDecimal getBondingAmount(Validator validator) {
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = new BigDecimal(validator.tokens).multiply(shares).divide(new BigDecimal(validator.delegator_shares), 0, BigDecimal.ROUND_DOWN);

        } catch (Exception e) {
        } finally {
            return result;
        }
    }

}
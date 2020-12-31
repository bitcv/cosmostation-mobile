package wannabit.io.bitcv.dao;

import java.math.BigDecimal;
import java.util.ArrayList;

import wannabit.io.bitcv.model.type.Coin;
import wannabit.io.bitcv.base.BaseConstant;

public class TotalReward {
    public Long                 accountId;
    public ArrayList<Coin>      coins;

    public TotalReward(Long accountId, ArrayList<Coin> coins) {
        this.accountId = accountId;
        this.coins = coins;
    }

    public BigDecimal getAtomAmount() {
        BigDecimal result = BigDecimal.ZERO;
        for(Coin coin:coins) {
            if(coin.denom.equals(BaseConstant.TOKEN_ATOM)) {
                result = new BigDecimal(coin.amount);
                break;
            }
        }
        return result;
    }
}

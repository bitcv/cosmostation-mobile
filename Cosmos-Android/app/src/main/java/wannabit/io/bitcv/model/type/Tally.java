package wannabit.io.bitcv.model.type;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.RoundingMode;

import wannabit.io.bitcv.network.res.ResStakingPool;

public class Tally {

    @SerializedName("yes")
    public String yes;

    @SerializedName("abstain")
    public String abstain;

    @SerializedName("no")
    public String no;

    @SerializedName("no_with_veto")
    public String no_with_veto;

    public BigDecimal sum() {
        return new BigDecimal(yes).add(new BigDecimal(abstain)).add(new BigDecimal(no)).add(new BigDecimal(no_with_veto));
    }

    public BigDecimal getYesPer() {
        if (sum().equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO.setScale(2);
        }
        return new BigDecimal(yes).movePointRight(2).divide(sum(), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getNoPer() {
        if (sum().equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO.setScale(2);
        }
        return new BigDecimal(no).movePointRight(2).divide(sum(), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAbstainPer() {
        if (sum().equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO.setScale(2);
        }
        return new BigDecimal(abstain).movePointRight(2).divide(sum(), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getVetoPer() {
        if (sum().equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO.setScale(2);
        }
        return new BigDecimal(no_with_veto).movePointRight(2).divide(sum(), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTurnout(ResStakingPool pool) {
//        if (sum().equals(BigDecimal.ZERO)) {
//            return BigDecimal.ZERO.setScale(2);
//        }
//        return new BigDecimal(yes).movePointRight(2).divide(sum(), 2, RoundingMode.DOWN);
        if (pool == null) {
            return BigDecimal.ZERO.setScale(2);

        } else if (sum().equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO.setScale(2);

        } else {
            BigDecimal bondedToken = new BigDecimal(pool.result.bonded_tokens);
            return sum().movePointRight(2).divide(bondedToken, 2, RoundingMode.HALF_UP);
        }

    }
}

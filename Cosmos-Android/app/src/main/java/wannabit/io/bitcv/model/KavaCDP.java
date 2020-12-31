package wannabit.io.bitcv.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

import wannabit.io.bitcv.model.type.Coin;

public class KavaCDP {

    @SerializedName("id")
    public String id;

    @SerializedName("owner")
    public String owner;

    @SerializedName("type")
    public String type;

    @SerializedName("collateral")
    public Coin collateral;

    @SerializedName("principal")
    public Coin principal;

    @SerializedName("accumulated_fees")
    public Coin accumulated_fees;

    @SerializedName("fees_updated")
    public String fees_updated;

    public BigDecimal getAccumulatedFees() {
        BigDecimal result = BigDecimal.ZERO;
        if (accumulated_fees != null) {
            result = new BigDecimal(accumulated_fees.amount);
        }
        return result;
    }

    public int getCdpId() {
        return Integer.parseInt(id);
    }

}

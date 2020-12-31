package wannabit.io.bitcv.network.req;

import com.google.gson.annotations.SerializedName;

import wannabit.io.bitcv.model.StdTx;

public class ReqBroadCast {

    @SerializedName("mode")
    public String returns;

    @SerializedName("tx")
    public StdTx.Value tx;

}

package wannabit.io.bitcv.network.res;

import com.google.gson.annotations.SerializedName;

public class ResKavaMarketPrice {
    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Result result;

    public class Result {
        @SerializedName("market_id")
        public String market_id;

        @SerializedName("price")
        public String price;

    }
}

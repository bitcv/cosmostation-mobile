package wannabit.io.bitcv.network.res;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.bitcv.model.type.BnbHistory;

public class ResBnbHistories {

    @SerializedName("tx")
    public ArrayList<BnbHistory> tx;

}

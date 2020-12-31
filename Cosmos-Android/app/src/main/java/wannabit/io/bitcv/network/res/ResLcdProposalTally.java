package wannabit.io.bitcv.network.res;

import com.google.gson.annotations.SerializedName;

import wannabit.io.bitcv.model.type.Tally;

public class ResLcdProposalTally {
    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Tally result;

}

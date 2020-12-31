package wannabit.io.bitcv.network.res;

import com.google.gson.annotations.SerializedName;

import wannabit.io.bitcv.model.type.Proposal;

public class ResLcdProposal {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Proposal result;
}

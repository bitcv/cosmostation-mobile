package wannabit.io.bitcv.network.res;

import com.google.gson.annotations.SerializedName;

import wannabit.io.bitcv.model.type.Vote;

public class ResMyVote {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Vote result;
}

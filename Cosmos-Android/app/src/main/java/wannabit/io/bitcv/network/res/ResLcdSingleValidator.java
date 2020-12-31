package wannabit.io.bitcv.network.res;

import com.google.gson.annotations.SerializedName;

import wannabit.io.bitcv.model.type.Validator;

public class ResLcdSingleValidator {

    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public Validator result;
}

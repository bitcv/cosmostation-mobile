package wannabit.io.bitcv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.bitcv.model.type.Fee;
import wannabit.io.bitcv.model.type.Signature;
import wannabit.io.bitcv.model.type.Msg;

public class StdTx {

    @SerializedName("type")
    public String type;

    @SerializedName("value")
    public Value value;

    public static class Value {

        @SerializedName("msg")
        public ArrayList<Msg> msg;

        @SerializedName("fee")
        public Fee fee;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @SerializedName("signatures")
        public ArrayList<Signature> signatures;

        @SerializedName("memo")
        public String memo;
    }
}

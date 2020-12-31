package wannabit.io.bitcv.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.bitcv.model.type.Fee;
import wannabit.io.bitcv.model.type.Msg;

public class HistoryStdTx {

    @SerializedName("type")
    public String type;

    @SerializedName("value")
    public HistoryStdTx.Value value;

    public static class Value {

        @SerializedName("msg")
        public ArrayList<Msg> msg;

        @SerializedName("fee")
        public Fee fee;

        @SerializedName("memo")
        public String memo;
    }
}

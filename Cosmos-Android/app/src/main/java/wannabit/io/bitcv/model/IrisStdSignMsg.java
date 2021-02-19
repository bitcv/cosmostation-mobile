package wannabit.io.bitcv.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.nio.charset.Charset;
import java.util.ArrayList;

import wannabit.io.bitcv.model.type.Fee;
import wannabit.io.bitcv.model.type.Msg;

public class IrisStdSignMsg {

    @SerializedName("chain_id")
    public String chain_id;

    @SerializedName("account_number")
    public String account_number;

    @SerializedName("sequence")
    public String sequence;

    @SerializedName("fee")
    public Fee fee;

    @SerializedName("msgs")
    public ArrayList<Msg.Value> msgs;

    @SerializedName("memo")
    public String memo;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public byte[] getToSignByte() {
        Gson Presenter = new GsonBuilder().disableHtmlEscaping().create();
        return Presenter.toJson(this).getBytes(Charset.forName("UTF-8"));
    }
}
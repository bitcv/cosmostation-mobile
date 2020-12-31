package wannabit.io.bitcv.network.res;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.bitcv.model.StarNameAccount;

public class ResIovStarNameAccount {
    @SerializedName("height")
    public String height;

    @SerializedName("result")
    public IovAccountValue result;

    public class IovAccountValue {
        @SerializedName("accounts")
        public ArrayList<StarNameAccount> accounts;

    }
}

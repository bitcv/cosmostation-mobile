package wannabit.io.bitcv.network.res;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wannabit.io.bitcv.model.type.Coin;

public class ResLcdUnBonding {

    @SerializedName("delegator_addr")
    public String delegator_addr;

    @SerializedName("validator_addr")
    public String validator_addr;

    @SerializedName("entries")
    public ArrayList<Entry> entries;

    public class Entry {
        @SerializedName("creation_height")
        public String creation_height;

        @SerializedName("completion_time")
        public String completion_time;

        @SerializedName("initial_balance")
        @Expose
        private Object initial_balance;

        @SerializedName("balance")
        @Expose
        private Object balance;

        public String getinitial_balance() {
            String result = "";
            try {
                Coin temp = new Gson().fromJson(new Gson().toJson(initial_balance), Coin.class);
                result = temp.amount;

            } catch (Exception e) {

            }
            try {
                String temp = new Gson().fromJson(new Gson().toJson(initial_balance), String.class);
                result = temp;

            } catch (Exception e) {

            }
            return result;
        }

        public String getbalance() {
            String result = "";
            try {
                Coin temp = new Gson().fromJson(new Gson().toJson(balance), Coin.class);
                result = temp.amount;

            } catch (Exception e) {

            }
            try {
                String temp = new Gson().fromJson(new Gson().toJson(balance), String.class);
                result = temp;

            } catch (Exception e) {

            }
            return result;
        }
    }

    //13k
    @SerializedName("delegator_address")
    public String delegator_address;

    @SerializedName("validator_address")
    public String validator_address;

    //iris
    @SerializedName("creation_height")
    public String creation_height;

    @SerializedName("min_time")
    public String min_time;

    @SerializedName("initial_balance")
    public String initial_balance;

    @SerializedName("balance")
    public String balance;

}

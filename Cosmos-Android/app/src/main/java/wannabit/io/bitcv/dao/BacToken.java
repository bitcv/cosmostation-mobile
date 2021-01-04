package wannabit.io.bitcv.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.model.type.Coin;
import wannabit.io.bitcv.model.type.Validator;

import static wannabit.io.bitcv.base.BaseConstant.BAC_MAIN_DENOM;
import static wannabit.io.bitcv.base.BaseConstant.BAC_TOKEN_DECIMAL;
import static wannabit.io.bitcv.base.BaseConstant.BAC_TOKEN_DESCRIPTION;
import static wannabit.io.bitcv.base.BaseConstant.BAC_TOKEN_HOME;
import static wannabit.io.bitcv.base.BaseConstant.BAC_TOKEN_ISSUE_TIME;
import static wannabit.io.bitcv.base.BaseConstant.BAC_TOKEN_SUPPLY;
import static wannabit.io.bitcv.base.BaseConstant.BAC_TOKEN_SYMBOL;
import static wannabit.io.bitcv.base.BaseConstant.BCV_MAIN_DENOM;
import static wannabit.io.bitcv.base.BaseConstant.BCV_TOKEN_DECIMAL;
import static wannabit.io.bitcv.base.BaseConstant.BCV_TOKEN_DESCRIPTION;
import static wannabit.io.bitcv.base.BaseConstant.BCV_TOKEN_HOME;
import static wannabit.io.bitcv.base.BaseConstant.BCV_TOKEN_ISSUE_TIME;
import static wannabit.io.bitcv.base.BaseConstant.BCV_TOKEN_SUPPLY;
import static wannabit.io.bitcv.base.BaseConstant.BCV_TOKEN_SYMBOL;


public class BacToken implements  Parcelable {
    public static int BAC_TOKEN_TYPE_BEP2 = 1;
    public static int BAC_TOKEN_TYPE_MINI = 2;

    @SerializedName("owner_address")
    public String owner;

    @SerializedName("exchange_address")
    public String exchange_addr;

    @SerializedName("outer_name")
    public String symbol;

    @SerializedName("inner_symbol")
    public String original_symbol;

    @SerializedName("supply_num")
    public String total_supply;

    @SerializedName("margin")
    public Coin margincoin;

    @SerializedName("precision")
    public int decimal;

    @SerializedName("website")
    public String website;

    @SerializedName("description")
    public String description;

    @SerializedName("exchange_rate")
    public String exchange_rate;


    @SerializedName("timestamp")
    public String timestamp;

    public int type = 0;

    public BacToken(String symbol){
        if(symbol == BAC_TOKEN_SYMBOL) {
            this.setValue(
                    BAC_TOKEN_SYMBOL, BAC_MAIN_DENOM, BAC_TOKEN_SUPPLY, BAC_TOKEN_DECIMAL, BAC_TOKEN_HOME,
                    BAC_TOKEN_DESCRIPTION, BAC_TOKEN_ISSUE_TIME);
        }else if(symbol == BCV_TOKEN_SYMBOL){
            this.setValue(BCV_TOKEN_SYMBOL, BCV_MAIN_DENOM, BCV_TOKEN_SUPPLY, BCV_TOKEN_DECIMAL, BCV_TOKEN_HOME,
                    BCV_TOKEN_DESCRIPTION, BCV_TOKEN_ISSUE_TIME);
        }
    }
    public BacToken(String symbol, String original_symbol, String total_supply, int decimal, String website, String description, String timestamp)
    {
        this.setValue(symbol, original_symbol, total_supply, decimal, website, description, timestamp);

    }
    public void setValue(String symbol, String original_symbol, String total_supply, int decimal, String website, String description, String timestamp){
        this.owner = "SYSTEM";
        this.exchange_addr = "SYSTEM";
        this.symbol = symbol;
        this.original_symbol = original_symbol;
        this.total_supply = total_supply;
        this.margincoin = new Coin(BaseConstant.BCV_MAIN_DENOM, "0"
        );
        this.decimal = decimal;
        this.website = website;
        this.description = description;
        this.exchange_rate = "0";
        this.timestamp = timestamp;
    }
    public BacToken(String owner, String exchange_addr, String symbol, String original_symbol,  String total_supply,
                    Coin margin, int decimal, String website, String description, String exchange_rate, String timestamp) {
        this.owner = owner;
        this.exchange_addr = exchange_addr;
        this.symbol = symbol;
        this.original_symbol = original_symbol;
        this.total_supply = total_supply;
        this.margincoin = margin;
        this.decimal = decimal;
        this.website = website;
        this.description = description;
        this.exchange_rate = exchange_rate;
        this.timestamp = timestamp;
    }

    protected BacToken(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        owner = in.readString();
        exchange_addr = in.readString();
        symbol = in.readString();
        original_symbol = in.readString();
        total_supply = in.readString();
        margincoin = new Coin();
        margincoin = in.readParcelable(Coin.class.getClassLoader());
        decimal = in.readInt();
        website = in.readString();
        description = in.readString();
        exchange_rate = in.readString();
        timestamp = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(owner);
        dest.writeString(exchange_addr);
        dest.writeString(symbol);
        dest.writeString(original_symbol);
        dest.writeString(total_supply);
        dest.writeParcelable(margincoin, flags);
        dest.writeInt(decimal);
        dest.writeString(website);
        dest.writeString(description);
        dest.writeString(exchange_rate);
        dest.writeString(timestamp);
    }

    public static final Parcelable.Creator<BacToken> CREATOR = new Parcelable.Creator<BacToken>() {
        @Override
        public BacToken createFromParcel(Parcel in) {
            return new BacToken(in);
        }

        @Override
        public BacToken[] newArray(int size) {
            return new BacToken[size];
        }
    };
}



